package com.hotel.service;

import com.hotel.config.HibernateUtil;
import com.hotel.domain.Client;
import com.hotel.repository.ClientRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ClientService {
    public List<Client> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new ClientRepository(session).findAll();
        }
    }

    public Client findById(String cin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return new ClientRepository(session).findById(cin);
        }
    }

    public void create(Client client) {
        validate(client);
        executeInTransaction(session -> new ClientRepository(session).save(client));
    }

    public void update(Client client) {
        validate(client);
        executeInTransaction(session -> new ClientRepository(session).update(client));
    }

    public void delete(Client client) {
        executeInTransaction(session -> new ClientRepository(session).delete(client));
    }

    private void validate(Client client) {
        ValidationUtils.requireNotBlank(client.getCin(), "Le CIN est obligatoire.");
        ValidationUtils.requireNotBlank(client.getNom(), "Le nom est obligatoire.");
        ValidationUtils.requireNotBlank(client.getPrenom(), "Le prénom est obligatoire.");
        ValidationUtils.requireNotBlank(client.getTelephone(), "Le téléphone est obligatoire.");
        ValidationUtils.requireEmail(client.getEmail(), "Email invalide.");
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
