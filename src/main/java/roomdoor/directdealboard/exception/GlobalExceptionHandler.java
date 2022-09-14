package roomdoor.directdealboard.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
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

}
