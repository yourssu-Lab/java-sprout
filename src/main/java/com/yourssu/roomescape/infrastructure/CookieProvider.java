package com.yourssu.roomescape.infrastructure;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;

public class CookieProvider {

    public static String findCookieByKey(Cookie[] cookies, String target) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(target))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }
}
