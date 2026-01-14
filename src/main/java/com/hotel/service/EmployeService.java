package com.hotel.service;

import com.hotel.config.HibernateUtil;
import com.hotel.domain.Employe;
import com.hotel.repository.EmployeRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeService {
    public List<Employe> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new EmployeRepository(session).findAll();
        }
    }

    public Employe findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new EmployeRepository(session).findById(id);
        }
    }

    public void create(Employe employe) {
        validate(employe);
        executeInTransaction(session -> new EmployeRepository(session).save(employe));
    }

    public void update(Employe employe) {
        validate(employe);
        executeInTransaction(session -> new EmployeRepository(session).update(employe));
    }

    public void delete(Employe employe) {
        executeInTransaction(session -> new EmployeRepository(session).delete(employe));
    }

    private void validate(Employe employe) {
        ValidationUtils.requireNotBlank(employe.getNom(), "Le nom est obligatoire.");
        ValidationUtils.requireNotBlank(employe.getPrenom(), "Le prénom est obligatoire.");
        ValidationUtils.requireNotBlank(employe.getPoste(), "Le poste est obligatoire.");
        ValidationUtils.requirePositive(employe.getSalaire(), "Le salaire doit être supérieur à 0.");
        ValidationUtils.requireNotBlank(employe.getHoraire(), "L'horaire est obligatoire.");
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
