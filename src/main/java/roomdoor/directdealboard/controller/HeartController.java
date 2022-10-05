package roomdoor.directdealboard.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomdoor.directdealboard.dto.HeartDto;
import roomdoor.directdealboard.service.HeartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heart")
public class HeartController {

	private final HeartService heartService;

	@PostMapping("/plus")
	public ResponseEntity<HeartDto> heart(@RequestBody @Valid HeartDto heartDto) {
		heartService.heart(heartDto);
		return new ResponseEntity<>(heartDto, HttpStatus.CREATED);
	}

	@PostMapping("/minus")
	public ResponseEntity<HeartDto> unHeart(@RequestBody @Valid HeartDto heartDto) {
		heartService.unHeart(heartDto);
		return new ResponseEntity<>(heartDto, HttpStatus.OK);
	}
}
