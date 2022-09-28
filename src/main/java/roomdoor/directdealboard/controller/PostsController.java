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
import roomdoor.directdealboard.dto.PostsDto;
import roomdoor.directdealboard.service.PostsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostsController {

	private final PostsService postsService;

	@GetMapping("/get")
	public ResponseEntity<PostsDto.Response> getPosts(@RequestParam Long id) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(postsService.getPosts(id));
	}

	@GetMapping("/list")
	public ResponseEntity<List<PostsDto.Response>> list() {
		return ResponseEntity.status(HttpStatus.OK)
			.body(postsService.list());
	}

	@PostMapping("/create")
	public ResponseEntity<?> create(
		@RequestBody @Valid PostsDto.CreateRequest createRequest) {
		return new ResponseEntity<>(postsService.create(createRequest), HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<PostsDto.Response> update(
		@RequestBody @Valid PostsDto.UpdateRequest updateRequest) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(postsService.update(updateRequest));
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(@Valid @RequestBody PostsDto.DeleteRequest deleteRequest) {
		postsService.delete(deleteRequest);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
