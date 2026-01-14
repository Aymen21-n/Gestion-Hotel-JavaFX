package com.hotel.service;

import com.hotel.config.HibernateUtil;
import com.hotel.domain.Service;
import com.hotel.repository.ServiceRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ServiceHotelierService {
    public List<Service> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new ServiceRepository(session).findAll();
        }
    }

    public Service findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new ServiceRepository(session).findById(id);
        }
    }

    public void create(Service service) {
        validate(service);
        executeInTransaction(session -> new ServiceRepository(session).save(service));
    }

    public void update(Service service) {
        validate(service);
        executeInTransaction(session -> new ServiceRepository(session).update(service));
    }

    public void delete(Service service) {
        executeInTransaction(session -> new ServiceRepository(session).delete(service));
    }

    private void validate(Service service) {
        ValidationUtils.requireNotBlank(service.getNomService(), "Le nom du service est obligatoire.");
        ValidationUtils.requireNotBlank(service.getHoraireOuverture(), "L'horaire d'ouverture est obligatoire.");
        ValidationUtils.requireNotBlank(service.getHoraireFermeture(), "L'horaire de fermeture est obligatoire.");
        ValidationUtils.requirePositive(service.getPrix(), "Le prix du service doit être supérieur à 0.");
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
