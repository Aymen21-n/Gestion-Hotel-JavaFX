package com.hotel.repository;

import com.hotel.domain.Facture;
import org.hibernate.Session;

public class FactureRepository extends BaseRepository<Facture, Long> {
    public FactureRepository(Session session) {
        super(session, Facture.class);
    }
}
