package com.yourssu.roomescape.exception;

public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
