package com.yourssu.roomescape.util;

import com.yourssu.roomescape.config.JwtProperties;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.exception.UnauthenticatedException;
import com.yourssu.roomescape.member.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long validityInMilliseconds;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        this.validityInMilliseconds = jwtProperties.getExpireLength();
    }

    public String createToken(Member member) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .subject(member.getEmail())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    private Claims parseClaims(String token) {
        if (token == null || token.isBlank()) {
            throw new UnauthenticatedException(ErrorCode.TOKEN_NOT_FOUND);
        }

        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new UnauthenticatedException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new UnauthenticatedException(ErrorCode.UNSUPPORTED_TOKEN);
        } catch (MalformedJwtException e) {
            throw new UnauthenticatedException(ErrorCode.MALFORMED_TOKEN);
        } catch (SignatureException | IllegalArgumentException e) {
            throw new UnauthenticatedException(ErrorCode.INVALID_TOKEN);
        }
    }
}
