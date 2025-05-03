package com.yourssu.roomescape.member.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank
    private String password;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
