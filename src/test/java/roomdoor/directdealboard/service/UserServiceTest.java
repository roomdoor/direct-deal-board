package roomdoor.directdealboard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import roomdoor.directdealboard.components.MailComponents;
import roomdoor.directdealboard.dto.UserDto.CreateRequest;
import roomdoor.directdealboard.entity.User;
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
			.id("sihwa95@naver.com")
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
		assertEquals("sihwa95@naver.com", user.getId());
	}
}