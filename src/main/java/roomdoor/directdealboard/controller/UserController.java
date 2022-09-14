package roomdoor.directdealboard.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomdoor.directdealboard.dto.UserDto;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/user/create")
	public ResponseEntity<?> userCreate(
		@RequestBody @Valid UserDto.CreateRequest createRequest
//		, Errors errors
	) {

//		if (errors.hasErrors()) {
//			Map<String, String> validResult = userService.validateHandler(errors);
//			return new ResponseEntity<>(validResult, HttpStatus.BAD_REQUEST);
//		}

		return new ResponseEntity<>(userService.userCreate(createRequest), HttpStatus.CREATED);
	}

	@GetMapping("/user/email-auth")
	public ResponseEntity<?> emailAuth(@RequestParam String uuid, @RequestParam String email) {
		userService.emailAuth(uuid, email);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/user/delete")
	public ResponseEntity<?> userDelete(@RequestBody UserDto.DeleteRequest deleteRequest) {
		User user = userService.userDelete(deleteRequest);

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PutMapping("/user/update")
	public ResponseEntity<?> userUpdate(@RequestBody UserDto.UpdateRequest updateRequest
//		, Errors errors
	) {

//		if (errors.hasErrors()) {
//			Map<String, String> validResult = userService.validateHandler(errors);
//			return new ResponseEntity<>(validResult, HttpStatus.BAD_REQUEST);
//		}

		return new ResponseEntity<>(userService.userUpdate(updateRequest), HttpStatus.OK);
	}

	@GetMapping("/user/list")
	public ResponseEntity<?> userList() {
		List<User> userList = userService.list();

		return new ResponseEntity<>(userList, HttpStatus.OK);
	}
}