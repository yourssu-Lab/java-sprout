package com.yourssu.roomescape;

import com.yourssu.roomescape.member.LoginRequest;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;


    public String createToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload).build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(validity)
                   .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                   .compact();
    }

    public String getPayload(String token) {
        return Jwts.parser()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody().getSubject();
    }
}
