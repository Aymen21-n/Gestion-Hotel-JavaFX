package com.hotel.ui;

import com.hotel.domain.Facture;
import com.hotel.domain.Reservation;
import com.hotel.service.FactureService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class FacturesController {
    @FXML
    private TableView<Facture> factureTable;
    @FXML
    private TableColumn<Facture, String> reservationColumn;
    @FXML
    private TableColumn<Facture, String> clientColumn;
    @FXML
    private TableColumn<Facture, String> dateColumn;
    @FXML
    private TableColumn<Facture, String> montantColumn;
    @FXML
    private TableColumn<Facture, String> modePaiementColumn;

    private final FactureService factureService = new FactureService();

    @FXML
    private void initialize() {
        reservationColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getReservation() != null
                        ? String.valueOf(data.getValue().getReservation().getIdReservation())
                        : ""));
        clientColumn.setCellValueFactory(data -> new SimpleStringProperty(formatClientName(data.getValue())));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getDateFacture())));
        montantColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getMontantTotal())));
        modePaiementColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModePaiement()));
        refreshTable();
    }

    @FXML
    private void onAfficher() {
        Facture selected = factureTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner une facture.");
            return;
        }
        Reservation reservation = selected.getReservation();
        String details = "Facture n° " + selected.getIdFacture() + "\n" +
                "Réservation: " + (reservation != null ? reservation.getIdReservation() : "-") + "\n" +
                "Client: " + (reservation != null ? formatClientName(selected) : "-") + "\n" +
                "Chambre: " + (reservation != null && reservation.getChambre() != null
                ? reservation.getChambre().getNumero() : "-") + "\n" +
                "Montant: " + selected.getMontantTotal() + "\n" +
                "Mode de paiement: " + selected.getModePaiement();
        showAlert(Alert.AlertType.INFORMATION, "Détails facture", details);
    }

    private void refreshTable() {
        factureTable.getItems().setAll(factureService.findAll());
    }

    private String formatClientName(Facture facture) {
        Reservation reservation = facture.getReservation();
        if (reservation == null || reservation.getClient() == null) {
            return "";
        }
        return reservation.getClient().getNom() + " " + reservation.getClient().getPrenom();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
