package com.hotel.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SALLE_SPORT")
public class SalleDeSport extends Service {
    private int nbAppareils;
    private boolean entraineurDisponible;
    private String horairesCours;

    public SalleDeSport() {
    }

    public SalleDeSport(String nomService, String horaireOuverture, String horaireFermeture, int nbAppareils,
                        boolean entraineurDisponible, String horairesCours) {
        super(nomService, horaireOuverture, horaireFermeture);
        this.nbAppareils = nbAppareils;
        this.entraineurDisponible = entraineurDisponible;
        this.horairesCours = horairesCours;
    }

    public int getNbAppareils() {
        return nbAppareils;
    }

    public void setNbAppareils(int nbAppareils) {
        this.nbAppareils = nbAppareils;
    }

    public boolean isEntraineurDisponible() {
        return entraineurDisponible;
    }

    public void setEntraineurDisponible(boolean entraineurDisponible) {
        this.entraineurDisponible = entraineurDisponible;
    }

    public String getHorairesCours() {
        return horairesCours;
    }

    public void setHorairesCours(String horairesCours) {
        this.horairesCours = horairesCours;
    }
}
