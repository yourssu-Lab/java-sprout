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
    String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            throw new TokenNotFoundException();
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new TokenNotFoundException();
    }

    private static class TokenNotFoundException extends RuntimeException {
        public TokenNotFoundException() {
            super("쿠키에서 토큰을 찾을 수 없습니다.");
        }
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

//    public String createToken(String email, String password) {
//
//        return Jwts.builder()
//                .setSubject(email)
//                .claim("password", password)
//                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                .compact();
//    }

}