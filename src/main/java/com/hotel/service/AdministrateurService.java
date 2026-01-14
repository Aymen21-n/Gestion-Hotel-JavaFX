package com.hotel.service;

import com.hotel.domain.Administrateur;
import com.hotel.repository.AdministrateurRepository;

public class AdministrateurService {
    private final AdministrateurRepository repository;

    public AdministrateurService(AdministrateurRepository repository) {
        this.repository = repository;
    }

    public boolean authenticate(String email, String motDePasse) {
        ValidationUtils.requireEmail(email, "Email administrateur invalide.");
        ValidationUtils.requireNotBlank(motDePasse, "Mot de passe requis.");

        Administrateur administrateur = repository.findByEmail(email);
        return administrateur != null && motDePasse.equals(administrateur.getMotDePasse());
    }
}
