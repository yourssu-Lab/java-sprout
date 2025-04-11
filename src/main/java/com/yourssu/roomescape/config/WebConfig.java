package com.yourssu.roomescape.config;

import com.yourssu.roomescape.member.LoginMemberArgumentResolver;
import com.yourssu.roomescape.member.MemberRoleHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private LoginMemberArgumentResolver loginMemberArgumentResolver;
    private MemberRoleHandlerInterceptor memberRoleHandlerInterceptor;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver, MemberRoleHandlerInterceptor memberRoleHandlerInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.memberRoleHandlerInterceptor = memberRoleHandlerInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(memberRoleHandlerInterceptor)
                .addPathPatterns("/admin/**");
    }
}
