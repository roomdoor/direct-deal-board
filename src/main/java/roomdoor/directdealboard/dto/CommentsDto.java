package roomdoor.directdealboard.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.var;
import org.springframework.util.ObjectUtils;
import roomdoor.directdealboard.entity.Comments;

@Getter
@Setter
public class CommentsDto {


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Response {

		private long id;
		private String comments;
		private String writer;
		private Long likeCount;

		public static Response of(Comments comments) {
			return Response.builder()
				.id(comments.getId())
				.comments(comments.getComments())
				.writer(comments.getWriter())
				.likeCount(comments.getLikeCount())
				.build();
		}

		public static List<Response> of(List<Comments> comments) {
			if (ObjectUtils.isEmpty(comments)) {
				return new ArrayList<>();
			}

			List<Response> responses = new ArrayList<>();
			for (var comment : comments) {
				responses.add(Response.of(comment));
			}

			return responses;
		}

	}


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class CreateRequest {

		@NotNull
		private Long postsId;

		@NotEmpty
		@Size(max = 100)
		private String comments;

		@NotNull
		private String writer;
	}


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class UpdateRequest {

		private Long id;
		private Long postsId;

		@NotEmpty
		@Size(max = 100)
		private String comments;

		@NotNull
		private String writer;
	}


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class DeleteRequest {

		private Long id;
		private Long postsId;

		private String writer;
		private String writerPassword;
	}
}
