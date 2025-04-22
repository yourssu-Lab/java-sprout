package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.reservation.dto.ReservationResponse;
import com.yourssu.roomescape.security.LoginMember;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationRequest reservationRequest, @LoginMember Member member) {
        if (reservationRequest.getDate() == null
            || reservationRequest.getTheme() == null
            || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        ReservationResponse reservation = reservationService.save(reservationRequest, member);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<ReservationResponse>> getAdminReservations(@LoginMember Member member) {
        return ResponseEntity.ok().body(reservationService.findReservation(member));
    }
//    @GetMapping("/reservations-mine")
//    public ResponseEntity<List<AdminReservationResponse>> getAdminReservations(@RoleCheck(Role.ADMIN) Member member) {
//        return ResponseEntity.ok().body(reservationService.findReservationsByAdmin(member));
//    }
//
//    @GetMapping("/reservations-mine")
//    public ResponseEntity<List<UserReservationResponse>> getUserReservations(@RoleCheck(Role.USER) Member member) {
//        return ResponseEntity.ok().body(reservationService.findReservationsByUser(member));
//    }
}
