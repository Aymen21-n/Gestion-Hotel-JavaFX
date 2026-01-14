package com.hotel.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "factures")
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFacture;

    private LocalDate dateFacture;
    private double montantTotal;
    private String modePaiement;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public Facture() {
    }

    public Facture(LocalDate dateFacture, double montantTotal, String modePaiement) {
        this.dateFacture = dateFacture;
        this.montantTotal = montantTotal;
        this.modePaiement = modePaiement;
    }

    public static Facture genererFacture(Reservation reservation, String modePaiement) {
        Facture facture = new Facture(LocalDate.now(), reservation.calculerMontant(), modePaiement);
        facture.setReservation(reservation);
        reservation.setFacture(facture);
        return facture;
    }

    public Long getIdFacture() {
        return idFacture;
    }

    public LocalDate getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
