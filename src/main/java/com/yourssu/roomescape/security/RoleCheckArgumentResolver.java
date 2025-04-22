package com.yourssu.roomescape.security;

import com.yourssu.roomescape.infrastructure.CookieProvider;
import com.yourssu.roomescape.infrastructure.JwtTokenProvider;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.file.AccessDeniedException;

@Component
public class RoleCheckArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public RoleCheckArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RoleCheck.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = CookieProvider.findCookieByKey(request.getCookies(), "token");

        String email = jwtTokenProvider.getPayload(token);
        Member member = memberRepository.findByEmail(email);

        RoleCheck roleCheck = parameter.getParameterAnnotation(RoleCheck.class);
        if (!member.getRole().equals(roleCheck.value().getValue())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        return member;
    }
}
