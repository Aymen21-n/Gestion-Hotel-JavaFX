package com.hotel.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PISCINE")
public class Piscine extends Service {
    private double profondeur;
    private boolean estChauffee;
    private double superficie;

    public Piscine() {
    }

    public Piscine(String nomService, String horaireOuverture, String horaireFermeture, double profondeur, boolean estChauffee, double superficie) {
        super(nomService, horaireOuverture, horaireFermeture);
        this.profondeur = profondeur;
        this.estChauffee = estChauffee;
        this.superficie = superficie;
    }

    public double getProfondeur() {
        return profondeur;
    }

    public void setProfondeur(double profondeur) {
        this.profondeur = profondeur;
    }

    public boolean isEstChauffee() {
        return estChauffee;
    }

    public void setEstChauffee(boolean estChauffee) {
        this.estChauffee = estChauffee;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }
}
