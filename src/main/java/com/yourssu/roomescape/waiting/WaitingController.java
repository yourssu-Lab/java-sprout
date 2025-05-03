package com.yourssu.roomescape.waiting;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.auth.UserInfo;
import com.yourssu.roomescape.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> createWaiting(
            @RequestBody WaitingRequest request,
            @LoginMember UserInfo userInfo
    ) {
        if (userInfo == null) {
            return ResponseEntity.badRequest().build();
        }

        WaitingResponse waiting = waitingService.addWaitingByMemberId(
                request.getDate(),
                request.getTheme(),
                request.getTime(),
                userInfo.getId()
        );

        return ResponseEntity.created(URI.create("/waitings/" + waiting.getId()))
                .body(waiting);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> deleteWaiting(@PathVariable Long id) {
        waitingService.deleteWaiting(id);
        return ResponseEntity.noContent().build();
    }
}
