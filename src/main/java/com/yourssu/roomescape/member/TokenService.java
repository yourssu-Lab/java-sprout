package com.yourssu.roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    // 쿠키 배열에서 토큰 추출하는 메서드 (내부용)
    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 전달된 토큰을 파싱해서 JWT subject 값을 Long 타입의 memberId로 변환하는 메서드
    public Long getMemberIdFromToken(String token) {
        return Long.valueOf(
                Jwts.parser()
                        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }

    // HttpServletRequest에서 쿠키를 추출하여 토큰 파싱까지 한 번에 처리하는 편리한 메서드
    public Long getMemberIdFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        if (token == null) {
            return null;
        }
        return getMemberIdFromToken(token);
    }
}