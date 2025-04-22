package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.LoginMember;
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
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, LoginMember member) {
        if (reservationRequest.getDate() == null
                || reservationRequest.getTheme() == null
                || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }
            ReservationResponse reservation = reservationService.save(reservationRequest, member);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);

    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<MineReservationResponse>> reservationMine(LoginMember loginMember){

        return ResponseEntity.ok(reservationService.reservationMine(loginMember));
    }

    @PostMapping("/waitings")
    public ResponseEntity<ReservationResponse> wait(@RequestBody ReservationWaitingRequest reservationWaitingRequest, LoginMember member){
        ReservationResponse response = reservationService.waitReservation(member, reservationWaitingRequest);
        return ResponseEntity.created(URI.create("/waitings/" + response.getId())).body(response);
    }
}

