package com.hotel.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String typeReservation;
    private String modePaiement;

    @Enumerated(EnumType.STRING)
    private ReservationStatut statut;

    @ManyToOne
    @JoinColumn(name = "chambre_numero")
    private Chambre chambre;

    @OneToOne
    @JoinColumn(name = "client_cin", unique = true)
    private Client client;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Facture facture;

    @ManyToMany
    @JoinTable(name = "reservation_services",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private java.util.List<Service> services = new java.util.ArrayList<>();

    public Reservation() {
    }

    public Reservation(LocalDate dateDebut, LocalDate dateFin, String typeReservation, ReservationStatut statut) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.typeReservation = typeReservation;
        this.statut = statut;
    }

    public double calculerMontant() {
        if (chambre == null || dateDebut == null || dateFin == null) {
            return 0.0;
        }
        long nbNuits = ChronoUnit.DAYS.between(dateDebut, dateFin);
        double montant = nbNuits * chambre.getPrixParNuit();
        if (services != null) {
            montant += services.stream().mapToDouble(Service::getPrix).sum();
        }
        return montant;
    }

    public Long getIdReservation() {
        return idReservation;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getTypeReservation() {
        return typeReservation;
    }

    public void setTypeReservation(String typeReservation) {
        this.typeReservation = typeReservation;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public ReservationStatut getStatut() {
        return statut;
    }

    public void setStatut(ReservationStatut statut) {
        this.statut = statut;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public java.util.List<Service> getServices() {
        return services;
    }

    public void setServices(java.util.List<Service> services) {
        this.services = services;
    }
}
