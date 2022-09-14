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
import org.springframework.transaction.annotation.Transactional;
import roomdoor.directdealboard.components.MailComponents;
import roomdoor.directdealboard.dto.UserDto.CreateRequest;
import roomdoor.directdealboard.dto.UserDto.DeleteRequest;
import roomdoor.directdealboard.dto.UserDto.UpdateRequest;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.exception.UserException;
import roomdoor.directdealboard.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceTest {


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
		User user = userService.userCreate(createRequest);

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
		boolean result = userService.emailAuth("qwer1234", "ss@ss.com");

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
			userService.emailAuth("1234qwer", "ss@ss.com"));

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

		//when
		User user = userService.userDelete(DeleteRequest.builder()
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

		given(userRepository.save(any())).willReturn(User.builder()
			.id("ss@ss.com")
			.password("secretNumber")
			.userName("update name")
			.nickName("update nickname")
			.address("update address")
			.phoneNumber("01022221111")
			.build());

		//when
		User user = userService.userUpdate(UpdateRequest.builder()
			.id("ss@ss.com")
			.password("secretNumber")
			.userName("update name")
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
		List<User> list = userService.list();

		//then
		assertEquals(2, list.size());
		assertEquals("ss@ss.com", list.get(1).getId());
		assertEquals("aa@ss.com", list.get(0).getId());

	}

}