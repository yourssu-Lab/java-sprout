package com.yourssu.roomescape.util;

import com.yourssu.roomescape.exception.UnauthenticatedException;
import jakarta.servlet.http.Cookie;

public class CookieUtil {

    public static final String TOKEN = "token";

    private CookieUtil() {
    }

    public static String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            throw new UnauthenticatedException("쿠키가 없습니다.");
        }

        for (Cookie cookie : cookies) {
            if (TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new UnauthenticatedException("'token' 이름의 쿠키가 없습니다.");
    }
}
