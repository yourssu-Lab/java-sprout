package com.yourssu.roomescape.member;

import com.yourssu.roomescape.utils.TokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MemberRoleHandlerInterceptor implements HandlerInterceptor {

    private MemberService memberService;
    private final TokenUtil tokenUtil = new TokenUtil();

    public MemberRoleHandlerInterceptor(MemberService memberService){
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();

        String token = tokenUtil.extractTokenFromCookie(cookies);
        MemberResponse member = memberService.checkLogin(token);

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
