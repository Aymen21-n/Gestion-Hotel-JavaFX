package com.hotel.repository;

import com.hotel.domain.Reservation;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class ReservationRepository extends BaseRepository<Reservation, Long> {
    public ReservationRepository(Session session) {
        super(session, Reservation.class);
    }

    public boolean hasDateConflict(Long chambreNumero, LocalDate debut, LocalDate fin, Long excludeReservationId) {
        String baseQuery = "select count(r) from Reservation r where r.chambre.numero = :numero " +
                "and r.dateFin > :debut and r.dateDebut < :fin " +
                "and r.statut <> com.hotel.domain.ReservationStatut.ANNULEE";
        if (excludeReservationId != null) {
            baseQuery += " and r.idReservation <> :excludeId";
        }
        var query = session.createQuery(baseQuery, Long.class)
                .setParameter("numero", chambreNumero)
                .setParameter("debut", debut)
                .setParameter("fin", fin);
        if (excludeReservationId != null) {
            query.setParameter("excludeId", excludeReservationId);
        }
        Long count = query.getSingleResult();
        return count != null && count > 0;
    }

    public List<Reservation> findAllWithDetails() {
        return session.createQuery(
                        "select distinct r from Reservation r " +
                                "left join fetch r.client " +
                                "left join fetch r.chambre " +
                                "left join fetch r.services",
                        Reservation.class)
                .list();
    }

    public List<Reservation> findConfirmableForFacture() {
        return session.createQuery(
                        "select distinct r from Reservation r " +
                                "left join fetch r.client " +
                                "left join fetch r.facture " +
                                "where r.statut = com.hotel.domain.ReservationStatut.CONFIRMEE " +
                                "and r.facture is null",
                        Reservation.class)
                .list();
    }
}
