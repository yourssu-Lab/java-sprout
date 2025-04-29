package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.auth.LoginMemberAnnotation;
import com.yourssu.roomescape.reservation.waiting.WaitingRequest;
import com.yourssu.roomescape.reservation.waiting.WaitingResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity create(@Valid @RequestBody ReservationRequest reservationRequest, @LoginMemberAnnotation LoginMember loginMember) {
        ReservationResponse reservation = reservationService.save(reservationRequest, loginMember);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.id())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<MyReservationResponse>> myReservations(@LoginMemberAnnotation LoginMember loginMember) {
        return ResponseEntity.ok(reservationService.findMyReservations(loginMember));
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> createWaiting(@Valid @RequestBody WaitingRequest request, @LoginMemberAnnotation LoginMember loginMember) {
        WaitingResponse waitingResponse = reservationService.createWaiting(request, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + waitingResponse.id())).body(waitingResponse);
    }
}
