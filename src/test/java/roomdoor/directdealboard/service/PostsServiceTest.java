package roomdoor.directdealboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import roomdoor.directdealboard.dto.PostsDto;
import roomdoor.directdealboard.dto.PostsDto.Response;
import roomdoor.directdealboard.dto.PostsDto.UpdateRequest;
import roomdoor.directdealboard.entity.Posts;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.exception.PostsException;
import roomdoor.directdealboard.exception.UserException;
import roomdoor.directdealboard.repository.PostsRepository;
import roomdoor.directdealboard.repository.UserRepository;
import roomdoor.directdealboard.type.Category;
import roomdoor.directdealboard.type.ErrorCode;

@ExtendWith(MockitoExtension.class)
@Transactional
class PostsServiceTest {

	@Mock
	private PostsRepository postsRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private PostsService postsService;


	@DisplayName("01. get posts success")
	@Test
	public void test_01() {
		//given
		given(postsRepository.findById(any())).willReturn(Optional.of(Posts.builder()
			.id(1L)
			.title("test글 제목")
			.text("글 내용")
			.views(0L)
			.build()));

		given(postsRepository.save(any())).willReturn(any());

		//when
		PostsDto.Response posts = postsService.getPosts(1L);

		//then
		assertEquals(posts.getTitle(), "test글 제목");
		assertEquals(posts.getText(), "글 내용");
	}

	@DisplayName("01_01. get user fail")
	@Test
	public void test_01_01() {
		//given
		given(postsRepository.findById(any())).willReturn(Optional.empty());

		//when
		assertThrows(PostsException.class, () -> postsService.getPosts(1L));

		//then
	}

	@DisplayName("02. list")
	@Test
	public void test_02() {
		//given
		List<Posts> postsList = new ArrayList<>();
		postsList.add(Posts.builder().build());
		postsList.add(Posts.builder().build());
		postsList.add(Posts.builder().build());

		given(postsRepository.findAll()).willReturn(postsList);

		//when
		List<PostsDto.Response> posts = postsService.list();

		//then
		assertEquals(posts.size(), 3);
	}

	@DisplayName("03. user create success")
	@Test
	public void test_03() {
		//given
		List<Category> categories = new ArrayList<>();
		categories.add(Category.멍떄리는중);
		given(postsRepository.save(any())).willReturn(Posts.builder()
			.writer("이연주")
			.title("멍때리는 중")
			.text("아 시험지도 다 점검했고 수행평가 채점도 다 했는데 시화는 아직 공부중이다 방해하지말고 멍떄려야지...")
			.views(0L)
			.likeCount(0L)
			.categories(categories)
			.build());

		//when
		Response posts = postsService.create(PostsDto.CreateRequest.builder()
			.writer("이연주")
			.title("멍떄리는 중")
			.text("아 시험지도 다 점검했고 수행평가 채점도 다 했는데 시화는 아직 공부중이다 방해하지말고 멍떄려야지...")
			.build());

		//then
		assertEquals(posts.getWriter(), "이연주");
		assertEquals(posts.getTitle(), "멍때리는 중");
		assertEquals(posts.getCategories().get(0), Category.멍떄리는중);
	}

	@DisplayName("04. user update success")
	@Test
	public void test_04() {
		//given
		given(postsRepository.findById(any())).willReturn(Optional.of(Posts.builder()
			.writer("이연주")
			.title("멍때리는 중")
			.text("아 시험지도 다 점검했고 수행평가 채점도 다 했는데 시화는 아직 공부중이다 방해하지말고 멍떄려야지...")
			.views(0L)
			.likeCount(0L)
			.categories(new ArrayList<>())
			.build()));
		given(postsRepository.save(any())).willReturn(Posts.builder()
			.writer("이연주")
			.title("휴지에 그림 그리기")
			.text("시화 기다리다가 심심해서 휴지에 그림 그리는중!!")
			.views(0L)
			.likeCount(0L)
			.categories(new ArrayList<>())
			.build());

		//when
		Response posts = postsService.update(UpdateRequest.builder()
			.writer("이연주")
			.title("휴지에 그림 그리기")
			.text("시화 기다리다가 심심해서 휴지에 그림 그리는중!!")
			.build());

		//then

		assertEquals(posts.getTitle(), "휴지에 그림 그리기");
		assertEquals(posts.getText(), "시화 기다리다가 심심해서 휴지에 그림 그리는중!!");
	}

	@DisplayName("04_01. user update fail")
	@Test
	public void test_04_01() {
		//given
		given(postsRepository.findById(any())).willThrow(
			new PostsException(ErrorCode.NOT_FOUND_POSTS));
		//when
		assertThrows(PostsException.class, () -> postsService.update(UpdateRequest.builder()
			.writer("이연주")
			.title("휴지에 그림 그리기")
			.text("시화 기다리다가 심심해서 휴지에 그림 그리는중!!")
			.build()));
		//then
	}

	@DisplayName("05 . posts delete")
	@Test
	public void test_05() {
		//given
		given(postsRepository.findById(any())).willReturn(Optional.of(Posts.builder()
			.id(1L)
			.build()));

		given(userRepository.findById(any())).willReturn(Optional.of(User.builder()
			.id("ss@ss.com")
			.password("222")
			.build()));

//		given(postsRepository.delete(any()))

		//when

		postsService.delete(PostsDto.DeleteRequest.builder()
			.id(1L)
			.writerId("ss@ss.com")
			.writerPassword("222")
			.build());

		//then
	}

	@DisplayName("05_01 . posts delete fail not found posts")
	@Test
	public void test_05_01() {
		//given
		given(postsRepository.findById(any())).willReturn(Optional.empty());

		//when

		assertThrows(PostsException.class,
			() -> postsService.delete(PostsDto.DeleteRequest.builder()
				.id(1L)
				.writerId("ss@ss.com")
				.writerPassword("222")
				.build()));

		//then
	}

	@DisplayName("05_02 . posts delete fail not found user")
	@Test
	public void test_05_02() {
		//given
		given(postsRepository.findById(any())).willReturn(Optional.of(Posts.builder()
			.id(1L)
			.build()));

		given(userRepository.findById(any())).willReturn(Optional.empty());

		//when

		assertThrows(UserException.class,
			() -> postsService.delete(PostsDto.DeleteRequest.builder()
				.id(1L)
				.writerId("ss@ss.com")
				.writerPassword("222")
				.build()));

		//then
	}

	@DisplayName("05_03 . posts delete fail writer password mismatch")
	@Test
	public void test_05_03() {
		//given
		given(postsRepository.findById(any())).willReturn(Optional.of(Posts.builder()
			.id(1L)
			.build()));

		given(userRepository.findById(any())).willReturn(Optional.of(User.builder()
			.id("ss@ss.com")
			.password("222")
			.build()));

//		given(postsRepository.delete(any()))

		//when
		assertThrows(UserException.class, () -> postsService.delete(PostsDto.DeleteRequest.builder()
			.id(1L)
			.writerId("ss@ss.com")
			.writerPassword("비밀번호 틀렸대요")
			.build()));

		//then
	}
}