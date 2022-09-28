package roomdoor.directdealboard.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {

	NOTICE("공지사항 게시판"),
	SALE("판매 게시판"),
	BUY("구매 게시판"),
	REPORT("신고 게시판"),
	QNA("QNA 게시판"),

	;

	private final String description;
}
