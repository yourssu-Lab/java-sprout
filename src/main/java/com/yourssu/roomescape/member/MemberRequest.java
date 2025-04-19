//package com.yourssu.roomescape.member;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//// 클래스
//public class MemberRequest {
//    private String name;
//    private String email;
//    private String password;
//
//    // 생성자
//    public MemberRequest(){}
//
//    // 메서드
//    public String getName() {
//        return name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//}

package com.yourssu.roomescape.member;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MemberRequest {
    private final String name;
    private final String email;
    private final String password;

    @JsonCreator
    public MemberRequest(
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

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
