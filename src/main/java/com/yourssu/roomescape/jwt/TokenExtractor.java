package com.yourssu.roomescape.jwt;

import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import jakarta.servlet.http.Cookie;

import java.util.Arrays;

public class TokenExtractor {

    private TokenExtractor() {
        // 객체 생성 방지
    }

    public static String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            throw new CustomException(ErrorCode.COOKIE_NOT_FOUND);
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NOT_FOUND));
    }
}
