package roomdoor.directdealboard.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomdoor.directdealboard.exception.exception.CommentsException;
import roomdoor.directdealboard.exception.exception.PostsException;
import roomdoor.directdealboard.exception.exception.UserException;
import roomdoor.directdealboard.type.ErrorCode;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handlerMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		Map<String, String> result = new HashMap<>();

		e.getBindingResult().getAllErrors().forEach((error) -> {
			result.put(((FieldError) error).getField(), error.getDefaultMessage());
		});

		return result;
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<?> handlerUserException(UserException e) {

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ApiExceptionEntity.builder()
				.httpStatus(HttpStatus.BAD_REQUEST)
				.errorCode(e.getErrorCode())
				.message(e.getMessage())
				.timestamp(LocalDateTime.now())
				.build());
	}

	@ExceptionHandler(PostsException.class)
	public ResponseEntity<?> handlerPostsException(PostsException e) {

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ApiExceptionEntity.builder()
				.httpStatus(HttpStatus.BAD_REQUEST)
				.errorCode(e.getErrorCode())
				.message(e.getMessage())
				.timestamp(LocalDateTime.now())
				.build());
	}

	@ExceptionHandler(CommentsException.class)
	public ResponseEntity<?> handlerCommentsException(CommentsException e) {

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ApiExceptionEntity.builder()
				.httpStatus(HttpStatus.BAD_REQUEST)
				.errorCode(e.getErrorCode())
				.message(e.getMessage())
				.timestamp(LocalDateTime.now())
				.build());
	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handlerException(Exception e) {

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ApiExceptionEntity.builder()
				.httpStatus(HttpStatus.BAD_REQUEST)
				.errorCode(ErrorCode.EXCEPTION)
				.message(e.getMessage())
				.timestamp(LocalDateTime.now())
				.build());
	}


}
