package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.util.CookieConstants;
import com.yourssu.roomescape.util.ValidationUtils;
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

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request);

        Cookie cookie = new Cookie(CookieConstants.TOKEN, authResponse.getToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(HttpServletRequest request) {
        String token = ValidationUtils.extractTokenFromCookies(request.getCookies());
        if (token == null || token.isEmpty()) return ResponseEntity.status(401).build();

        String name = authService.getNameFromToken(token);
        return ResponseEntity.ok(new LoginCheckResponse(name));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(CookieConstants.TOKEN, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}