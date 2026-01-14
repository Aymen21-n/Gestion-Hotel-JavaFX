package com.hotel.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "chambres")
public class Chambre {
    @Id
    private Integer numero;

    private String categorie;
    private int etage;
    private boolean estReserve;
    private double prixParNuit;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public Chambre() {
    }

    public Chambre(Integer numero, String categorie, int etage, boolean estReserve, double prixParNuit) {
        this.numero = numero;
        this.categorie = categorie;
        this.etage = etage;
        this.estReserve = estReserve;
        this.prixParNuit = prixParNuit;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getEtage() {
        return etage;
    }

    public void setEtage(int etage) {
        this.etage = etage;
    }

    public boolean isEstReserve() {
        return estReserve;
    }

    public void setEstReserve(boolean estReserve) {
        this.estReserve = estReserve;
    }

    public double getPrixParNuit() {
        return prixParNuit;
    }

    public void setPrixParNuit(double prixParNuit) {
        this.prixParNuit = prixParNuit;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
