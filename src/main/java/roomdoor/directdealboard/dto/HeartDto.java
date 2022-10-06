package roomdoor.directdealboard.dto;

import javax.validation.constraints.NotNull;
import lombok.*;
import roomdoor.directdealboard.entity.Heart;

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

	private Long nowHeartCount;

	public static HeartDto of(Heart heart) {

		return HeartDto.builder()
			.postsId(heart.getPostsId())
			.userId(heart.getUserId())
			.build();
	}
}
