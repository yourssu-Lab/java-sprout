package com.yourssu.roomescape.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "roomescape.auth.jwt")
public class TokenProperty {
    private final String secret;

    public TokenProperty(String secret){
        this.secret = secret;
    }

    public String getSecretKey(){
        return secret;
    }
}