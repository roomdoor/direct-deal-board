package roomdoor.directdealboard.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomdoor.directdealboard.dto.UserDto;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@PostMapping("/create")
	public ResponseEntity<UserDto.Response> userCreate(
		@RequestBody @Valid UserDto.CreateRequest createRequest
//		, Errors errors
	) {

//		if (errors.hasErrors()) {
//			Map<String, String> validResult = userService.validateHandler(errors);
//			return new ResponseEntity<>(validResult, HttpStatus.BAD_REQUEST);
//		}

		return new ResponseEntity<>(userService.userCreate(createRequest), HttpStatus.CREATED);
	}

	@GetMapping("/email-auth")
	public ResponseEntity<?> emailAuth(@RequestParam String uuid, @RequestParam String email) {
		userService.emailAuth(uuid, email);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<UserDto.Response> userDelete(
		@RequestBody UserDto.DeleteRequest deleteRequest) {
		return new ResponseEntity<>(userService.userDelete(deleteRequest), HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<UserDto.Response> userUpdate(
		@RequestBody UserDto.UpdateRequest updateRequest) {

		return new ResponseEntity<>(userService.userUpdate(updateRequest), HttpStatus.OK);
	}

	@GetMapping("/list")
	public ResponseEntity<List<UserDto.Response>> userList() {
		return new ResponseEntity<>(userService.list(), HttpStatus.OK);
	}
}