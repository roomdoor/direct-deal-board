package roomdoor.directdealboard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import org.springframework.util.ObjectUtils;
import roomdoor.directdealboard.components.MailComponents;
import roomdoor.directdealboard.dto.CommentsDto;
import roomdoor.directdealboard.dto.PostsDto;
import roomdoor.directdealboard.dto.UserDto;
import roomdoor.directdealboard.dto.UserDto.CreateRequest;
import roomdoor.directdealboard.dto.UserDto.DeleteRequest;
import roomdoor.directdealboard.dto.UserDto.PasswordResetDto;
import roomdoor.directdealboard.dto.UserDto.Response;
import roomdoor.directdealboard.dto.UserDto.UpdateRequest;
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

	@Transactional
	public List<CommentsDto.Response> getAllComments(String id) {
		User user = getUser(id);
		return CommentsDto.Response.of(user.getCommentsList());
	}


	public Response userCreate(UserDto.CreateRequest createRequest) {

		String uuid = emailSendWhenCreate(createRequest);
		String encPassword = BCrypt.hashpw(createRequest.getPassword(), BCrypt.gensalt());

		User user = User.DtoToUser(createRequest);
		user.setPassword(encPassword);
		user.setUserState(UserState.EMAIL_AUTH_NOT);
		user.setEmailYn(false);
		user.setEmailCode(uuid);

		return UserDto.Response.of(userRepository.save(user));
	}

	private String emailSendWhenCreate(CreateRequest request) {
		String uuid = UUID.randomUUID().toString();

		String email = request.getId();
		String subject = "[direct-deal] 가입 인증 메일 입니다. ";
		String text = "<p>direct-deal 가입 인증 메일 입니다.<p>" +
			"<p>아래 링크를 클릭하셔서 이메일 인증을 해주세요</p>" +
			"<div><a target='_blank' href='http://localhost:8080/user/email-auth?uuid=" + uuid
			+ "&email=" + email + "'> 이메일 가입 인증 링크 </a></div>";
		mailComponents.sendMail(email, subject, text);
		return uuid;
	}


	public boolean emailAuthWhenCreate(String uuid, String email) {
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

		if (!ObjectUtils.isEmpty(updateRequest.getAddress())) {
			user.setAddress(updateRequest.getAddress());
		}

		if (!ObjectUtils.isEmpty(updateRequest.getNickName()) && userRepository.existsByNickName(
			updateRequest.getNickName())) {
			user.setNickName(updateRequest.getNickName());
		}

		if (!ObjectUtils.isEmpty(updateRequest.getPhoneNumber())) {
			user.setPhoneNumber(updateRequest.getPhoneNumber());
		}

		if (!ObjectUtils.isEmpty(updateRequest.getUpdatePassword())) {
			String encPassword = BCrypt.hashpw(updateRequest.getUpdatePassword(), BCrypt.gensalt());
			user.setPassword(encPassword);
		}

		return UserDto.Response.of(userRepository.save(user));
	}

	public List<Response> list() {
		return Response.of(userRepository.findAll());
	}

	public String passwordResetRequest(PasswordResetDto resetDto) {
		User user = getUser(resetDto.getUserId());

		if (!Objects.equals(user.getUserName(), resetDto.getUserName())) {
			throw new UserException(ErrorCode.USER_NAME_MISMATCH);
		}

		emailSendPasswordResetRequest(resetDto);

		return "비밀번호 초기화 인증 메일이 발송되었습니다. 이메일에서 인증링크를 통해 비밀번호를 초기화해주세요.";
	}

	private void emailSendPasswordResetRequest(PasswordResetDto request) {
		String uuid = UUID.randomUUID().toString();

		String email = request.getUserId();
		String subject = "[direct-deal] 비밀번호 초기화 메일 입니다.";
		String text = "<p>direct-deal 비밀번호 초기화 메일 입니다.<p>" +
			"<p>아래 링크를 클릭하셔서 비밀번호 초기화를 완료해주세요</p>" +
			"<div><a target='_blank' href='http://localhost:8080/user/password-reset/email-auth?uuid="
			+ uuid + "&email=" + email + "'> 비밀번호 초기화 링크 </a></div>";
		mailComponents.sendMail(email, subject, text);

		User user = getUser(request.getUserId());
		user.setPasswordResetYn(true);
		user.setPasswordResetCode(uuid);
		userRepository.save(user);
	}

	private void emailSendPasswordReset(String email, String resetPassword) {

		String subject = "[direct-deal] 초기화된 비밀번호 입니다.";
		String text = "<p>direct-deal 초기화된 비밀번호 입니다.<p>" +
			"<p>로그인 후 비밀번호를 변경해 주세요</p>" +
			"초기화된 비밀번호: " + resetPassword;
		mailComponents.sendMail(email, subject, text);
	}

	public String passwordReset(String uuid, String email) {
		String resetPassword = UUID.randomUUID().toString().substring(0, 5) + "1234@#$%";
		User user = getUser(email);
		if (!user.isPasswordResetYn()) {
			throw new UserException(ErrorCode.ALREADY_CHANGED);
		}

		if (Objects.equals(user.getPasswordResetCode(), uuid)) {
			user.setPasswordResetYn(false);
			String encPassword = BCrypt.hashpw(resetPassword, BCrypt.gensalt());
			user.setPassword(encPassword);
			userRepository.save(user);
			emailSendPasswordReset(email, resetPassword);

			return "<p> 초기화된 비밀번호를 메일로 전송하였습니다.<p>" +
				"<p>로그인 후 비밀번호를 변경해 주세요</p>";
		} else {
			throw new UserException(ErrorCode.EMAIL_CODE_MISMATCH);
		}
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

		if (user.getUserState() == UserState.DELETE) {
			throw new UserException(ErrorCode.DELETED_USER);
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
		return userRepository.findById(id)
			.orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));
	}
}
