package com.yourssu.roomescape.member;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
   private final TokenService tokenService;
   private final MemberService memberService;

    public LoginMemberArgumentResolver(TokenService tokenService, MemberService memberService) {
        this.tokenService = tokenService;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class)
                && parameter.getParameterType().equals(LoginMemberInfo.class);
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory){
        log.info("✅ LoginMemberArgumentResolver is called!");
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Long memberId = tokenService.getMemberIdFromRequest(request);
        Member member = memberService.findByMemberId(memberId);
       return new LoginMemberInfo(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
