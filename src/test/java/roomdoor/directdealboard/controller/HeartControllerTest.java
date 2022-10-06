package roomdoor.directdealboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.Validator;
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
import roomdoor.directdealboard.dto.HeartDto;
import roomdoor.directdealboard.service.HeartService;
import roomdoor.directdealboard.service.PostsService;

@WebMvcTest(controllers = HeartController.class
	, excludeFilters = {
	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class),}
)
@WithMockUser
class HeartControllerTest {

	@MockBean
	private HeartService heartService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private Validator validatorInjected;


	@DisplayName("01_00.  heart success")
	@Test
	public void test_01_00() throws Exception {
		//given
		HeartDto heart = HeartDto.builder()
			.postsId(123L)
			.userId("id")
			.nowHeartCount(1L)
			.build();

		given(heartService.heart(any())).willReturn(heart);

		//when
		mvc.perform(post("/heart/plus")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(heart))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.postsId").value(123L))
			.andExpect(jsonPath("$.userId").value("id"))
			.andExpect(jsonPath("$.nowHeartCount").value(1L))
			.andDo(print());
		//then
	}

	@DisplayName("02_00.  unHeart success")
	@Test
	public void test_02_00() throws Exception {
		//given
		HeartDto heart = HeartDto.builder()
			.postsId(123L)
			.userId("id")
			.nowHeartCount(1L)
			.build();

		given(heartService.unHeart(any())).willReturn(heart);

		//when
		mvc.perform(post("/heart/minus")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(heart))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.postsId").value(123L))
			.andExpect(jsonPath("$.userId").value("id"))
			.andExpect(jsonPath("$.nowHeartCount").value(1L))
			.andDo(print());
		//then
	}

}