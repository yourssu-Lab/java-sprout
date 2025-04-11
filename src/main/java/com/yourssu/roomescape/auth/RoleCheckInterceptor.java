package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

// todo: naming change

@Component
public class RoleCheckInterceptor implements HandlerInterceptor {
    private final MemberDao memberDao;
    private final LoginService loginService;

    public RoleCheckInterceptor(LoginService loginService, MemberDao memberDao) {
        this.loginService = loginService;
        this.memberDao = memberDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getCookies() == null) {
            response.setStatus(400);
            return false;
        }

        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .map(cookie -> cookie.getValue())
                .findFirst().orElse("");

        if (token.isBlank()) {
            response.setStatus(400);
            return false;
        }

        String name = loginService.checkLogin(token);
        Member member = memberDao.findByName(name);
        System.out.println("member : " + member.getRole());
        System.out.println("member : " + name);
        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
