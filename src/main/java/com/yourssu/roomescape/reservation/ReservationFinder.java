package com.yourssu.roomescape.reservation;

import org.springframework.stereotype.Component;

import static com.yourssu.roomescape.exception.ExceptionMessage.NO_EXIST_RESERVATION;

@Component
public class ReservationFinder {

    private final ReservationRepository reservationRepository;

    public ReservationFinder(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation findReservation(Long reservationId){
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(NO_EXIST_RESERVATION.getMessage()));
    }
}
