package com.hotel.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SPA")
public class Spa extends Service {
    private int nbSallesMassage;
    private String typesSoins;

    public Spa() {
    }

    public Spa(String nomService, String horaireOuverture, String horaireFermeture, int nbSallesMassage, String typesSoins) {
        super(nomService, horaireOuverture, horaireFermeture);
        this.nbSallesMassage = nbSallesMassage;
        this.typesSoins = typesSoins;
    }

    public int getNbSallesMassage() {
        return nbSallesMassage;
    }

    public void setNbSallesMassage(int nbSallesMassage) {
        this.nbSallesMassage = nbSallesMassage;
    }

    public String getTypesSoins() {
        return typesSoins;
    }

    public void setTypesSoins(String typesSoins) {
        this.typesSoins = typesSoins;
    }
}
