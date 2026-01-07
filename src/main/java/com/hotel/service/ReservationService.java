package com.hotel.service;

import com.hotel.domain.Chambre;
import com.hotel.domain.Reservation;
import com.hotel.repository.ReservationRepository;

import java.time.LocalDate;

public class ReservationService {
    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public void validateReservation(Reservation reservation) {
        if (reservation.getDateDebut() == null || reservation.getDateFin() == null) {
            throw new IllegalArgumentException("Les dates de réservation sont obligatoires.");
        }
        if (!reservation.getDateFin().isAfter(reservation.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début.");
        }
        if (reservation.getChambre() == null) {
            throw new IllegalArgumentException("Une chambre doit être sélectionnée.");
        }

        Chambre chambre = reservation.getChambre();
        if (!isChambreDisponible(chambre.getNumero().longValue(), reservation.getDateDebut(), reservation.getDateFin())) {
            throw new IllegalArgumentException("La chambre est déjà réservée pour cette période.");
        }
    }

    public boolean isChambreDisponible(Long chambreNumero, LocalDate dateDebut, LocalDate dateFin) {
        return !repository.hasDateConflict(chambreNumero, dateDebut, dateFin);
    }
}
