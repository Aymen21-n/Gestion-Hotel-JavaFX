package com.hotel.service;

import com.hotel.config.HibernateUtil;
import com.hotel.domain.Facture;
import com.hotel.domain.Reservation;
import com.hotel.repository.FactureRepository;
import com.hotel.repository.ReservationRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class FactureService {
    public List<Facture> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new FactureRepository(session).findAll();
        }
    }

    public Facture generateFacture(Long reservationId, String modePaiement) {
        ValidationUtils.requireNotBlank(modePaiement, "Le mode de paiement est obligatoire.");
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ReservationRepository reservationRepository = new ReservationRepository(session);
            Reservation reservation = reservationRepository.findById(reservationId);
            if (reservation == null) {
                throw new IllegalArgumentException("Réservation introuvable.");
            }
            if (reservation.getFacture() != null) {
                return reservation.getFacture();
            }
            Facture facture = Facture.genererFacture(reservation, modePaiement);
            new FactureRepository(session).save(facture);
            transaction.commit();
            return facture;
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw exception;
        }
    }
}
