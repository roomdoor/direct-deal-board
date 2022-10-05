package roomdoor.directdealboard.dto;

import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class HeartDto {
	@NotNull
	private Long postsId;

	@NotNull
	private String userId;
}
