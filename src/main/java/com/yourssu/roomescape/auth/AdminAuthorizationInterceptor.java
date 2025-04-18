package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.jwt.TokenExtractor;
import com.yourssu.roomescape.member.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AdminAuthorizationInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = TokenExtractor.extractTokenFromCookies(cookies);
        Member member = authService.checkLogin(token);

        if(!"ADMIN".equals(member.getRole())) {
            response.setStatus(401);
            response.getWriter().write("해당 접근에 대한 권한이 없습니다.");
            return false;
        }

        return true;
    }
}
