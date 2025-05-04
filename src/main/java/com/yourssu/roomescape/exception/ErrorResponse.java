package com.yourssu.roomescape.exception;

public record ErrorResponse(int status, String message) {
    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode.getStatus().value(), errorCode.getMessage());
    }
}
