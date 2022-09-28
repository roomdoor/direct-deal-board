package roomdoor.directdealboard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomdoor.directdealboard.components.MailComponents;
import roomdoor.directdealboard.dto.PostsDto;
import roomdoor.directdealboard.dto.UserDto;
import roomdoor.directdealboard.dto.UserDto.CreateRequest;
import roomdoor.directdealboard.dto.UserDto.DeleteRequest;
import roomdoor.directdealboard.dto.UserDto.Response;
import roomdoor.directdealboard.dto.UserDto.UpdateRequest;
import roomdoor.directdealboard.entity.Posts;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.exception.exception.UserException;
import roomdoor.directdealboard.repository.UserRepository;
import roomdoor.directdealboard.type.ErrorCode;
import roomdoor.directdealboard.type.UserState;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

	private final MailComponents mailComponents;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;


	public UserDto.Response userDetail(String id) {
		return UserDto.Response.of(getUser(id));
	}

	@Transactional
	public List<PostsDto.Response> getAllPosts(String id) {
		User user = getUser(id);
		return PostsDto.Response.of(user.getPostsList());
	}

	public Response userCreate(UserDto.CreateRequest createRequest) {

		String uuid = emailSend(createRequest);
		String encPassword = BCrypt.hashpw(createRequest.getPassword(), BCrypt.gensalt());

		User user = User.DtoToUser(createRequest);
		user.setPassword(encPassword);
		user.setUserState(UserState.EMAIL_AUTH_NOT);
		user.setEmailYn(false);
		user.setEmailCode(uuid);

		return UserDto.Response.of(userRepository.save(user));
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

	public boolean emailAuth(String uuid, String email) {
		User user = getUser(email);

		if (Objects.equals(user.getEmailCode(), uuid)) {
			user.setEmailYn(true);
			user.setUserState(UserState.NORMAL);
			userRepository.save(user);
			return true;
		} else {
			throw new UserException(ErrorCode.EMAIL_CODE_MISMATCH);
		}
	}

	public Response userDelete(DeleteRequest deleteRequest) {
		User user = getUser(deleteRequest.getId());
		if (!bCryptPasswordEncoder.matches(deleteRequest.getPassword(), user.getPassword())) {
			throw new UserException(ErrorCode.PASSWORD_MISMATCH);
		}
		userRepository.deleteById(deleteRequest.getId());

		return UserDto.Response.of(user);
	}


	public Response userUpdate(UpdateRequest updateRequest) {
		User user = getUser(updateRequest.getId());
		if (!bCryptPasswordEncoder.matches(updateRequest.getPassword(), user.getPassword())) {
			throw new UserException(ErrorCode.PASSWORD_MISMATCH);
		}

		user.setAddress(updateRequest.getAddress());
		user.setUserName(updateRequest.getUserName());
		user.setNickName(updateRequest.getNickName());
		user.setPhoneNumber(updateRequest.getPhoneNumber());

		return UserDto.Response.of(userRepository.save(user));
	}

	public List<Response> list() {
		return Response.of(userRepository.findAll());
	}

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		User user = getUser(id);

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

		return new org.springframework.security.core.userdetails.User(user.getId(),
			user.getPassword(), grantedAuthorities);
	}

	private User getUser(String id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (!optionalUser.isPresent()) {
			throw new UserException(ErrorCode.NOT_FOUND_USER);
		}
		return optionalUser.get();
	}
}
