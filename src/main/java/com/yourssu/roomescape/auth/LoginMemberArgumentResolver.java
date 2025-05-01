package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.exception.MemberNotFoundException;
import com.yourssu.roomescape.exception.UnauthenticatedException;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.util.CookieUtil;
import com.yourssu.roomescape.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMemberAnnotation.class) &&
                parameter.getParameterType().equals(com.yourssu.roomescape.auth.LoginMember.class);
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = CookieUtil.extractTokenFromCookies(request.getCookies());

        if (token == null || token.isBlank()) {
            throw new UnauthenticatedException("토큰이 존재하지 않거나 비어 있습니다.");
        }

        String email = jwtTokenProvider.getEmail(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("이메일로 회원을 찾을 수 없습니다: " + email));

        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
