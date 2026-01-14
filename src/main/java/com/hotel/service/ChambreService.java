package com.hotel.service;

import com.hotel.config.HibernateUtil;
import com.hotel.domain.Chambre;
import com.hotel.repository.ChambreRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ChambreService {
    public List<Chambre> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new ChambreRepository(session).findAll();
        }
    }

    public Chambre findById(Integer numero) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new ChambreRepository(session).findById(numero);
        }
    }

    public void create(Chambre chambre) {
        validate(chambre);
        executeInTransaction(session -> new ChambreRepository(session).save(chambre));
    }

    public void update(Chambre chambre) {
        validate(chambre);
        executeInTransaction(session -> new ChambreRepository(session).update(chambre));
    }

    public void delete(Chambre chambre) {
        executeInTransaction(session -> new ChambreRepository(session).delete(chambre));
    }

    private void validate(Chambre chambre) {
        if (chambre.getNumero() == null || chambre.getNumero() <= 0) {
            throw new IllegalArgumentException("Le numéro de chambre est obligatoire.");
        }
        ValidationUtils.requireNotBlank(chambre.getCategorie(), "La catégorie est obligatoire.");
        if (chambre.getEtage() < 0) {
            throw new IllegalArgumentException("L'étage doit être positif.");
        }
        ValidationUtils.requirePositive(chambre.getPrixParNuit(), "Le prix par nuit doit être supérieur à 0.");
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
