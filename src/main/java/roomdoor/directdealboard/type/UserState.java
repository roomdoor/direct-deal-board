package roomdoor.directdealboard.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserState {
	EMAIL_AUTH_NOT("이메일 인증 필요"),
	NORMAL("사용 가능 상태"),
	SUSPEND("제한 상태"),
	ADMIN("관리자")
	;

	private final String description;

}
