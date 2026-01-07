package com.hotel.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private void initialize() {
        welcomeLabel.setText("Bienvenue dans le tableau de bord");
    }
}
