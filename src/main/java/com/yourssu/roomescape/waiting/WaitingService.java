package com.yourssu.roomescape.waiting;

import com.yourssu.roomescape.common.exception.ResourceNotFoundException;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.reservation.ReservationRepository;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public WaitingService(WaitingRepository waitingRepository,
                          ReservationRepository reservationRepository,
                          MemberRepository memberRepository,
                          ThemeRepository themeRepository,
                          TimeRepository timeRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    private WaitingResponse addWaiting(String date, Long themeId, Long timeId, Member member) {
        boolean alreadyReserved = reservationRepository.existsByMemberIdAndDateAndThemeIdAndTimeId(
                member.getId(), date, themeId, timeId);

        if (alreadyReserved) {
            throw new IllegalStateException("이미 예약한 시간입니다.");
        }

        boolean alreadyWaiting = waitingRepository.existsByMember_IdAndDateAndTheme_IdAndTime_Id(
                member.getId(), date, themeId, timeId);

        if (alreadyWaiting) {
            throw new IllegalStateException("이미 대기 중인 시간입니다.");
        }

        Time time = timeRepository.findById(timeId)
                .orElseThrow(() -> new ResourceNotFoundException("Time not found"));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        Waiting waiting = new Waiting(date, time, theme, member);
        Waiting savedWaiting = waitingRepository.save(waiting);

        Long waitingNumber = waitingRepository.findByDateAndThemeIdAndTimeId(date, themeId, timeId)
                .stream()
                .filter(w -> w.getId() <= savedWaiting.getId())
                .count();

        return new WaitingResponse(
                savedWaiting.getId(),
                theme.getName(),
                date,
                time.getValue(),
                waitingNumber
        );
    }

    @Transactional
    public void deleteWaiting(Long id) {
        waitingRepository.deleteById(id);
    }

    @Transactional
    public WaitingResponse addWaitingByMemberId(String date, Long themeId, Long timeId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        return addWaiting(date, themeId, timeId, member);
    }

}
