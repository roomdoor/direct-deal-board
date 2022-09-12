package roomdoor.directdealboard.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import roomdoor.directdealboard.requestDto.UserCreateRequestDto;

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

	private boolean emailYn;
	private String passwordResetCode;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static User DtoToUser(UserCreateRequestDto userCreateRequestDto) {
		return User.builder()
			.id(userCreateRequestDto.getId())
			.userName(userCreateRequestDto.getUserName())
			.nickName(userCreateRequestDto.getNickName())
			.password(userCreateRequestDto.getPassword())
			.phoneNumber(userCreateRequestDto.getPhoneNumber())
			.address(userCreateRequestDto.getAddress())
			.build();
	}
}
