package com.yourssu.roomescape.common.exception;

public class ErrorResponse {
    private String message;
    private String details;

    public ErrorResponse(String message, String details) {
        this.message = message;
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public String getMessage() {
        return message;
    }
}

