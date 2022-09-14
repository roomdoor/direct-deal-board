package roomdoor.directdealboard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import roomdoor.directdealboard.components.MailComponents;
import roomdoor.directdealboard.dto.UserDto;
import roomdoor.directdealboard.dto.UserDto.CreateRequest;
import roomdoor.directdealboard.dto.UserDto.DeleteRequest;
import roomdoor.directdealboard.dto.UserDto.UpdateRequest;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.exception.UserException;
import roomdoor.directdealboard.repository.UserRepository;
import roomdoor.directdealboard.type.ErrorCode;
import roomdoor.directdealboard.type.UserState;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

	private final MailComponents mailComponents;

	private final HttpSession session;


	public User userCreate(UserDto.CreateRequest createRequest) {

		String uuid = emailSend(createRequest);
		String encPassword = BCrypt.hashpw(createRequest.getPassword(), BCrypt.gensalt());
		
		User user = User.DtoToUser(createRequest);
		user.setPassword(encPassword);
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
		Optional<User> optionalUser = getUser(email);

		User user = optionalUser.get();
		if (Objects.equals(user.getEmailCode(), uuid)) {
			user.setEmailYn(true);
			userRepository.save(user);
		}
		return true;
	}

	public User userDelete(DeleteRequest deleteRequest) {
		Optional<User> optionalUser = getUser(deleteRequest.getId());

		User user = optionalUser.get();
		if (user.getPassword().equals(deleteRequest.getPassword())) {
			userRepository.deleteById(deleteRequest.getId());
		}

		return user;
	}

	

	public User userUpdate(UpdateRequest updateRequest) {
		Optional<User> optionalUser = getUser(updateRequest.getId());

		User user = optionalUser.get();

		user.setAddress(updateRequest.getAddress());
		user.setUserName(updateRequest.getUserName());
		user.setNickName(updateRequest.getNickName());
		user.setPhoneNumber(updateRequest.getPhoneNumber());
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		Optional<User> optionalUser = getUser(id);

		User user = optionalUser.get();
		if (user.getUserState() == UserState.EMAIL_AUTH_NOT) {
			throw new UserException(ErrorCode.EMAIL_AUTH_FAIL);
		}

		if (user.getUserState() == UserState.SUSPEND) {
			throw new UserException(ErrorCode.SUSPENDED_USER);
		}

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		if (user.getUserState() == UserState.ADMIN) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		session.setAttribute("user", UserDto.SessionDto.of(user));

		return new org.springframework.security.core.userdetails.User(user.getId(),
			user.getPassword(), grantedAuthorities);
	}

	private Optional<User> getUser(String id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (!optionalUser.isPresent()) {
			throw new UserException(ErrorCode.NOT_FOUND_USER);
		}
		return optionalUser;
	}

	public List<User> list() {
		return userRepository.findAll();
	}
}
