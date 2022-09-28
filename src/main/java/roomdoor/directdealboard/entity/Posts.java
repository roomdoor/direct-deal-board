package roomdoor.directdealboard.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.*;
import roomdoor.directdealboard.type.Category;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Posts extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String title;
	private String text;

	@Enumerated(EnumType.STRING)
	@ElementCollection
	private List<Category> categories;

	private String writer;
	private String writerId;

	private Long views;
	private Long likeCount;
	private boolean isSailed;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "posts_id")
	List<Comments> commentsList;

}
