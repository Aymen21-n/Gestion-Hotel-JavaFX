package com.hotel.repository;

import com.hotel.domain.Reservation;
import org.hibernate.Session;

import java.time.LocalDate;

public class ReservationRepository extends BaseRepository<Reservation, Long> {
    public ReservationRepository(Session session) {
        super(session, Reservation.class);
    }

    public boolean hasDateConflict(Long chambreNumero, LocalDate debut, LocalDate fin) {
        Long count = session.createQuery(
                        "select count(r) from Reservation r where r.chambre.numero = :numero " +
                                "and r.dateFin > :debut and r.dateDebut < :fin", Long.class)
                .setParameter("numero", chambreNumero)
                .setParameter("debut", debut)
                .setParameter("fin", fin)
                .getSingleResult();
        return count != null && count > 0;
    }
}
