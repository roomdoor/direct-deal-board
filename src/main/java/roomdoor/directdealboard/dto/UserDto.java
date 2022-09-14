package roomdoor.directdealboard.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.context.annotation.Primary;
import roomdoor.directdealboard.anotation.NotDuplicateId;
import roomdoor.directdealboard.anotation.NotDuplicateNickName;

import lombok.*;
import roomdoor.directdealboard.entity.User;

@Getter
@Setter
public class UserDto {

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class CreateRequest {

		@NotBlank(message = "아이디는 필수 입력 값입니다.")
		@Email(message = "이메일 형식에 맞지 않습니다.")
		@NotDuplicateId
		private String id;


		@NotBlank(message = "이름은 필수 입력 값입니다.")
		@Size(min = 1, max = 10)
		private String userName;


		@NotBlank(message = "닉네임은 필수 입력 값입니다.")
		@NotDuplicateNickName
		@Size(min = 3, max = 10)
		private String nickName;

		@NotBlank(message = "전화번호는 필수 입력 값입니다.")
		@Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
		private String phoneNumber;

		@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
		@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
			message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
		private String password;

		//	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
		//	// TODO 커스텀 벨리데이터 비밀번호 같은지 확인
		//	private String rePassword;

		// TODO 주소 찾기 API 이용하여 추가
		private String address;

	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class CreateResponse {

		private String id;
		private String userName;
		private String nickName;
		private String phoneNumber;
		private String password;
		//	private String rePassword;
		private String address;

		public static CreateResponse of(UserDto.CreateRequest createRequest) {
			return CreateResponse.builder()
				.id(createRequest.getId())
				.userName(createRequest.getUserName())
				.nickName(createRequest.getNickName())
				.phoneNumber(createRequest.getPhoneNumber())
				.password(createRequest.getPassword())
				.address(createRequest.getAddress())
				.build();
		}
	}


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class DeleteRequest {

		private String id;
		private String password;

	}


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class DeleteResponse {

	}


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class UpdateRequest {

		@NotBlank(message = "아이디는 필수 입력 값입니다.")
		@Email(message = "이메일 형식에 맞지 않습니다.")
		private String id;

		@NotBlank(message = "이름은 필수 입력 값입니다.")
		@Size(min = 1, max = 10)
		private String userName;

		@NotBlank(message = "닉네임은 필수 입력 값입니다.")
		@NotDuplicateNickName
		@Size(min = 3, max = 10)
		private String nickName;

		@NotBlank(message = "전화번호는 필수 입력 값입니다.")
		@Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
		private String phoneNumber;

		@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
		private String password;

		private String address;

	}


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class SessionDto {

		private String id;
		private String nickName;

		public static SessionDto of(User user) {
			return SessionDto.builder()
				.id(user.getId())
				.nickName(user.getNickName())
				.build();
		}

	}
}
