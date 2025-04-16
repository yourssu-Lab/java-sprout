package com.yourssu.roomescape.member;

import com.yourssu.roomescape.auth.LoginCheckResponse;
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

//    @PostMapping
//    public ResponseEntity<MemberResponse> create(@RequestBody MemberRequest request) {
//        MemberResponse response = memberService.createMember(request);
//        return ResponseEntity.created(URI.create("/members/" + response.getId())).body(response);
//    }

}
