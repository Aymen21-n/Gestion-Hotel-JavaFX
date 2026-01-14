package com.hotel.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLUB")
public class Club extends Service {
    private String nomDJ;
    private String styleMusical;
    private int capacite;
    private int ageMinimum;

    public Club() {
    }

    public Club(String nomService, String horaireOuverture, String horaireFermeture, String nomDJ, String styleMusical,
                int capacite, int ageMinimum) {
        super(nomService, horaireOuverture, horaireFermeture);
        this.nomDJ = nomDJ;
        this.styleMusical = styleMusical;
        this.capacite = capacite;
        this.ageMinimum = ageMinimum;
    }

    public String getNomDJ() {
        return nomDJ;
    }

    public void setNomDJ(String nomDJ) {
        this.nomDJ = nomDJ;
    }

    public String getStyleMusical() {
        return styleMusical;
    }

    public void setStyleMusical(String styleMusical) {
        this.styleMusical = styleMusical;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public int getAgeMinimum() {
        return ageMinimum;
    }

    public void setAgeMinimum(int ageMinimum) {
        this.ageMinimum = ageMinimum;
    }
}
