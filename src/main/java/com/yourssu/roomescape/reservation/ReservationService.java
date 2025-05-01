    package com.yourssu.roomescape.reservation;

    import com.yourssu.roomescape.exception.CustomException;
    import com.yourssu.roomescape.exception.ErrorCode;
    import com.yourssu.roomescape.member.Member;
    import com.yourssu.roomescape.member.MemberRepository;
    import com.yourssu.roomescape.reservation.dto.ReservationFindAllForAdminResponse;
    import com.yourssu.roomescape.reservation.dto.ReservationFindAllResponse;
    import com.yourssu.roomescape.reservation.dto.ReservationSaveRequest;
    import com.yourssu.roomescape.reservation.dto.ReservationSaveResponse;
    import com.yourssu.roomescape.reservation.dto.ReservationWaitingWithRank;
    import com.yourssu.roomescape.theme.Theme;
    import com.yourssu.roomescape.theme.ThemeRepository;
    import com.yourssu.roomescape.time.Time;
    import com.yourssu.roomescape.time.TimeRepository;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.List;
    import java.util.stream.Collectors;
    import java.util.stream.Stream;

    @Service
    @Transactional(readOnly = true)
    public class ReservationService {
        private final ReservationRepository reservationRepository;
        private final MemberRepository memberRepository;
        private final TimeRepository timeRepository;
        private final ThemeRepository themeRepository;

        public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
            this.reservationRepository = reservationRepository;
            this.memberRepository = memberRepository;
            this.timeRepository = timeRepository;
            this.themeRepository = themeRepository;
        }

        @Transactional
        public ReservationSaveResponse save(ReservationSaveRequest reservationSaveRequest, Member member) {

            Member existingMember;

            if (reservationSaveRequest.name() != null) {
                existingMember = memberRepository.findByName(reservationSaveRequest.name())
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            } else {
                existingMember = member;
            }

            Time time = timeRepository.findById(reservationSaveRequest.time())
                    .orElseThrow(() -> new CustomException(ErrorCode.TIME_NOT_FOUND));
            Theme theme = themeRepository.findById(reservationSaveRequest.theme())
                    .orElseThrow(() -> new CustomException(ErrorCode.THEME_NOT_FOUND));

            Reservation reservation = new Reservation(
                    existingMember,
                    reservationSaveRequest.date(),
                    time,
                    theme,
                    reservationSaveRequest.status()
            );

            Reservation newReservation = reservationRepository.save(reservation);

            Long waitingRank = reservationRepository.findWaitingRank(
                    newReservation.getTheme(),
                    newReservation.getDate(),
                    newReservation.getTime(),
                    newReservation.getId()
            );

            return ReservationSaveResponse.of(newReservation, waitingRank);
        }

        @Transactional
        public void deleteById(Long id, Member member) {

            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

            if(reservation.getMember() != member && !member.getRole().equals("ADMIN")) {
                throw new CustomException(ErrorCode.NO_PERMISSION_FOR_RESERVATION);
            }

            reservationRepository.deleteById(id);
        }

        public List<ReservationFindAllResponse> getMyReservations(Member member) {
            List<Reservation> reservedReservations = reservationRepository.findByMemberAndStatus(member, ReservationStatus.RESERVED);
            List<ReservationWaitingWithRank> waitingsWithRank = reservationRepository.findWaitingsWithRankByMember(member);

            return Stream.concat(
                            reservedReservations.stream().map(ReservationFindAllResponse::fromReservation),
                            waitingsWithRank.stream().map(ReservationFindAllResponse::fromWaitingWithRank)
                    )
                    .toList();
        }

        public List<ReservationFindAllForAdminResponse> getAllReservations(Member member) {
            if(!member.getRole().equals("ADMIN")) {
                throw new CustomException(ErrorCode.NOT_ADMIN);
            }

            List<Reservation> reservedReservations = reservationRepository.findByStatus(ReservationStatus.RESERVED);

            return reservedReservations.stream()
                    .map(ReservationFindAllForAdminResponse::from)
                    .collect(Collectors.toList());
        }
    }
