package com.yourssu.roomescape.exception;

import lombok.Getter;

@Getter
public class ThemeNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public ThemeNotFoundException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage() + " " + detail);
        this.errorCode = errorCode;
    }
}
