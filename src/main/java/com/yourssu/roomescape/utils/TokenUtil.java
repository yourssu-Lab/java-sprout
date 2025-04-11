package com.yourssu.roomescape.utils;

import jakarta.servlet.http.Cookie;

public class TokenUtil {

    public String extractTokenFromCookie(Cookie[] cookies){
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
