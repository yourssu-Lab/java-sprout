package com.yourssu.roomescape.exception;

public enum ExceptionMessage {

    NO_EXIST_MEMBER("멤버가 존재하지 않습니다."),
    NO_EXIST_TIME("시간이 존재하지 않습니다."),
    NO_EXIST_THEME("테마가 존재하지 않습니다."),
    NO_EXIST_RESERVATION("예약이 존재하지 않습니다."),
    ALREADY_EXIST_RESERVATION("이미 예약이 존재합니다."),
    CANCLE_DENY_RESERVATION("예약은 대기 상태일 때만 취소할 수 있습니다."),

    ;
    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
