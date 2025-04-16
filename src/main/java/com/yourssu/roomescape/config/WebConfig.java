package com.yourssu.roomescape.config;

import com.yourssu.roomescape.auth.LoginMemberArgumentResolver;
import com.yourssu.roomescape.member.MemberDao;
import com.yourssu.roomescape.util.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminInterceptor adminInterceptor;

    public WebConfig(MemberDao memberDao, JwtTokenProvider jwtTokenProvider, AdminInterceptor adminInterceptor) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberDao, jwtTokenProvider));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin");
    }
}
