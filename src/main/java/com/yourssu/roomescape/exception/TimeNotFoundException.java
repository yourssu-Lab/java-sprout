package com.yourssu.roomescape.exception;

import lombok.Getter;

@Getter
public class TimeNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public TimeNotFoundException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage() + " " + detail);
        this.errorCode = errorCode;
    }
}
