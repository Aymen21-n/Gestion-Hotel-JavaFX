package com.hotel.repository;

import com.hotel.domain.Administrateur;
import org.hibernate.Session;

public class AdministrateurRepository extends BaseRepository<Administrateur, String> {
    public AdministrateurRepository(Session session) {
        super(session, Administrateur.class);
    }

    public Administrateur findByEmail(String email) {
        return session.createQuery("from Administrateur a where a.emailAdmin = :email", Administrateur.class)
                .setParameter("email", email)
                .uniqueResult();
    }
}
