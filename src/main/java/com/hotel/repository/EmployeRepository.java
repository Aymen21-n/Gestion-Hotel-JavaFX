package com.hotel.repository;

import com.hotel.domain.Employe;
import org.hibernate.Session;

public class EmployeRepository extends BaseRepository<Employe, Long> {
    public EmployeRepository(Session session) {
        super(session, Employe.class);
    }
}
