package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.auth.dto.CheckLoginResponse;
import com.yourssu.roomescape.auth.dto.LoginRequest;
import com.yourssu.roomescape.jwt.TokenExtractor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        String accessToken = authService.login(loginRequest);
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckLoginResponse> loginCheck(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String token = TokenExtractor.extractTokenFromCookies(cookies);

        String name = authService.checkLogin(token).getName();
        return ResponseEntity.ok().body(new CheckLoginResponse(name));
    }
}
