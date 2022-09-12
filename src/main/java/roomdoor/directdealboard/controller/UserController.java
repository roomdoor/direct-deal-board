package roomdoor.directdealboard.controller;

import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomdoor.directdealboard.dto.UserDto;
import roomdoor.directdealboard.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/user/create")
	public ResponseEntity<?> userCreate(
		@RequestBody @Valid UserDto.CreateRequest userCreateRequestDto, Errors errors) {

		if (errors.hasErrors()) {
			Map<String, String> validResult = userService.validateHandler(errors);
			return new ResponseEntity<>(validResult, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(userService.userCreate(userCreateRequestDto), HttpStatus.OK);
	}

	@GetMapping("/user/email-auth")
	public String emailAuth(@RequestParam String uuid, @RequestParam String email) {
		boolean result = userService.emailAuth(uuid, email);

		if (result) {
			return "인증이 완료 되었습니다.";
		} else {
			return "인증이 실패하였습니다.";
		}
	}
}