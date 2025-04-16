package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import com.yourssu.roomescape.util.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.method.support.*;
import org.springframework.web.context.request.*;

import static com.yourssu.roomescape.util.CookieConstants.TOKEN;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (TOKEN.equals(cookie.getName())) {
                String email = jwtTokenProvider.getEmail(cookie.getValue());
                Member member = memberDao.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
                return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
            }
        }

        return null;
    }
}