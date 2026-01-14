package com.hotel.repository;

import com.hotel.domain.Facture;
import org.hibernate.Session;

import java.util.List;

public class FactureRepository extends BaseRepository<Facture, Long> {
    public FactureRepository(Session session) {
        super(session, Facture.class);
    }

    public Facture findByReservationId(Long reservationId) {
        return session.createQuery(
                        "select f from Facture f join fetch f.reservation r left join fetch r.client where r.idReservation = :id",
                        Facture.class)
                .setParameter("id", reservationId)
                .uniqueResult();
    }

    public List<Facture> findAllWithReservation() {
        return session.createQuery(
                        "select f from Facture f join fetch f.reservation r left join fetch r.client",
                        Facture.class)
                .list();
    }
}
