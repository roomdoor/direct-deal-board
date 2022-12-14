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
		private String writerNickName;

		private Long likeCount;

		public static Response of(Comments comments) {
			return Response.builder()
				.id(comments.getId())
				.comments(comments.getComments())
				.writerNickName(comments.getWriterNickName())
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

		@NotEmpty(message = "댓글 내용 입력은 필수 입니다.")
		@Size(max = 100, message = "100 글자 이하로 작성해 주세요")
		private String comments;

		@NotNull
		@NotEmpty
		private String writerNickName;

		@NotNull
		@NotEmpty
		private String userId;
	}


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class UpdateRequest {

		private Long id;
		private Long postsId;

		@NotEmpty(message = "댓글 내용 입력은 필수 입니다.")
		@Size(max = 100, message = "100 글자 이하로 작성해 주세요")
		private String comments;

		@NotNull
		@NotEmpty
		private String writerNickName;

		@NotNull
		@NotEmpty
		private String userId;

		@NotNull
		@NotEmpty
		private String userPassword;
	}


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class DeleteRequest {

		private Long id;
		private Long postsId;

		@NotNull
		@NotEmpty
		private String writerNickName;
		@NotNull
		@NotEmpty
		private String userId;
		@NotNull
		@NotEmpty
		private String userPassword;
	}
}
