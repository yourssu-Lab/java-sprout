package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> myReservations(@LoginMember Member member) {
        if (member == null) {
            return List.of(); // 로그인하지 않은 경우 빈 목록 반환
        }
        return reservationService.findMyReservationsByMemberId(member.getId());
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@Validated @RequestBody ReservationRequest reservationRequest, @LoginMember Member loginMember) {
        if (reservationRequest.name() == null && loginMember == null) {
            return ResponseEntity.badRequest().build();
        }

        ReservationResponse reservation = reservationService.save(reservationRequest, loginMember);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.id())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
