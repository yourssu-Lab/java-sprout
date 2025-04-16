package com.yourssu.roomescape.util;

import jakarta.servlet.http.Cookie;

public class ValidationUtils {

    public static String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (CookieConstants.TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
