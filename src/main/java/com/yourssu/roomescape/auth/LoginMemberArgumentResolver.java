package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.infrastructure.CookieProvider;
import com.yourssu.roomescape.infrastructure.JwtTokenProvider;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import com.yourssu.roomescape.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public LoginMemberArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        String token = CookieProvider.findCookieByKey(cookies, "token");
        if (token.isBlank()) {
            return null;
        }

        String memberEmail = jwtTokenProvider.getPayload(token);
        return memberDao.findByEmail(memberEmail);
    }
}
