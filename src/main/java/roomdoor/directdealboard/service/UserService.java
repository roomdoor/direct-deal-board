package roomdoor.directdealboard.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import roomdoor.directdealboard.components.MailComponents;
import roomdoor.directdealboard.dto.UserDto;
import roomdoor.directdealboard.dto.UserDto.CreateRequest;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.repository.UserRepository;
import roomdoor.directdealboard.type.UserState;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final MailComponents mailComponents;

	public User userCreate(UserDto.CreateRequest userCreateRequestDto) {

		String uuid = emailSend(userCreateRequestDto);

		User user = User.DtoToUser(userCreateRequestDto);
		user.setUserState(UserState.EMAIL_AUTH_NOT);
		user.setEmailYn(false);
		user.setEmailCode(uuid);

		return userRepository.save(user);
	}

	private String emailSend(CreateRequest userCreateRequestDto) {
		String uuid = UUID.randomUUID().toString();

		String email = userCreateRequestDto.getId();
		String subject = "[direct-deal] 가입 인증 메일 입니다. ";
		String text = "<p>direct-deal 가입 인증 메일 입니다.<p>" +
			"<p>아래 링크를 클릭하셔서 이메일 인증을 해주세요</p>" +
			"<div><a target='_blank' href='http://localhost:8080/user/email-auth?uuid=" + uuid
			+ "&email=" + email + "'> 이메일 가입 인증 링크 </a></div>";
		mailComponents.sendMail(email, subject, text);
		return uuid;
	}

	public Map<String, String> validateHandler(Errors errors) {
		Map<String, String> result = new HashMap<>();

		for (FieldError error : errors.getFieldErrors()) {
			System.out.println(error.getField());
			System.out.println(error.getDefaultMessage());

			result.put(error.getField(), error.getDefaultMessage());
		}

		return result;
	}

	public boolean emailAuth(String uuid, String email) {
		Optional<User> optionalUser = userRepository.findById(email);
		if (!optionalUser.isPresent()) {
			return false;
		}
		User user = optionalUser.get();
		if (Objects.equals(user.getEmailCode(), uuid)) {
			user.setEmailYn(true);
			userRepository.save(user);
		}
		return true;
	}
}
