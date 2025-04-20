package com.yourssu.roomescape.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

    public static final String TOKEN = "token";

    private CookieUtil() {}

    public static String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
