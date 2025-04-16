package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import com.yourssu.roomescape.util.CookieUtil;
import com.yourssu.roomescape.util.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

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

        String token = CookieUtil.extractTokenFromCookies(request.getCookies());
        if (token == null || token.isBlank()) return null;

        String email = jwtTokenProvider.getEmail(token);
        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
