package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.reservation.dto.ReservationFindAllForAdminResponse;
import com.yourssu.roomescape.reservation.dto.ReservationFindAllResponse;
import com.yourssu.roomescape.reservation.dto.ReservationSaveRequest;
import com.yourssu.roomescape.reservation.dto.ReservationSaveResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/mine")
    public List<ReservationFindAllResponse> getMyReservations(@LoginMember Member member) {
        return reservationService.getMyReservations(member);
    }

    @GetMapping("/admin")
    public List<ReservationFindAllForAdminResponse> getAllReservations(@LoginMember Member member) {
        return reservationService.getAllReservations(member);
    }

    @PostMapping
    public ResponseEntity<ReservationSaveResponse> create(@RequestBody @Valid ReservationSaveRequest reservationSaveRequest, @LoginMember Member member) {

        ReservationSaveResponse reservation = reservationService.save(reservationSaveRequest, member);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.id())).body(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @LoginMember Member member) {
        reservationService.deleteById(id, member);
        return ResponseEntity.noContent().build();
    }
}
