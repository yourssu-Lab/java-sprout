package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberRepository memberRepository;

    public ReservationController(ReservationService reservationService, MemberRepository memberRepository) {
        this.reservationService = reservationService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> myReservations(LoginMember loginMember) {
        if (loginMember == null) {
            return List.of(); // 로그인하지 않은 경우 빈 목록 반환
        }

        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return reservationService.findMyReservations(member);
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.getDate() == null
                || reservationRequest.getTheme() == null
                || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (reservationRequest.getName() == null && loginMember == null) {
            return ResponseEntity.badRequest().build();
        }

        // 비즈니스 로직을 서비스 계층에 위임
        ReservationResponse reservation = reservationService.save(reservationRequest, loginMember);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
