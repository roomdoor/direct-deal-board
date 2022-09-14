package roomdoor.directdealboard.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import roomdoor.directdealboard.dto.UserDto;
import roomdoor.directdealboard.type.UserState;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {

	@Id
	private String id;
	private String userName;
	private String nickName;
	private String password;
	private String phoneNumber;
	private String address;
	private int dealTotalCount;
	@Enumerated(EnumType.STRING)
	private UserState userState;

	private String emailCode;
	private boolean emailYn;
	private String passwordResetCode;
	private boolean passwordResetYn;


	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static User DtoToUser(UserDto.CreateRequest userCreateRequestDto) {
		return User.builder()
			.id(userCreateRequestDto.getId())
			.userName(userCreateRequestDto.getUserName())
			.nickName(userCreateRequestDto.getNickName())
			.phoneNumber(userCreateRequestDto.getPhoneNumber())
			.address(userCreateRequestDto.getAddress())
			.build();
	}

	@Override
	public String toString() {
		return "User{" +
			"id='" + id + '\'' +
			", userName='" + userName + '\'' +
			", nickName='" + nickName + '\'' +
			", password='" + password + '\'' +
			", phoneNumber='" + phoneNumber + '\'' +
			", address='" + address + '\'' +
			", dealTotalCount=" + dealTotalCount +
			", emailYn=" + emailYn +
			", passwordResetCode='" + passwordResetCode + '\'' +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			'}';
	}
}
