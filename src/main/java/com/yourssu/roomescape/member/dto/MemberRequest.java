package com.yourssu.roomescape.member.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
        @NotBlank
        String name,

        @NotBlank
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @NotBlank
        String password
) {}