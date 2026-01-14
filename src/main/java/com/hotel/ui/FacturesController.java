package com.hotel.ui;

import com.hotel.domain.Facture;
import com.hotel.domain.Reservation;
import com.hotel.service.FactureService;
import com.hotel.service.ReservationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Optional;

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
    private final ReservationService reservationService = new ReservationService();

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
    private void onGenerer() {
        Optional<Facture> result = showFactureDialog();
        result.ifPresent(facture -> {
            try {
                factureService.generateForReservation(facture.getReservation().getIdReservation(), facture.getModePaiement());
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Facture générée.");
            } catch (Exception exception) {
                showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
            }
        });
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

    private Optional<Facture> showFactureDialog() {
        Dialog<Facture> dialog = new Dialog<>();
        dialog.setTitle("Générer une facture");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Reservation> reservationBox = new ComboBox<>();
        List<Reservation> reservations = reservationService.findConfirmableForFacture();
        reservationBox.getItems().setAll(reservations);
        reservationBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Reservation reservation) {
                return reservation == null ? "" : "Réservation " + reservation.getIdReservation();
            }

            @Override
            public Reservation fromString(String string) {
                return null;
            }
        });
        if (!reservations.isEmpty()) {
            reservationBox.setValue(reservations.get(0));
        }

        TextField modePaiementField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Réservation"), reservationBox);
        grid.addRow(1, new Label("Mode de paiement"), modePaiementField);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                Facture facture = new Facture();
                facture.setReservation(reservationBox.getValue());
                facture.setModePaiement(modePaiementField.getText());
                return facture;
            }
            return null;
        });

        return dialog.showAndWait();
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
