package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.auth.UserInfo;
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
    public List<MyReservationResponse> myReservations(@LoginMember UserInfo userInfo) {
        if (userInfo == null) {
            return List.of();
        }
        return reservationService.findMyReservationsByMemberId(userInfo.getId());
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@Validated @RequestBody ReservationRequest reservationRequest, @LoginMember UserInfo userInfo) {
        if (reservationRequest.name() == null && userInfo == null) {
            return ResponseEntity.badRequest().build();
        }

        ReservationResponse reservation = reservationService.save(reservationRequest, userInfo);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.id())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
