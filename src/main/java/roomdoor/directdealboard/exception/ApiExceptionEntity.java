package roomdoor.directdealboard.exception;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.http.HttpStatus;
import roomdoor.directdealboard.type.ErrorCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiExceptionEntity {

	private HttpStatus httpStatus;

	private ErrorCode errorCode;

	private String message;

	private LocalDateTime timestamp;
}
