package roomdoor.directdealboard.exception;

import lombok.*;
import roomdoor.directdealboard.type.ErrorCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserException extends RuntimeException{

	private ErrorCode errorCode;
	private String message;

	public UserException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = errorCode.getDescription();
	}
}
