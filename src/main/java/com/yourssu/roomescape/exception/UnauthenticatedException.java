package com.yourssu.roomescape.exception;

import lombok.Getter;

@Getter
public class UnauthenticatedException extends RuntimeException {
    private final ErrorCode errorCode;

    public UnauthenticatedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
