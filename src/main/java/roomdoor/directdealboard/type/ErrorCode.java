package roomdoor.directdealboard.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	NOT_FOUND_USER("존재하지 않는 아이디 입니다."),
	EMAIL_AUTH_FAIL("이메일 인증이 필요합니다."),
	SUSPENDED_USER("정지된 회원입니다.")
	;

	private final String description;
}