package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.member.LoginRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 로그인 API
     * - POST /login
     * - 요청 바디에 email, password 포함
     * - 로그인 성공 시 토큰을 쿠키로 발급 (HttpOnly)
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request,
                                               HttpServletResponse response) {

        AuthResponse authResponse = authService.login(request);

        // ✅ JWT 토큰을 HttpOnly 쿠키로 세팅
        Cookie cookie = new Cookie("token", authResponse.getToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(authResponse);
    }

    /**
     * 로그인 상태 확인 API
     * - GET /login/check
     * - 쿠키에서 token 꺼내서 사용자 이름 반환
     */
    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(HttpServletRequest request) {
        String token = extractTokenFromCookie(request.getCookies());
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).build(); // 인증 실패
        }

        String name = authService.loginCheck(token);
        return ResponseEntity.ok().body(new LoginCheckResponse(name));
    }

    /**
     * token 쿠키 추출 유틸
     */
    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
