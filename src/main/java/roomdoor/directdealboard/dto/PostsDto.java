package roomdoor.directdealboard.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import roomdoor.directdealboard.entity.Posts;
import roomdoor.directdealboard.type.Category;

@Getter
@Setter
public class PostsDto {


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class CreateRequest {


		@NotBlank(message = "제목은 필수 입력 값입니다.")
		@Size(min = 1, max = 50)
		private String title;

		@NotBlank(message = "글 내용은 필수 입력 값입니다.")
		private String text;


		private List<Category> categories;

		@NotNull
		private String writer;
	}



	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class UpdateRequest {

		@NotNull
		private Long id;

		@NotBlank(message = "제목은 필수 입력 값입니다.")
		@Size(min = 1, max = 50)
		private String title;

		@NotBlank(message = "글 내용은 필수 입력 값입니다.")
		private String text;


		private List<Category> categories;

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

		private String writerId;

		private String writerPassword;
	}


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Response {

		private String title;

		@NotBlank(message = "글 내용은 필수 입력 값입니다.")
		private String text;


		private List<Category> categories;

		@NotNull
		private String writer;
		private Long views;
		private Long likeCount;
		private boolean isSailed;

		public static Response of(Posts posts) {

			return Response.builder()
				.title(posts.getTitle())
				.text(posts.getText())
				.categories(posts.getCategories())
				.writer(posts.getWriter())
				.views(posts.getViews())
				.likeCount(posts.getLikeCount())
				.isSailed(posts.isSailed())
				.build();
		}

		public static List<Response> of(List<Posts> postsList) {
			List<Response> list = new ArrayList<>();

			for (Posts posts : postsList) {
				list.add(Response.of(posts));
			}

			return list;
		}
	}
}