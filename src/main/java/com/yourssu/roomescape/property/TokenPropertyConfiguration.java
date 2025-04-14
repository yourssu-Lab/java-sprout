package com.yourssu.roomescape.property;

import org.apache.el.parser.Token;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({TokenProperty.class})
public class TokenPropertyConfiguration {
}
