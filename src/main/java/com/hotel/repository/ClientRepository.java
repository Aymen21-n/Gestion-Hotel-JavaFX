package com.hotel.repository;

import com.hotel.domain.Client;
import org.hibernate.Session;

public class ClientRepository extends BaseRepository<Client, String> {
    public ClientRepository(Session session) {
        super(session, Client.class);
    }
}
