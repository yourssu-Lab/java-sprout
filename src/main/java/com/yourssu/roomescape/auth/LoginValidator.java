package com.yourssu.roomescape.auth;

import jakarta.servlet.http.Cookie;

public class LoginValidator {
    public static boolean hasNoCookies(Cookie[] cookies) {
        return cookies == null || cookies.length == 0;
    }

    public static boolean isInvalidToken(String token) {
        return token == null || token.isBlank();
    }
}
