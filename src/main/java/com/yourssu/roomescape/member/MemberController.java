package com.yourssu.roomescape.member;

import com.yourssu.roomescape.AppConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse){
        TokenDto tokenDTO = memberService.login(loginRequest); // TODO: 여기 변수명과 반환값 이름에 대해서 생각해보기

        Cookie cookie = new Cookie(AppConstants.TOKEN_COOKIE_NAME, tokenDTO.getToken()); // TODO: constant 프로퍼티로 관리
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        // TODO: 빈문자열 관련 예외처리
        String token = "";

        // TODO: stream 사용해보기
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(AppConstants.TOKEN_COOKIE_NAME)) {
                token = cookie.getValue();
            }
        }
        String name = memberService.loginCheck(token);
        return ResponseEntity.ok().body(new LoginCheckResponse(name));
    }
        @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(AppConstants.TOKEN_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
