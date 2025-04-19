package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.member.Member;
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

    @GetMapping("/reservations-mine")
    public List<ReservationFindAllResponse> list(@LoginMember Member member) {
        return reservationService.findAll(member);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationSaveResponse> create(@RequestBody ReservationSaveRequest reservationSaveRequest, @LoginMember Member member) {
        if (reservationSaveRequest.getDate() == null || reservationSaveRequest.getTheme() == null || reservationSaveRequest.getTime() == null) {
            throw new CustomException(ErrorCode.INVALID_RESERVATION_REQUEST);
        }

        ReservationSaveResponse reservation = reservationService.save(reservationSaveRequest, member);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
