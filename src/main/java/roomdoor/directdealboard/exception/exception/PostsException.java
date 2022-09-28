package roomdoor.directdealboard.exception.exception;


import lombok.*;
import roomdoor.directdealboard.type.ErrorCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostsException extends RuntimeException {

	private ErrorCode errorCode;

	private String message;

	public PostsException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = errorCode.getDescription();
	}
}
