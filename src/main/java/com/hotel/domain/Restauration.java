package com.hotel.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("RESTAURATION")
public class Restauration extends Service {
    private String typeCuisine;
    private int capacite;
    private String menu;

    public Restauration() {
    }

    public Restauration(String nomService, String horaireOuverture, String horaireFermeture, String typeCuisine, int capacite, String menu) {
        super(nomService, horaireOuverture, horaireFermeture);
        this.typeCuisine = typeCuisine;
        this.capacite = capacite;
        this.menu = menu;
    }

    public String getTypeCuisine() {
        return typeCuisine;
    }

    public void setTypeCuisine(String typeCuisine) {
        this.typeCuisine = typeCuisine;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}
