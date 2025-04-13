package com.yourssu.roomescape.member;

import com.yourssu.roomescape.auth.LoginCheckResponse;
import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.auth.LoginRequest;
import com.yourssu.roomescape.auth.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class MemberController {
    private LoginService loginService;
    private MemberService memberService;

    public MemberController(LoginService loginService, MemberService memberService) {
        this.loginService = loginService;
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", loginService.login(loginRequest));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(@LoginMember Member member) {
        return ResponseEntity.ok().body(new LoginCheckResponse(member.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
