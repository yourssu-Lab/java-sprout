package com.yourssu.roomescape.enums;

public enum Cookie {
    TOKEN_NAME("token");

    private final String value;

    Cookie(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}