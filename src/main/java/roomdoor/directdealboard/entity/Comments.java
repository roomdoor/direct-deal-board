package roomdoor.directdealboard.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Comments extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "posts_id")
	private Long postsId;
//	@ManyToOne(fetch = FetchType.LAZY)
//	@ToString.Exclude
//	private Posts posts;

	private String comments;
	private String writer;
	private Long likeCount;

}
