package com.yourssu.roomescape.waiting;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.reservation.ReservationSaveRequest;
import com.yourssu.roomescape.reservation.ReservationSaveResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class WaitingController {

    private final WaitingService waitingService;


    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingSaveResponse> create(@RequestBody WaitingSaveRequest request, @LoginMember Member member) {
        if (request.date() == null || request.themeId() == null || request.timeId() == null) {
            throw new CustomException(ErrorCode.INVALID_RESERVATION_REQUEST);
        }

        WaitingSaveResponse response = waitingService.save(request, member);

        return ResponseEntity.created(URI.create("/waitings/" + response.waitingId())).body(response);
    }

}
