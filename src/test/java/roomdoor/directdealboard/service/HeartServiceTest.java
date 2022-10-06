package roomdoor.directdealboard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomdoor.directdealboard.dto.HeartDto;
import roomdoor.directdealboard.entity.Heart;
import roomdoor.directdealboard.entity.Posts;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.exception.exception.HeartException;
import roomdoor.directdealboard.exception.exception.PostsException;
import roomdoor.directdealboard.exception.exception.UserException;
import roomdoor.directdealboard.repository.HeartRepository;
import roomdoor.directdealboard.repository.PostsRepository;
import roomdoor.directdealboard.repository.UserRepository;
import roomdoor.directdealboard.type.ErrorCode;

@ExtendWith(MockitoExtension.class)
@Transactional
class HeartServiceTest {

	@Mock
	private HeartRepository heartRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PostsRepository postsRepository;

	@InjectMocks
	private HeartService heartService;

	@DisplayName("01_00. findHeartWithUserIdAndPostsId success")
	@Test
	public void test_01_00() {
		//given
		given(heartRepository.findHeartByUserIdAndPostsId(any(), any())).willReturn(Optional.of(
			Heart.builder()
				.id(1L)
				.postsId(1L)
				.userId("id")
				.build()));

		//when
		Optional<Heart> heart = heartService.findHeartWithUserIdAndPostsId(HeartDto.builder()
			.userId("id")
			.postsId(1L)
			.build());

		//then
		assertEquals(heart.get().getId(), 1L);
		assertEquals(heart.get().getPostsId(), 1L);
		assertEquals(heart.get().getUserId(), "id");

	}

	@DisplayName("02_00. heart success")
	@Test
	public void test_02_00() {
		//given
		given(postsRepository.findById(any())).willReturn(
			Optional.of(Posts.builder().id(1L).likeCount(0L).build()));

		given(userRepository.findById(any())).willReturn(
			Optional.of(User.builder().id("id").build()));

		given(heartRepository.save(any())).willReturn(Heart.builder()
			.userId("id")
			.postsId(1L)
			.build());

		//when
		HeartDto hD = heartService.heart(HeartDto.builder()
			.userId("id")
			.postsId(1L)
			.build());

		//then
		assertEquals(hD.getPostsId(), 1L);
		assertEquals(hD.getUserId(), "id");
		assertEquals(hD.getNowHeartCount(), 1L);
	}

	@DisplayName("02_01. heart fail already heart")
	@Test
	public void test_02_01() {
		//given
		given(heartRepository.findHeartByUserIdAndPostsId(any(), any())).willReturn(
			Optional.of(Heart.builder().build()));

		//when
		assertThrows(HeartException.class, () -> heartService.heart(HeartDto.builder()
			.userId("id")
			.postsId(1L)
			.build()));

		//then
	}

	@DisplayName("02_02. heart fail not found Posts")
	@Test
	public void test_02_02() {
		//given
		given(postsRepository.findById(any())).willThrow(
			new PostsException(ErrorCode.NOT_FOUND_POSTS));

		//when
		assertThrows(PostsException.class, () -> heartService.heart(HeartDto.builder()
			.userId("id")
			.postsId(1L)
			.build()));

		//then
	}

	@DisplayName("02_03. heart fail not found User")
	@Test
	public void test_02_03() {
		//given

		given(postsRepository.findById(any())).willReturn(
			Optional.of(Posts.builder().id(1L).likeCount(0L).build()));

		given(userRepository.findById(any())).willThrow(
			new UserException(ErrorCode.NOT_FOUND_USER));

		//when
		assertThrows(UserException.class, () -> heartService.heart(HeartDto.builder()
			.userId("id")
			.postsId(1L)
			.build()));

		//then
	}

	@DisplayName("03_00. unheart success")
	@Test
	public void test_03_00() {
		//given
		given(heartRepository.findHeartByUserIdAndPostsId(any(), any())).willReturn(
			Optional.of(Heart.builder()
				.id(1L)
				.postsId(1L)
				.userId("id")
				.build()));

		given(postsRepository.findById(any())).willReturn(Optional.of(Posts.builder()
			.id(1L)
			.likeCount(1L)
			.build()));

		//when
		HeartDto hD = heartService.unHeart(HeartDto.builder()
			.userId("id")
			.postsId(1L)
			.build());

		//then
		assertEquals(hD.getPostsId(), 1L);
		assertEquals(hD.getUserId(), "id");
		assertEquals(hD.getNowHeartCount(), 0L);
	}

	@DisplayName("03_01. unheart fail not found heart")
	@Test
	public void test_03_01() {
		//given
		given(heartRepository.findHeartByUserIdAndPostsId(any(), any())).willReturn(
			Optional.empty());

		//when
		assertThrows(HeartException.class, () -> heartService.unHeart(HeartDto.builder()
			.userId("id")
			.postsId(1L)
			.build()));

		//then
	}

	@DisplayName("03_02. heart fail not found Posts")
	@Test
	public void test_03_02() {
		//given
		given(heartRepository.findHeartByUserIdAndPostsId(any(), any())).willReturn(
			Optional.of(Heart.builder()
				.id(1L)
				.userId("id")
				.postsId(1L)
				.build()));

		given(postsRepository.findById(any())).willThrow(
			new PostsException(ErrorCode.NOT_FOUND_POSTS));

		//when
		assertThrows(PostsException.class, () -> heartService.unHeart(HeartDto.builder()
			.userId("id")
			.postsId(1L)
			.build()));

		//then
	}
}