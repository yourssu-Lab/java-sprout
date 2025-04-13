package com.yourssu.roomescape.infrastructure;

import com.yourssu.roomescape.auth.LoginValidator;
import jakarta.servlet.http.Cookie;

import java.util.Arrays;

public class CookieProvider {

    public static String findCookieByKey(Cookie[] cookies, String target) {
        if (LoginValidator.hasNoCookies(cookies)) {
            throw new RuntimeException("cookies is empty");
        }
        String token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(target))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
        if (LoginValidator.isInvalidToken(token)) {
            throw new RuntimeException("cookies is empty");
        }
        return token;
    }
}
