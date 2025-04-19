package com.yourssu.roomescape.member;

import com.yourssu.roomescape.reservation.Reservation;
import com.yourssu.roomescape.reservation.ReservationRequest;
import com.yourssu.roomescape.reservation.ReservationResponse;
import com.yourssu.roomescape.reservation.ReservationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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
    private HttpServletResponse response;
    private TokenService tokenService;
    private ReservationService reservationService;

    public MemberController(MemberService memberService, TokenService tokenService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    // 토큰 생성 후 쿠키 생성해서 반환하는 부분
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberRequest memberRequest, HttpServletResponse response) {
        String email = memberRequest.getEmail();
        String password = memberRequest.getPassword();
        MemberResponse member = new MemberResponse(memberService.findByEmailAndPassword(email, password));
        // 토큰 생성부분
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        String accessToken = Jwts.builder().setSubject(member.getId().toString()).claim("email", member.getEmail()).claim("password", member.getPassword()).signWith(Keys.hmacShaKeyFor(secretKey.getBytes())).compact();

        // 쿠키 생성부분
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("로그인 성공");
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request){
        try {
            Long memberId = tokenService.getMemberIdFromRequest(request);
            Member member = memberService.findByMemberId(memberId);

            MemberResponse memberResponse = new MemberResponse(
                    member.getId(),
                    member.getName(),
                    member.getEmail(),
                    member.getRole()
            );
            return ResponseEntity.ok(memberResponse);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

//    @PostMapping("/reservations")
//    public ResponseEntity<?> create(@RequestBody ReservationRequest request, @LoginMember LoginMemberInfo loginMember){
//        Member member;
//        if(request.getName() != null && !request.getName().isBlank()){
//            member = memberService.findByName(request.getName());
//        } else {
//            member = memberService.findByMemberId(loginMember.getId());
//        }
//
//        ReservationResponse reservation = reservationService.save(request);
//        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId()))
//                .body(reservation);
//    }
