package com.hotel.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String adresse;
    private double note;
    private String telephone;
    private int nbChambres;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chambre> chambres = new ArrayList<>();

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employe> employes = new ArrayList<>();

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Service> services = new ArrayList<>();

    public Hotel() {
    }

    public Hotel(String nom, String adresse, double note, String telephone, int nbChambres) {
        this.nom = nom;
        this.adresse = adresse;
        this.note = note;
        this.telephone = telephone;
        this.nbChambres = nbChambres;
    }

    public void addChambre(Chambre chambre) {
        chambres.add(chambre);
        chambre.setHotel(this);
    }

    public void addEmploye(Employe employe) {
        employes.add(employe);
        employe.setHotel(this);
    }

    public void addService(Service service) {
        services.add(service);
        service.setHotel(this);
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getNbChambres() {
        return nbChambres;
    }

    public void setNbChambres(int nbChambres) {
        this.nbChambres = nbChambres;
    }

    public List<Chambre> getChambres() {
        return chambres;
    }

    public List<Employe> getEmployes() {
        return employes;
    }

    public List<Service> getServices() {
        return services;
    }
}
