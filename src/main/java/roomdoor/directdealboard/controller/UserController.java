package roomdoor.directdealboard.controller;

import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import roomdoor.directdealboard.requestDto.UserCreateRequestDto;
import roomdoor.directdealboard.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/user/create")
	public String userCreate(UserCreateRequestDto userCreateRequestDto) {
		return "/user/create";
	}

	@PostMapping("/user/create")
	public String userCreate(Model model, @Valid UserCreateRequestDto userCreateRequestDto,
		Errors errors) {

		model.addAttribute("userCreateRequestDto", userCreateRequestDto);

		if (errors.hasErrors()) {
			Map<String, String> validResult = userService.validateHandler(errors);
			for (String key : validResult.keySet()) {
				model.addAttribute(key, validResult.get(key));
			}
			return "/user/create";
		}

		boolean result = userService.userCreate(userCreateRequestDto);
		model.addAttribute("result", result);

		return "/user/create-success";
	}
}