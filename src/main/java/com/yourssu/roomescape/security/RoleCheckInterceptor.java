package com.yourssu.roomescape.security;

import com.yourssu.roomescape.auth.LoginService;
import com.yourssu.roomescape.enums.Cookie;
import com.yourssu.roomescape.enums.Role;
import com.yourssu.roomescape.infrastructure.CookieProvider;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
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
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        String token = CookieProvider.findCookieByKey(cookies, Cookie.TOKEN_NAME.getValue());

        String name = loginService.checkLogin(token);
        Member member = memberRepository.findByName(name);
        if (member == null || !member.getRole().equals(Role.ADMIN.getValue())) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
