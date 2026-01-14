package com.hotel.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "administrateurs")
public class Administrateur {
    @Id
    private String emailAdmin;

    private String motDePasse;

    public Administrateur() {
    }

    public Administrateur(String emailAdmin, String motDePasse) {
        this.emailAdmin = emailAdmin;
        this.motDePasse = motDePasse;
    }

    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
