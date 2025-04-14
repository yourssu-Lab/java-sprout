package com.yourssu.roomescape.member;

public class LoginMemberInfo {

    private Long id;
    private String name;
    private String email;
    private String role;

    public LoginMemberInfo(Long id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Getter만 필요하면 이 정도면 충분해
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
