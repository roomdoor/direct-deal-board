package roomdoor.directdealboard.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {

	@Id
	private String id;
	private String nickName;
	private String password;
	private String phoneNumber;
	private String address;
	private int dealTotalCount;

	private boolean emailYn;
	private String passwordResetCode;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
