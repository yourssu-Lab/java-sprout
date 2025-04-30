package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.common.AppConstants;
import com.yourssu.roomescape.common.security.JwtTokenProvider;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AdminAuthInterceptor(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication required");
            return false;
        }

        String token = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName() != null && AppConstants.TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }

        if (token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is missing");
            return false;
        }

        try {
            String email = jwtTokenProvider.getPayload(token);
            Member member = memberService.findByEmail(email);

            if (member == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("User not found");
                return false;
            }

            if (member.getRole() == null || !AppConstants.ROLE_ADMIN.equals(member.getRole())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 상태 코드 사용
                response.getWriter().write("Admin privileges required");
                return false;
            }
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token: " + e.getMessage());
            return false;
        }
    }}
