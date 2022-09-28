package roomdoor.directdealboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import roomdoor.directdealboard.dto.PostsDto;
import roomdoor.directdealboard.dto.PostsDto.Response;
import roomdoor.directdealboard.exception.PostsException;
import roomdoor.directdealboard.service.PostsService;
import roomdoor.directdealboard.type.Category;
import roomdoor.directdealboard.type.ErrorCode;

@WebMvcTest(controllers = PostsController.class
	, excludeFilters = {
	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class),}
)
@WithMockUser
class PostsControllerTest {

	@MockBean
	private PostsService postsService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mvc;


	@DisplayName("01_00. /posts/get success")
	@Test
	public void test_01() throws Exception {
		//given
		given(postsService.getPosts(any())).willReturn(PostsDto.Response.builder()
			.title("방송 보면서 코딩")
			.text("방송 보면서 개프 구현 중 지루하니까 이렇게 해야겠다")
			.isSailed(false)
			.category(Category.SALE)
			.views(0L)
			.likeCount(0L)
			.writer("이시화 (원래 여기 아이디(이메일)이 들어감)")
			.build());

		//when
		mvc.perform(get("/posts/get?id=1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("방송 보면서 코딩"))
			.andExpect(jsonPath("$.text").value("방송 보면서 개프 구현 중 지루하니까 이렇게 해야겠다"))
			.andExpect(jsonPath("$.writer").value("이시화 (원래 여기 아이디(이메일)이 들어감)"))
			.andDo(print());
		//then
	}

	@DisplayName("01_01. /posts/get fail")
	@Test
	public void test_01_01() throws Exception {
		//given
		given(postsService.getPosts(any())).willThrow(
			new PostsException(ErrorCode.NOT_FOUND_POSTS));

		//when
		mvc.perform(get("/posts/get?id=1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andDo(print());
		//then
	}

	@DisplayName("02_00. /posts/list")
	@Test
	public void test_02() throws Exception {
		//given
		List<PostsDto.Response> list = new ArrayList<>();
		list.add(Response.builder().build());
		list.add(Response.builder().build());
		list.add(Response.builder().build());
		given(postsService.list()).willReturn(list);

		//when
		mvc.perform(get("/posts/list"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(3))
			.andDo(print());
		//then
	}

	@DisplayName("03_00. /posts/create success")
	@Test
	public void test_03_00() throws Exception {
		//given
		given(postsService.create(any())).willReturn(PostsDto.Response.builder()
			.writer("ss@ss.com")
			.title("글 생성 텍스트 ")
			.text("test posts text")
			.isSailed(false)
			.views(0L)
			.likeCount(0L)
			.build());

		//when
		mvc.perform(post("/posts/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(PostsDto.CreateRequest.builder()
					.writer("ss@ss.com")
					.title("글 생성 텍스트 ")
					.text("test posts text")
					.build()))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.writer").value("ss@ss.com"))
			.andDo(print());

		//then
	}

	@DisplayName("03_01. /posts/create fail invalid")
	@Test
	public void test_03_01() throws Exception {
		//given
		given(postsService.create(any())).willReturn(PostsDto.Response.builder()
			.writer("ss@ss.com")
			.build());

		//when
		mvc.perform(post("/posts/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(PostsDto.CreateRequest.builder()
					.build()))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isBadRequest())
			.andDo(print());

		//then
	}

	@DisplayName("04_00. /posts/update success")
	@Test
	public void test_04_00() throws Exception {
		//given
		given(postsService.update(any())).willReturn(PostsDto.Response.builder()
			.writer("ss@ss.com")
			.title("update title")
			.text("text update but nothing change")
			.build());

		//when
		mvc.perform(put("/posts/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(PostsDto.UpdateRequest.builder()
					.id(1L)
					.writer("ss@ss.com")
					.title("update title")
					.text("text update but nothing change")
					.build()))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("update title"))
			.andExpect(jsonPath("$.text").value("text update but nothing change"))
			.andDo(print());
		//then
	}

	@DisplayName("04_01. /posts/update fail not found posts")
	@Test
	public void test_04_01() throws Exception {
		//given
		given(postsService.update(any())).willThrow(new PostsException(ErrorCode.NOT_FOUND_POSTS));

		//when
		mvc.perform(put("/posts/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(PostsDto.UpdateRequest.builder()
					.writer("ss@ss.com")
					.title("update title")
					.text("text update but nothing change")
					.build()))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isBadRequest())
			.andDo(print());
		//then
	}

	@DisplayName("04_02. /posts/update fail mismatch userId")
	@Test
	public void test_04_02() throws Exception {
		//given
		given(postsService.update(any())).willThrow(new PostsException(ErrorCode.MISMATCH_WRITER));

		//when
		mvc.perform(put("/posts/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(PostsDto.UpdateRequest.builder()
					.writer("ss@ss.com")
					.title("update title")
					.text("text update but nothing change")
					.build()))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isBadRequest())
			.andDo(print());
		//then
	}

	@DisplayName("05_00. /posts/delete success")
	@Test
	public void test_05_00() throws Exception {
		//given
		//when
		mvc.perform(delete("/posts/delete")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(PostsDto.DeleteRequest.builder()
					.id(1L)
					.writerId("ss@ss.com")
					.writerPassword("posts writer password")
					.build()))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isOk())
			.andDo(print());
		//then
	}


	@DisplayName("05_01. /posts/delete fail mismatch user")
	@Test
	public void test_05_01() throws Exception {
		//given
		given(postsService.delete(any())).willThrow(new PostsException(ErrorCode.MISMATCH_WRITER));

		//when
		mvc.perform(delete("/posts/delete")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(PostsDto.DeleteRequest.builder()
					.id(1L)
					.writerId("ss@ss.com")
					.writerPassword("posts writer password")
					.build()))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value("MISMATCH_WRITER"))
			.andDo(print());
		//then
	}

	@DisplayName("05_02. /posts/delete fail mismatch password")
	@Test
	public void test_05_02() throws Exception {
		//given
		given(postsService.delete(any())).willThrow(
			new PostsException(ErrorCode.PASSWORD_MISMATCH));

		//when
		mvc.perform(delete("/posts/delete")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(PostsDto.DeleteRequest.builder()
					.id(1L)
					.writerId("ss@ss.com")
					.writerPassword("posts writer password")
					.build()))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value("PASSWORD_MISMATCH"))
			.andDo(print());
		//then
	}
}