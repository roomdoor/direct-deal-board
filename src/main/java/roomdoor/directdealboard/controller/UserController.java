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
import roomdoor.directdealboard.dto.CommentsDto;
import roomdoor.directdealboard.dto.PostsDto;
import roomdoor.directdealboard.dto.UserDto;
import roomdoor.directdealboard.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@GetMapping("/get")
	public ResponseEntity<UserDto.Response> userDetail(@RequestParam String id) {
		return ResponseEntity.ok(userService.userDetail(id));
	}

	@GetMapping("/get/posts")
	public ResponseEntity<List<PostsDto.Response>> userDetailPosts(@RequestParam String id) {
		return ResponseEntity.ok(userService.getAllPosts(id));
	}

	@GetMapping("/get/comments")
	public ResponseEntity<List<CommentsDto.Response>> userDetailComments(@RequestParam String id) {
		return ResponseEntity.ok(userService.getAllComments(id));
	}

	@PostMapping("/create")
	public ResponseEntity<UserDto.Response> userCreate(
		@RequestBody @Valid UserDto.CreateRequest createRequest) {

		return new ResponseEntity<>(userService.userCreate(createRequest), HttpStatus.CREATED);
	}

	@GetMapping("/create/email-auth")
	public ResponseEntity<?> emailAuthWhenCreate(@RequestParam String uuid,
		@RequestParam String email) {
		userService.emailAuthWhenCreate(uuid, email);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("password-reset-request")
	public ResponseEntity<?> passwordResetRequest(@RequestBody UserDto.PasswordResetDto resetDto) {
		return ResponseEntity.ok(userService.passwordResetRequest(resetDto));
	}

	@GetMapping("/password-reset/email-auth")
	public ResponseEntity<?> emailAuthWhenPasswordReset(@RequestParam String uuid,
		@RequestParam String email) {
		return new ResponseEntity<>(userService.passwordReset(uuid, email),
			HttpStatus.OK);
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