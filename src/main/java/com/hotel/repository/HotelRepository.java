package com.hotel.repository;

import com.hotel.domain.Hotel;
import org.hibernate.Session;

public class HotelRepository extends BaseRepository<Hotel, Long> {
    public HotelRepository(Session session) {
        super(session, Hotel.class);
    }
}
