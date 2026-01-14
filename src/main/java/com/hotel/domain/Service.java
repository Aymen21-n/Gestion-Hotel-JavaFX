package com.hotel.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "services")
// Choix SINGLE_TABLE pour simplifier le modèle et centraliser les services.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_service")
public abstract class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomService;
    private String horaireOuverture;
    private String horaireFermeture;
    @Column(nullable = false, columnDefinition = "float default 0")
    private double prix;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    protected Service() {
    }

    protected Service(String nomService, String horaireOuverture, String horaireFermeture) {
        this.nomService = nomService;
        this.horaireOuverture = horaireOuverture;
        this.horaireFermeture = horaireFermeture;
    }

    public Long getId() {
        return id;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public String getHoraireOuverture() {
        return horaireOuverture;
    }

    public void setHoraireOuverture(String horaireOuverture) {
        this.horaireOuverture = horaireOuverture;
    }

    public String getHoraireFermeture() {
        return horaireFermeture;
    }

    public void setHoraireFermeture(String horaireFermeture) {
        this.horaireFermeture = horaireFermeture;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
