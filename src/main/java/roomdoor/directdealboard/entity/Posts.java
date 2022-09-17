package roomdoor.directdealboard.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.*;
import roomdoor.directdealboard.type.Category;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Posts {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String title;
	private String text;

	@ElementCollection
	private List<Category> categories;

	private String writer;
	private Long views;
	private Long likeCount;
	private boolean isSailed;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
