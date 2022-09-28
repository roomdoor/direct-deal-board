package roomdoor.directdealboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import roomdoor.directdealboard.config.SecurityConfiguration;
import roomdoor.directdealboard.dto.CommentsDto.DeleteRequest;
import roomdoor.directdealboard.dto.CommentsDto.UpdateRequest;
import roomdoor.directdealboard.entity.Comments;
import roomdoor.directdealboard.service.CommentsService;

@WebMvcTest(controllers = CommentsController.class
	, excludeFilters = {
	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class),}
)
@WithMockUser
class CommentsControllerTest {

	@MockBean
	private CommentsService commentsService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mvc;

	@DisplayName("01_00. /comments/get")
	@Test
	public void test_01_00() throws Exception {
		//given
		given(commentsService.getComment(any())).willReturn(Comments.builder()
			.id(1L)
			.writerNickName("tester")
			.comments("test comments")
			.likeCount(0L)
			.build());
		//when
		mvc.perform(get("/comments/get?id=1").contentType(MediaType.APPLICATION_JSON)
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.writerNickName").value("tester"))
			.andExpect(jsonPath("$.comments").value("test comments"))

			.andDo(print());

		//then
	}

	@DisplayName("02_00. /list")
	@Test
	public void test_02_00() throws Exception {
		//given
		List<Comments> list = new ArrayList<>();
		list.add(Comments.builder().build());
		list.add(Comments.builder().build());
		list.add(Comments.builder().build());
		given(commentsService.list(any())).willReturn(list);

		//when
		mvc.perform(get("/comments/list?id=3")
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(3))
			.andDo(print());

		//then
	}

	@DisplayName("03_00. /comments/update")
	@Test
	public void test_03_00() throws Exception {
		//given
		given(commentsService.update(any())).willReturn(Comments.builder()
			.comments("update")
			.build());

		//when
		mvc.perform(put("/comments/update").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(UpdateRequest.builder()
					.writerNickName("tester")
					.userId("userId")
					.userPassword("userPassword")
					.comments("update")
					.build()))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.comments").value("update"))
			.andDo(print());

		//then
	}

	@DisplayName("04_00. /comments/delete")
	@Test
	public void test_04_00() throws Exception {
		//given
		given(commentsService.delete(any())).willReturn(true);

		//when
		mvc.perform(delete("/comments/delete")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(DeleteRequest.builder()
					.build()))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isOk())
			.andDo(print());

		//then
	}
}