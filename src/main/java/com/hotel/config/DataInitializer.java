package com.hotel.config;

import com.hotel.domain.*;
import com.hotel.repository.AdministrateurRepository;
import com.hotel.repository.ClientRepository;
import com.hotel.repository.HotelRepository;
import com.hotel.repository.ServiceRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public final class DataInitializer {
    private DataInitializer() {
    }

    public static void initialize() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            ensureSchema(session);

            HotelRepository hotelRepository = new HotelRepository(session);
            AdministrateurRepository administrateurRepository = new AdministrateurRepository(session);
            ClientRepository clientRepository = new ClientRepository(session);
            ServiceRepository serviceRepository = new ServiceRepository(session);

            if (hotelRepository.count() == 0) {
                Hotel hotel = new Hotel("Hotel Oasis", "Avenue du Soleil, Tunis", 4.6, "+216 71 000 000", 120);
                Chambre chambre1 = new Chambre(101, "Deluxe", 1, false, 180.0);
                Chambre chambre2 = new Chambre(204, "Standard", 2, false, 110.0);
                Chambre chambre3 = new Chambre(305, "Suite", 3, false, 260.0);
                hotel.addChambre(chambre1);
                hotel.addChambre(chambre2);
                hotel.addChambre(chambre3);

                Employe employe = new Employe("Ben", "Salma", "Réception", 2200, "08:00-16:00");
                hotel.addEmploye(employe);

                Spa spa = new Spa("Spa Zen", "09:00", "20:00", 5, "Massage, Hammam");
                Restauration restauration = new Restauration("Restaurant Panorama", "12:00", "23:00", "Méditerranéenne", 120, "Menu du jour");
                spa.setPrix(90.0);
                restauration.setPrix(45.0);
                hotel.addService(spa);
                hotel.addService(restauration);

                hotelRepository.save(hotel);

                Administrateur administrateur = new Administrateur("admin@hotel.tn", "admin123");
                administrateurRepository.save(administrateur);

                Client client1 = new Client("CIN123456", "Trabelsi", "Nour", "+216 55 000 000", "nour@example.com");
                Client client2 = new Client("CIN654321", "Haddad", "Omar", "+216 22 000 000", "omar@example.com");
                clientRepository.save(client1);
                clientRepository.save(client2);

                Reservation reservation = new Reservation(LocalDate.now().plusDays(1), LocalDate.now().plusDays(4),
                        null, ReservationStatut.CONFIRMEE);
                reservation.setChambre(chambre1);
                reservation.setClient(client1);
                reservation.setModePaiement("CARTE");
                reservation.setServices(List.of(spa));
                session.persist(reservation);

                Facture facture = Facture.genererFacture(reservation, "Carte bancaire");
                session.persist(facture);

                serviceRepository.save(spa);
                serviceRepository.save(restauration);
            }

            transaction.commit();
        }
    }

    private static void ensureSchema(Session session) {
        session.createNativeQuery("""
                IF COL_LENGTH('services', 'prix') IS NULL
                ALTER TABLE services ADD prix FLOAT NOT NULL DEFAULT 0
                """).executeUpdate();
        session.createNativeQuery("""
                DECLARE @constraintName NVARCHAR(200);
                SELECT @constraintName = cc.name
                FROM sys.check_constraints cc
                JOIN sys.columns c ON c.object_id = cc.parent_object_id AND c.column_id = cc.parent_column_id
                WHERE cc.parent_object_id = OBJECT_ID('reservations') AND c.name = 'statut';
                IF @constraintName IS NOT NULL
                    EXEC('ALTER TABLE reservations DROP CONSTRAINT ' + @constraintName);
                ALTER TABLE reservations ADD CONSTRAINT CK_reservations_statut
                    CHECK (statut IN ('EN_COURS', 'CONFIRMEE', 'ANNULEE', 'TERMINEE'));
                """).executeUpdate();
    }
}
