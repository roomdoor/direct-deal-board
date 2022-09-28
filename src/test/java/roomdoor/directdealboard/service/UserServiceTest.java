package roomdoor.directdealboard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import roomdoor.directdealboard.components.MailComponents;
import roomdoor.directdealboard.dto.CommentsDto;
import roomdoor.directdealboard.dto.PostsDto;
import roomdoor.directdealboard.dto.UserDto.CreateRequest;
import roomdoor.directdealboard.dto.UserDto.DeleteRequest;
import roomdoor.directdealboard.dto.UserDto.PasswordResetDto;
import roomdoor.directdealboard.dto.UserDto.Response;
import roomdoor.directdealboard.dto.UserDto.UpdateRequest;
import roomdoor.directdealboard.entity.Comments;
import roomdoor.directdealboard.entity.Posts;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.exception.exception.UserException;
import roomdoor.directdealboard.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceTest {


	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	@Mock
	private UserRepository userRepository;

	@Mock
	private MailComponents mailComponents;

	@InjectMocks
	private UserService userService;

	@DisplayName("01. userCreate success")
	@Test
	public void test_01() {
		//given
		CreateRequest createRequest = CreateRequest.builder()
			.id("ss@ss.com")
			.userName("이시화")
			.nickName("aaaaa")
			.password("qwe12344@@")
			.phoneNumber("01012341234")
			.address("우리집")
			.build();

		given(userRepository.save(any()))
			.willReturn(User.DtoToUser(createRequest));
		given(mailComponents.sendMail(any(), any(), any())).willReturn(true);

		//when
		Response user = userService.userCreate(createRequest);

		//then
		verify(userRepository, times(1)).save(any());
		assertEquals("ss@ss.com", user.getId());
	}

	@DisplayName("02. email auth success")
	@Test
	public void test_02() {
		//given
		given(userRepository.findById((any()))).willReturn(Optional.of(User.builder()
			.id("ss@ss.com")
			.userName("nameName")
			.nickName("nickNikc")
			.emailYn(true)
			.emailCode("qwer1234")
			.build()));

		//when
		boolean result = userService.emailAuthWhenCreate("qwer1234", "ss@ss.com");

		//then
		assertTrue(result);
	}

	@DisplayName("03. email auth fail not match email code")
	@Test
	public void test_03() {
		//given
		given(userRepository.findById((any()))).willReturn(Optional.of(User.builder()
			.id("ss@ss.com")
			.userName("nameName")
			.nickName("nickNikc")
			.emailYn(false)
			.emailCode("qwer1234")
			.build()));

		//when
		assertThrows(UserException.class, () ->
			userService.emailAuthWhenCreate("1234qwer", "ss@ss.com"));

		//then
	}

	@DisplayName("04. user delete success")
	@Test
	public void test_04() {
		//given
		given(userRepository.findById((any()))).willReturn(Optional.of(User.builder()
			.id("ss@ss.com")
			.password("secretNumber")
			.userName("nameName")
			.nickName("nickNick")
			.emailYn(false)
			.emailCode("qwer1234")
			.build()));

		given(passwordEncoder.matches(any(), any())).willReturn(true);

		//when
		Response user = userService.userDelete(DeleteRequest.builder()
			.id("ss@ss.com")
			.password("secretNumber")
			.build());
		//then

		assertEquals(user.getId(), "ss@ss.com");
		assertEquals(user.getNickName(), "nickNick");
	}

	@DisplayName("05 .  user delete fail password mismatch")
	@Test
	public void test_05() {
		//given
		given(userRepository.findById((any()))).willReturn(Optional.of(User.builder()
			.id("ss@ss.com")
			.password("secretNumber")
			.userName("nameName")
			.nickName("nickNick")
			.emailYn(false)
			.emailCode("qwer1234")
			.build()));

		given(passwordEncoder.matches(any(), any())).willReturn(false);
		//when

		//then
		assertThrows(UserException.class, () -> userService.userDelete(DeleteRequest.builder()
			.id("ss@ss.com")
			.password("publicNumber")
			.build()));
	}

	@DisplayName("06. user update success")
	@Test
	public void test_06() {
		//given
		given(userRepository.findById((any()))).willReturn(Optional.of(User.builder()
			.id("ss@ss.com")
			.build()));

		given(passwordEncoder.matches(any(), any())).willReturn(true);

		given(userRepository.save(any())).willReturn(User.builder()
			.id("ss@ss.com")
			.password("secretNumber")
			.userName("update name")
			.nickName("update nickname")
			.address("update address")
			.phoneNumber("01022221111")
			.build());

		//when
		Response user = userService.userUpdate(UpdateRequest.builder()
			.id("ss@ss.com")
			.password("secretNumber")
			.nickName("update nickname")
			.address("update address")
			.phoneNumber("01022221111")
			.build());

		//then
		assertEquals(user.getNickName(), "update nickname");
		assertEquals(user.getUserName(), "update name");
		assertEquals(user.getAddress(), "update address");
		assertEquals(user.getPhoneNumber(), "01022221111");
	}

	@DisplayName("06_01. user update fail password mismatch")
	@Test
	public void test_06_01() {
		//given
		given(userRepository.findById((any()))).willReturn(Optional.of(User.builder()
			.id("ss@ss.com")
			.build()));

		given(passwordEncoder.matches(any(), any())).willReturn(false);

		//when
		assertThrows(UserException.class, () -> userService.userUpdate(UpdateRequest.builder()
			.id("ss@ss.com")
			.password("secretNumber")
			.nickName("update nickname")
			.address("update address")
			.phoneNumber("01022221111")
			.build()));

		//then
	}

	@DisplayName("07. list")
	@Test
	public void test_07() {
		//given
		User user = User.builder()
			.id("ss@ss.com")
			.userName("nameName")
			.nickName("nickNikc")
			.emailYn(true)
			.emailCode("qwer1234")
			.build();
		User user2 = User.builder()
			.id("aa@ss.com")
			.userName("nameName")
			.nickName("nickNikc")
			.emailYn(true)
			.emailCode("qwer1234")
			.build();
		List<User> givenList = new ArrayList<>();
		givenList.add(user2);
		givenList.add(user);
		given(userRepository.findAll()).willReturn(givenList);

		//when
		List<Response> list = userService.list();

		//then
		assertEquals(2, list.size());
		assertEquals("ss@ss.com", list.get(1).getId());
		assertEquals("aa@ss.com", list.get(0).getId());
	}

	@DisplayName("07_00. user get all posts")
	@Test
	public void test_07_00() {
		//given
		List<Posts> postsList = new ArrayList<>();
		postsList.add(Posts.builder().build());
		postsList.add(Posts.builder().build());
		postsList.add(Posts.builder().build());
		postsList.add(Posts.builder().build());
		postsList.add(Posts.builder().build());
		given(userRepository.findById(any())).willReturn(
			Optional.of(User.builder().id("11").postsList(postsList).build()));
		//when
		List<PostsDto.Response> allPosts = userService.getAllPosts("11");
		//then
		assertEquals(allPosts.size(), 5);

	}

	@DisplayName("08_00. user get all comments")
	@Test
	public void test_08_00() {
		//given
		List<Comments> commentsList = new ArrayList<>();
		commentsList.add(Comments.builder().build());
		commentsList.add(Comments.builder().build());
		commentsList.add(Comments.builder().build());
		commentsList.add(Comments.builder().build());
		given(userRepository.findById(any())).willReturn(
			Optional.of(User.builder().id("11").commentsList(commentsList).build()));
		//when
		List<CommentsDto.Response> allPosts = userService.getAllComments("11");
		//then
		assertEquals(allPosts.size(), 4);
	}

	@DisplayName("09_00. passwordResetRequest success")
	@Test
	public void test_09_00() {
		//given
		given(userRepository.findById(any()))
			.willReturn(Optional.of(User.builder()
				.id("ss@ss.com")
				.userName("테스터")
				.build()));

		given(userRepository.save(any())).willReturn(any());

		//when
		String result = userService.passwordResetRequest(PasswordResetDto.builder()
			.userId("ss@ss.com")
			.userName("테스터")
			.build());

		//then
		assertEquals(result,
			"비밀번호 초기화 인증 메일이 발송되었습니다. 이메일에서 인증링크를 통해 비밀번호를 초기화해주세요.");
	}

	@DisplayName("09_01. passwordResetRequest fail misMatch name id")
	@Test
	public void test_09_01() {
		//given
		given(userRepository.findById(any()))
			.willReturn(Optional.of(User.builder()
				.id("ss@ss.com")
				.userName("실전용")
				.build()));

		//when
		assertThrows(UserException.class,
			() -> userService.passwordResetRequest(PasswordResetDto.builder()
				.userId("ss@ss.com")
				.userName("테스터")
				.build()));

		//then
	}

	@DisplayName("10_00. passwordReset success")
	@Test
	public void test_10_00() {
		//given
		given(userRepository.findById(any()))
			.willReturn(Optional.of(User.builder()
				.id("ss@ss.com")
				.userName("실전용")
				.passwordResetYn(true)
				.passwordResetCode("1234qwer")
				.build()));

		//when
		String result = userService.passwordReset("1234qwer", "ss@ss.com");

		//then
		assertEquals(result, "<p> 초기화된 비밀번호를 메일로 전송하였습니다.<p>" +
			"<p>로그인 후 비밀번호를 변경해 주세요</p>");
	}

	@DisplayName("10_01. passwordReset fail ")
	@Test
	public void test_10_01() {
		//given
		given(userRepository.findById(any()))
			.willReturn(Optional.of(User.builder()
				.id("ss@ss.com")
				.userName("실전용")
				.passwordResetYn(false)
				.passwordResetCode("1234qwer")
				.build()));

		//when
		assertThrows(UserException.class, () -> userService.passwordReset("1234qwer", "ss@ss.com"));

		//then
	}


}