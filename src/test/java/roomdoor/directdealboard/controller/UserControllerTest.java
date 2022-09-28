package roomdoor.directdealboard.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomdoor.directdealboard.dto.CommentsDto;
import roomdoor.directdealboard.dto.PostsDto;
import roomdoor.directdealboard.dto.UserDto;
import roomdoor.directdealboard.exception.exception.UserException;
import roomdoor.directdealboard.repository.UserRepository;
import roomdoor.directdealboard.service.UserService;
import roomdoor.directdealboard.type.ErrorCode;

@WebMvcTest(UserController.class)
class UserControllerTest {


	@MockBean
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@DisplayName("01. /user/creat success")
	@Test
	public void test_01() throws Exception {
		//given
		given(userService.userCreate(any())).willReturn(UserDto.Response.builder()
			.id("ss@ss.com")
			.userName("이시화")
			.build());

		given(userRepository.existsById(any())).willReturn(false);
		given(userRepository.existsByNickName(any())).willReturn(false);

		//when
		//then
		mvc.perform(post("/user/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(UserDto.CreateRequest.builder()
					.id("ss@ss.com")
					.userName("이시화")
					.nickName("room")
					.password("qwe12344@@")
					.phoneNumber("01011112222")
					.address("우리집")
					.build())
				)
			).andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value("ss@ss.com"))
			.andExpect(jsonPath("$.userName").value("이시화"))
			.andDo(print());
	}

	@DisplayName("02_01. /user/create fail invalid duplicate Id")
	@Test
	public void test_02() throws Exception {
		//given
		given(userService.userCreate(any())).willReturn(UserDto.Response.builder()
			.id("ss@ss.com")
			.userName("이시화")
			.build());

		given(userRepository.existsById(any())).willReturn(true);
		given(userRepository.existsByNickName(any())).willReturn(false);

		//when
		//then
		mvc.perform(post("/user/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(UserDto.CreateRequest.builder()
					.id("ss@ss.com")
					.userName("이시화")
					.nickName("room")
					.password("qwe12344@@")
					.phoneNumber("01011112222")
					.address("우리집")
					.build())
				)
			).andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("02_02. /user/create fail invalid duplicate nickname")
	@Test
	public void test_02_02() throws Exception {
		//given
		given(userService.userCreate(any())).willReturn(UserDto.Response.builder()
			.id("ss@ss.com")
			.userName("이시화")
			.build());

		given(userRepository.existsById(any())).willReturn(false);
		given(userRepository.existsByNickName(any())).willReturn(true);

		//when
		//then
		mvc.perform(post("/user/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(UserDto.CreateRequest.builder()
					.id("ss@ss.com")
					.userName("이시화")
					.nickName("room")
					.password("qwe12344@@")
					.phoneNumber("01011112222")
					.address("우리집")
					.build())
				)
			).andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("02_03. /user/create fail invalid")
	@Test
	public void test_02_03() throws Exception {
		//given
		given(userService.userCreate(any())).willReturn(UserDto.Response.builder()
			.id("ss@ss.com")
			.userName("이시화")
			.build());

		given(userRepository.existsById(any())).willReturn(false);
		given(userRepository.existsByNickName(any())).willReturn(false);

		//when
		//then
		mvc.perform(post("/user/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(UserDto.CreateRequest.builder()
					.build())
				)
			).andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("03_01. /user/email-auth success")
	@Test
	public void test_03_01() throws Exception {
		//given
		given(userService.emailAuth(any(), any())).willReturn(true);
		//when
		//then
		mvc.perform(get("/user/email-auth?uuid=1111&email=sihwa95@naver.com")
				.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk())
			.andDo(print());
	}

	@DisplayName("03_02. /user/email-auth fail ")
	@Test
	public void test_03_02() throws Exception {
		//given
		given(userService.emailAuth(any(), any())).willThrow(
			new UserException(ErrorCode.EMAIL_CODE_MISMATCH));
		//when

		//then
		mvc.perform(get("/user/email-auth?uuid=1111&email=sihwa95@naver.com")
			).andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value("EMAIL_CODE_MISMATCH"))
			.andDo(print());
	}

	@DisplayName("04. /user/delete success")
	@Test
	public void test_04() throws Exception {
		//given
		given(userService.userDelete(any())).willReturn(UserDto.Response.builder()
			.id("지운 아이디")
			.build());
		//when
		//then
		mvc.perform(delete("/user/delete")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(UserDto.DeleteRequest.builder()
					.id("ss@ss.com")
					.password("1234")
					.build()))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value("지운 아이디"))
			.andDo(print());
	}

	@DisplayName("04_01. /user/delete fail")
	@Test
	public void test_04_01() throws Exception {
		//given
		given(userService.userDelete(any())).willThrow(
			new UserException(ErrorCode.NOT_FOUND_USER));
		//when
		//then
		mvc.perform(delete("/user/delete")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(UserDto.DeleteRequest.builder()
					.id("ss@ss.com")
					.password("1234")
					.build()))
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value("NOT_FOUND_USER"))
			.andDo(print());
	}

	@DisplayName("05 . /user/update success")
	@Test
	public void test_05() throws Exception {
		//given
		given(userService.userUpdate(any())).willReturn(UserDto.Response.builder()
			.id("ss@ss.com")
			.userName("이시화 업데이트")
			.build());

		//when

		mvc.perform(put("/user/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(UserDto.UpdateRequest.builder()
					.id("ss@ss.com")
					.userName("이시화 업데이트")
					.nickName("room")
					.password("qwe12344@@")
					.phoneNumber("01011112222")
					.address("우리집")
					.build())))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value("ss@ss.com"))
			.andExpect(jsonPath("$.userName").value("이시화 업데이트"))
			.andDo(print());

		//then
	}

	@DisplayName("05_01 . /user/update fail")
	@Test
	public void test_05_01() throws Exception {
		//given
		given(userService.userUpdate(any()))
			.willThrow(new UserException(ErrorCode.NOT_FOUND_USER));

		//when

		mvc.perform(put("/user/update")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(UserDto.UpdateRequest.builder()
				.id("ss@ss.com")
				.build())))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorCode").value("NOT_FOUND_USER"));

		//then
	}

	@DisplayName("06. /user/list")
	@Test
	public void test_06() throws Exception {
		//given
		List<UserDto.Response> userList = new ArrayList<>();
		userList.add(UserDto.Response.builder().id("111").build());
		userList.add(UserDto.Response.builder().id("222").build());
		userList.add(UserDto.Response.builder().id("333").build());
		given(userService.list()).willReturn(userList);

		//when
		mvc.perform(get("/user/list"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(3))
			.andDo(print());

		//then
	}

	@DisplayName("07_00. /user/get/posts")
	@Test
	public void test_07_00() throws Exception {
	    //given
		List<PostsDto.Response> postsList = new ArrayList<>();
		postsList.add(PostsDto.Response.builder().build());
		postsList.add(PostsDto.Response.builder().build());
		postsList.add(PostsDto.Response.builder().build());
		given(userService.getAllPosts(any())).willReturn(postsList);

		//when
		mvc.perform(get("/user/get/posts?id=1"))
			.andExpect(jsonPath("$.length()").value(3))
			.andDo(print());
	    //then
	}

	@DisplayName("08_00. /user/get/comments")
	@Test
	public void test_08_00() throws Exception {
		//given
		List<CommentsDto.Response> commentsList = new ArrayList<>();
		commentsList.add(CommentsDto.Response.builder().build());
		commentsList.add(CommentsDto.Response.builder().build());
		commentsList.add(CommentsDto.Response.builder().build());
		commentsList.add(CommentsDto.Response.builder().build());
		given(userService.getAllComments(any())).willReturn(commentsList);

		//when
		mvc.perform(get("/user/get/comments?id=1"))
			.andExpect(jsonPath("$.length()").value(4))
			.andDo(print());
		//then
	}
}