package com.hotel.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class DashboardController {
    @FXML
    private BorderPane root;

    @FXML
    private Label welcomeLabel;

    @FXML
    private void initialize() {
        welcomeLabel.setText("Bienvenue dans le tableau de bord");
    }

    @FXML
    private void openHotel() {
        loadCenter("/fxml/gestion-hotel.fxml");
    }

    @FXML
    private void openChambres() {
        loadCenter("/fxml/gestion-chambres.fxml");
    }

    @FXML
    private void openClients() {
        loadCenter("/fxml/gestion-clients.fxml");
    }

    @FXML
    private void openEmployes() {
        loadCenter("/fxml/gestion-employes.fxml");
    }

    @FXML
    private void openServices() {
        loadCenter("/fxml/gestion-services.fxml");
    }

    @FXML
    private void openReservations() {
        loadCenter("/fxml/gestion-reservations.fxml");
    }

    @FXML
    private void openFactures() {
        loadCenter("/fxml/factures.fxml");
    }

    private void loadCenter(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            root.setCenter(loader.load());
        } catch (IOException exception) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'écran demandé.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
