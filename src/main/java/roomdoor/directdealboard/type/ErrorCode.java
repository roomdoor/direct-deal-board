package roomdoor.directdealboard.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	NOT_FOUND_USER("존재하지 않는 아이디 입니다."),
	EMAIL_AUTH_FAIL("이메일 인증이 필요합니다."),
	SUSPENDED_USER("정지된 회원입니다."),
	PASSWORD_MISMATCH("비밀번호가 일치하지 않습니다."),
	EMAIL_CODE_MISMATCH("이메일 코드가 일치하지 않습니다."),
	USER_NAME_MISMATCH("해당 아이디와 이름이 일치하지 않습니다."),
	NOT_FOUND_POSTS("존재하지 않는 글 입니다."),
	MISMATCH_WRITER("글 작성자가 아닙니다."),
	ALREADY_CHANGED("이미 비밀번호가 초기화 되었습니다."),


	NOT_FOUND_COMMENTS("존재하지 않는 댓글 입니다."),


	EXCEPTION("에러");

	private final String description;
}