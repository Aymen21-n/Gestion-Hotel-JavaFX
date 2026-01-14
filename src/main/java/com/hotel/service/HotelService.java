package com.hotel.service;

import com.hotel.config.HibernateUtil;
import com.hotel.domain.Hotel;
import com.hotel.repository.HotelRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HotelService {
    public List<Hotel> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new HotelRepository(session).findAll();
        }
    }

    public Hotel findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new HotelRepository(session).findById(id);
        }
    }

    public void create(Hotel hotel) {
        validate(hotel);
        executeInTransaction(session -> new HotelRepository(session).save(hotel));
    }

    public void update(Hotel hotel) {
        validate(hotel);
        executeInTransaction(session -> new HotelRepository(session).update(hotel));
    }

    public void delete(Hotel hotel) {
        executeInTransaction(session -> new HotelRepository(session).delete(hotel));
    }

    private void validate(Hotel hotel) {
        ValidationUtils.requireNotBlank(hotel.getNom(), "Le nom de l'hôtel est obligatoire.");
        ValidationUtils.requireNotBlank(hotel.getAdresse(), "L'adresse est obligatoire.");
        ValidationUtils.requirePositive(hotel.getNote(), "La note doit être supérieure à 0.");
        ValidationUtils.requireNotBlank(hotel.getTelephone(), "Le téléphone est obligatoire.");
        if (hotel.getNbChambres() <= 0) {
            throw new IllegalArgumentException("Le nombre de chambres doit être supérieur à 0.");
        }
    }

    private void executeInTransaction(TransactionConsumer consumer) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            consumer.accept(session);
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    @FunctionalInterface
    private interface TransactionConsumer {
        void accept(Session session);
    }
}
