package roomdoor.directdealboard.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import roomdoor.directdealboard.dto.UserDto;
import roomdoor.directdealboard.type.UserState;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
public class User extends BaseEntity {

	@Id
	private String id;
	private String userName;
	private String nickName;
	private String password;
	private String phoneNumber;
	private String address;
	private int dealTotalCount;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "user_state")
	private UserState userState;

	private String emailCode;
	private boolean emailYn;
	private String passwordResetCode;
	private boolean passwordResetYn;


	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private List<Posts> postsList;


	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private List<Comments> commentsList;

	public static User DtoToUser(UserDto.CreateRequest userCreateRequestDto) {
		return User.builder()
			.id(userCreateRequestDto.getId())
			.userName(userCreateRequestDto.getUserName())
			.nickName(userCreateRequestDto.getNickName())
			.phoneNumber(userCreateRequestDto.getPhoneNumber())
			.address(userCreateRequestDto.getAddress())
			.build();
	}
}
