package com.yourssu.roomescape.exception;

public enum ExceptionMessage {

    NO_EXIST_MEMBER("멤버가 존재하지 않습니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
