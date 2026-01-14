package com.hotel.repository;

import com.hotel.domain.Service;
import org.hibernate.Session;

public class ServiceRepository extends BaseRepository<Service, Long> {
    public ServiceRepository(Session session) {
        super(session, Service.class);
    }
}
