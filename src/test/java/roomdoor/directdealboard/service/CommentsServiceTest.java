package roomdoor.directdealboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import roomdoor.directdealboard.dto.CommentsDto.DeleteRequest;
import roomdoor.directdealboard.dto.CommentsDto.Response;
import roomdoor.directdealboard.dto.CommentsDto.UpdateRequest;
import roomdoor.directdealboard.entity.Comments;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.exception.exception.CommentsException;
import roomdoor.directdealboard.exception.exception.UserException;
import roomdoor.directdealboard.repository.CommentsRepository;
import roomdoor.directdealboard.repository.UserRepository;
import roomdoor.directdealboard.type.ErrorCode;

@ExtendWith(MockitoExtension.class)
@Transactional
class CommentsServiceTest {

	@Mock
	private CommentsRepository commentsRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@InjectMocks
	private CommentsService commentsService;

	@DisplayName("01_00. get comments success")
	@Test
	public void test_01_00() {
		//given
		given(commentsRepository.findById(any())).willReturn(
			Optional.of(Comments.builder()
				.id(1L)
				.comments("comments test")
				.writerNickName("tester")
				.userId("ss@ss.com")
				.build()));
		//when
		Response comment = commentsService.getComment(1L);

		//then
		assertEquals(comment.getId(), 1L);
		assertEquals(comment.getComments(), "comments test");
		assertEquals(comment.getWriterNickName(), "tester");
	}

	@DisplayName("01_01. get comments fail")
	@Test
	public void test_01_01() {
		//given
		given(commentsRepository.findById(any())).willThrow(
			new CommentsException(ErrorCode.NOT_FOUND_COMMENTS));

		//when
		assertThrows(CommentsException.class, () -> commentsService.getComment(1L));

		//then
	}

	@DisplayName("02_00. list")
	@Test
	public void test_02_00() {
		//given
		List<Comments> list = new ArrayList<>();
		list.add(Comments.builder().build());
		list.add(Comments.builder().build());
		list.add(Comments.builder().build());
		list.add(Comments.builder().build());

		given(commentsRepository.findAllByPostsId(any())).willReturn(list);

		//when
		List<Response> list1 = commentsService.list(3L);

		//then
		assertEquals(list1.size(), 4);
	}

	@DisplayName("02_01. list page")
	@Test
	public void test_02_01() {
		//given
		List<Comments> list = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			list.add(Comments.builder()
				.comments("10개 댓글").build());
		}

		Page<Comments> listPage = new PageImpl<>(list);

		given(commentsRepository.findAllByPostsId(any(), any())).willReturn(listPage);

		//when
		Page<Response> listPage1 = commentsService.list(1L, 0);
		List<Response> list1 = listPage1.getContent();

		//then
		assertEquals(list1.size(), 10);
		assertEquals(list1.get(0).getComments(), "10개 댓글");
	}

	@DisplayName("03_00. update success")
	@Test
	public void test_03_00() {
		//given
		given(commentsRepository.findByPostsIdAndId(any(), any())).willReturn(
			Optional.of(Comments.builder()
				.comments("base")
				.build()));

		given(commentsRepository.save(any())).willReturn(Comments.builder()
			.comments("update")
			.build());

		given(userRepository.findById(any())).willReturn(Optional.of(User.builder().build()));

		given(passwordEncoder.matches(any(), any())).willReturn(true);
		//when
		Response update = commentsService.update(UpdateRequest.builder()
			.comments("update").build());

		//then
		assertEquals(update.getComments(), "update");
	}

	@DisplayName("03_01. update fail")
	@Test
	public void test_03_01() {
		//given
		given(commentsRepository.findByPostsIdAndId(any(), any())).willThrow(
			new CommentsException(ErrorCode.NOT_FOUND_COMMENTS));

		//when
		assertThrows(CommentsException.class, () -> commentsService.update(UpdateRequest.builder()
			.comments("update").build()));

		//then
	}

	@DisplayName("05_00. delete success")
	@Test
	public void test_05_00() {
		//given
		given(commentsRepository.findByPostsIdAndId(any(), any())).willReturn(
			Optional.of(Comments.builder()
				.writerNickName("room")
				.userId("ss@ss.com")
				.build()));

		given(userRepository.findById(any())).willReturn(Optional.of(User.builder()
			.password("pppp")
			.build()));

		//when
		boolean result = commentsService.delete(DeleteRequest.builder()
			.userId("ss@ss.com")
			.writerNickName("room")
			.userPassword("pppp")
			.build());

		//then
		assertTrue(result);
	}

	@DisplayName("05_01. delete fail not found comments")
	@Test
	public void test_05_01() {
		//given
		given(commentsRepository.findByPostsIdAndId(any(), any())).willThrow(
			new CommentsException(ErrorCode.NOT_FOUND_COMMENTS));

		//when
		assertThrows(CommentsException.class, () -> commentsService.delete(DeleteRequest.builder()
			.writerNickName("ss@ss.com")
			.userPassword("pppp")
			.build()));

		//then
	}

	@DisplayName("05_02. delete fail not found user")
	@Test
	public void test_05_02() {
		//given
		given(commentsRepository.findByPostsIdAndId(any(), any())).willReturn(
			Optional.of(Comments.builder()
				.writerNickName("ss@ss.com")
				.userId("ss@ss.com")
				.build()));

		given(userRepository.findById(any())).willThrow(
			new UserException(ErrorCode.NOT_FOUND_USER));

		//when
		assertThrows(UserException.class, () -> commentsService.delete(DeleteRequest.builder()
			.writerNickName("ss@ss.com")
			.userPassword("pppp")
			.build()));

		//then
	}

	@DisplayName("05_03. delete fail mismatch password")
	@Test
	public void test_05_03() {
//given
		given(commentsRepository.findByPostsIdAndId(any(), any())).willReturn(
			Optional.of(Comments.builder()
				.writerNickName("ss@ss.com")
				.userId("ss@ss.com")
				.build()));

		given(userRepository.findById(any())).willReturn(Optional.of(User.builder()
			.password("aaaa")
			.build()));

		//when
		assertThrows(CommentsException.class, () -> commentsService.delete(DeleteRequest.builder()
			.writerNickName("ss@ss.com")
			.userPassword("pppp")
			.build()));

		//then
	}

}