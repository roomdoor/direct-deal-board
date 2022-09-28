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
public class Orders extends BaseEntity{

	@Id
	private String orderNumber;

	private String deliveryCompany;
	private String deliveryNumber;

	private String userId;
	private Long PostsId;

	private String seller;
	private String buyer;
	private boolean isReceived;

}
