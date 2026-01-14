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
            return new FactureRepository(session).findAllWithReservation();
        }
    }

    public Facture generateForReservation(Long reservationId, String modePaiement) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Facture facture = generateForReservation(session, reservationId, modePaiement);
            transaction.commit();
            return facture;
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    public Facture generateForReservation(Session session, Long reservationId, String modePaiement) {
        String finalMode = (modePaiement == null || modePaiement.isBlank()) ? "ESPECES" : modePaiement;
        FactureRepository factureRepository = new FactureRepository(session);
        Facture existing = factureRepository.findByReservationId(reservationId);
        if (existing != null) {
            return existing;
        }
        Reservation reservation = new ReservationRepository(session).findById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Réservation introuvable.");
        }
        if (reservation.getStatut() != com.hotel.domain.ReservationStatut.CONFIRMEE) {
            throw new IllegalArgumentException("La facture ne peut être générée que pour une réservation confirmée.");
        }
        Facture facture = Facture.genererFacture(reservation, finalMode);
        factureRepository.save(facture);
        return facture;
    }
}
