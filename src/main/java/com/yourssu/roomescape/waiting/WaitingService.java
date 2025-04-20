package com.yourssu.roomescape.waiting;

import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingService(WaitingRepository waitingRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public WaitingSaveResponse save(WaitingSaveRequest request, Member member) {

        Time time = timeRepository.findById(request.timeId())
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_NOT_FOUND));

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new CustomException(ErrorCode.THEME_NOT_FOUND));

        Waiting newWaiting = new Waiting(
                member,
                request.date(),
                time,
                theme,
                LocalDateTime.now()
        );

        Waiting savedWaiting = waitingRepository.save(newWaiting);

        return new WaitingSaveResponse(savedWaiting.getId());
    }
}
