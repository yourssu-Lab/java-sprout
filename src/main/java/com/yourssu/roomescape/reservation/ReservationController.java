package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.LoginMember;
import com.yourssu.roomescape.utils.TokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final TokenUtil tokenUtil;

    public ReservationController(ReservationService reservationService, TokenUtil tokenUtil) {
        this.reservationService = reservationService;
        this.tokenUtil = tokenUtil;
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

    @GetMapping("/reservation")
    public ResponseEntity<List<MineReservationResponse>> reservationMine(LoginMember loginMember){

        return ResponseEntity.ok(reservationService.reservationMine(loginMember));
    }
}
