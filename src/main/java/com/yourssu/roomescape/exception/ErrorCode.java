package com.yourssu.roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "쿠키에 토큰이 존재하지 않습니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 정보를 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
