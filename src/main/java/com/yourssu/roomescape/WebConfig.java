package com.yourssu.roomescape;

import com.yourssu.roomescape.member.AdminAuthInterceptor;
import com.yourssu.roomescape.member.LoginMemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final AdminAuthInterceptor adminAuthInterceptor;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver, AdminAuthInterceptor adminAuthInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.adminAuthInterceptor = adminAuthInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthInterceptor).addPathPatterns("/admin/**");
    }
}