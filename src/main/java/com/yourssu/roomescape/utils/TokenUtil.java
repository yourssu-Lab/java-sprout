package com.yourssu.roomescape.utils;

import com.yourssu.roomescape.member.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

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

    public String createToken(Member member){
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Long parseTokenToId(String token){
        return Long.valueOf(Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()))
                .build()
                .parseSignedClaims(token)
                .getBody().getSubject());
    }
}
