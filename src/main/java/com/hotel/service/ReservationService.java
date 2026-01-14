package com.hotel.service;

import com.hotel.config.HibernateUtil;
import com.hotel.domain.Chambre;
import com.hotel.domain.Client;
import com.hotel.domain.Reservation;
import com.hotel.domain.ReservationStatut;
import com.hotel.repository.FactureRepository;
import com.hotel.repository.ChambreRepository;
import com.hotel.repository.ClientRepository;
import com.hotel.repository.ReservationRepository;
import com.hotel.repository.ServiceRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    public List<Reservation> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new ReservationRepository(session).findAllWithDetails();
        }
    }

    public List<Reservation> findConfirmableForFacture() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new ReservationRepository(session).findConfirmableForFacture();
        }
    }

    public List<Chambre> listChambresDisponibles(LocalDate dateDebut, LocalDate dateFin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ReservationRepository reservationRepository = new ReservationRepository(session);
            List<Chambre> chambres = new ChambreRepository(session).findAll();
            return chambres.stream()
                    .filter(chambre -> !reservationRepository.hasDateConflict(chambre.getNumero().longValue(), dateDebut, dateFin))
                    .toList();
        }
    }

    public Reservation createReservation(String clientCin, Integer chambreNumero, LocalDate dateDebut,
                                         LocalDate dateFin, String modePaiement, List<Long> serviceIds) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Client client = new ClientRepository(session).findById(clientCin);
            if (client == null) {
                throw new IllegalArgumentException("Client introuvable.");
            }
            Chambre chambre = new ChambreRepository(session).findById(chambreNumero);
            if (chambre == null) {
                throw new IllegalArgumentException("Chambre introuvable.");
            }

            Reservation reservation = new Reservation(dateDebut, dateFin, null, ReservationStatut.EN_COURS);
            reservation.setClient(client);
            reservation.setChambre(chambre);
            reservation.setModePaiement(modePaiement);
            reservation.setServices(loadServices(session, serviceIds));
            validateReservation(reservation, session);

            new ReservationRepository(session).save(reservation);
            chambre.setEstReserve(true);
            new ChambreRepository(session).update(chambre);

            transaction.commit();
            return reservation;
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    public void cancelReservation(Long reservationId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ReservationRepository repository = new ReservationRepository(session);
            Reservation reservation = repository.findById(reservationId);
            if (reservation == null) {
                throw new IllegalArgumentException("Réservation introuvable.");
            }
            reservation.setStatut(ReservationStatut.ANNULEE);
            repository.update(reservation);
            Chambre chambre = reservation.getChambre();
            if (chambre != null) {
                chambre.setEstReserve(false);
                new ChambreRepository(session).update(chambre);
            }
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    public void validateReservation(Long reservationId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ReservationRepository repository = new ReservationRepository(session);
            Reservation reservation = repository.findById(reservationId);
            if (reservation == null) {
                throw new IllegalArgumentException("Réservation introuvable.");
            }
            if (reservation.getStatut() == ReservationStatut.CONFIRMEE) {
                throw new IllegalArgumentException("La réservation est déjà confirmée.");
            }
            if (reservation.getStatut() == ReservationStatut.ANNULEE) {
                throw new IllegalArgumentException("La réservation est annulée.");
            }
            validateReservation(reservation, session);
            reservation.setStatut(ReservationStatut.CONFIRMEE);
            repository.update(reservation);

            FactureService factureService = new FactureService();
            if (new FactureRepository(session).findByReservationId(reservationId) == null) {
                factureService.generateForReservation(session, reservationId, reservation.getModePaiement());
            }
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    public void validateReservation(Reservation reservation, Session session) {
        if (reservation.getDateDebut() == null || reservation.getDateFin() == null) {
            throw new IllegalArgumentException("Les dates de réservation sont obligatoires.");
        }
        if (!reservation.getDateFin().isAfter(reservation.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début.");
        }
        if (reservation.getChambre() == null) {
            throw new IllegalArgumentException("Une chambre doit être sélectionnée.");
        }
        ValidationUtils.requireNotBlank(reservation.getModePaiement(), "Le mode de paiement est obligatoire.");

        ReservationRepository repository = new ReservationRepository(session);
        if (repository.hasDateConflict(reservation.getChambre().getNumero().longValue(),
                reservation.getDateDebut(), reservation.getDateFin())) {
            throw new IllegalArgumentException("La chambre est déjà réservée pour cette période.");
        }
    }

    private List<com.hotel.domain.Service> loadServices(Session session, List<Long> serviceIds) {
        if (serviceIds == null || serviceIds.isEmpty()) {
            return new ArrayList<>();
        }
        ServiceRepository repository = new ServiceRepository(session);
        List<com.hotel.domain.Service> services = new ArrayList<>();
        for (Long id : serviceIds) {
            com.hotel.domain.Service service = repository.findById(id);
            if (service != null) {
                services.add(service);
            }
        }
        return services;
    }
}
