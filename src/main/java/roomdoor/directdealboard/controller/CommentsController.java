package roomdoor.directdealboard.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import roomdoor.directdealboard.dto.CommentsDto.Response;
import roomdoor.directdealboard.service.CommentsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentsController {

	private final CommentsService commentsService;

	@GetMapping("/get")
	public ResponseEntity<CommentsDto.Response> getComments(@RequestParam Long id) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(commentsService.getComment(id));
	}

	@GetMapping("/list")
	public ResponseEntity<List<Response>> list(@RequestParam Long id) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(commentsService.list(id));
	}

	@GetMapping("/list/page")
	public ResponseEntity<Page<Response>> list(@RequestParam Long id,
		@RequestParam int pageNumber) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(commentsService.list(id, pageNumber));
	}

	@PostMapping("/create")
	public ResponseEntity<?> create(
		@RequestBody @Valid CommentsDto.CreateRequest createRequest) {
		return new ResponseEntity<>(commentsService.create(createRequest), HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<CommentsDto.Response> update(
		@RequestBody @Valid CommentsDto.UpdateRequest updateRequest) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(commentsService.update(updateRequest));
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(@RequestBody CommentsDto.DeleteRequest deleteRequest) {
		commentsService.delete(deleteRequest);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
