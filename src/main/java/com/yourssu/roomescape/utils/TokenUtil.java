package com.yourssu.roomescape.utils;

import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.property.TokenProperty;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    private final TokenProperty tokenProperty;

    public TokenUtil(TokenProperty tokenProperty) {
        this.tokenProperty = tokenProperty;
    }

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
        String secretKey = tokenProperty.getSecretKey();

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Long parseTokenToId(String token){
        return Long.valueOf(Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(tokenProperty.getSecretKey().getBytes()))
                .build()
                .parseSignedClaims(token)
                .getBody().getSubject());
    }
}
