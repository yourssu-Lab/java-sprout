package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.infrastructure.CookieProvider;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleCheckInterceptor implements HandlerInterceptor {
    private final MemberRepository memberRepository;
    private final LoginService loginService;

    public RoleCheckInterceptor(MemberRepository memberRepository, LoginService loginService) {
        this.memberRepository = memberRepository;
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = CookieProvider.findCookieByKey(cookies, "token");

        String name = loginService.checkLogin(token);
        Member member = memberRepository.findByName(name);
        if (member == null || !member.getRole().equals(Role.ADMIN)) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
