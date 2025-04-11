package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginCheckResponse;
import com.yourssu.roomescape.auth.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final LoginService loginService;

    public ReservationController(ReservationService reservationService, LoginService loginService) {
        this.reservationService = reservationService;
        this.loginService = loginService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    // why handlerMethodArgumentResolver not invoked?
    // use HttpServletRequest to pass test only
    // todo: modify to resolver until sunday
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, HttpServletRequest request) {
        if (reservationRequest.getDate() == null
            || reservationRequest.getTheme() == null
            || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }
        else if (reservationRequest.getName() == null) {
            if (request.getCookies() == null) {
                return ResponseEntity.noContent().build();
            }

            String token = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("token"))
                    .map(cookie -> cookie.getValue())
                    .findFirst().orElse("");

            if (token.isBlank()) {
                return ResponseEntity.notFound().build();
            }

            String name = loginService.checkLogin(token);
            reservationRequest.setName(name);
        }
        ReservationResponse reservation = reservationService.save(reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
