package com.yourssu.roomescape;

import com.yourssu.roomescape.security.LoginMemberArgumentResolver;
import com.yourssu.roomescape.security.RoleCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final RoleCheckInterceptor roleCheckInterceptor;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver, RoleCheckInterceptor roleCheckInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.roleCheckInterceptor = roleCheckInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleCheckInterceptor)
                .addPathPatterns("/admin/**");
    }
}
