package com.hotel.repository;

import com.hotel.domain.Chambre;
import org.hibernate.Session;

public class ChambreRepository extends BaseRepository<Chambre, Integer> {
    public ChambreRepository(Session session) {
        super(session, Chambre.class);
    }
}
