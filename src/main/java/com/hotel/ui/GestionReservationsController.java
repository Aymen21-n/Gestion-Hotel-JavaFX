package com.hotel.ui;

import com.hotel.domain.Chambre;
import com.hotel.domain.Client;
import com.hotel.domain.Reservation;
import com.hotel.service.ChambreService;
import com.hotel.service.ClientService;
import com.hotel.service.ReservationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GestionReservationsController {
    @FXML
    private DatePicker filterDebut;
    @FXML
    private DatePicker filterFin;
    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private TableColumn<Reservation, String> clientColumn;
    @FXML
    private TableColumn<Reservation, String> chambreColumn;
    @FXML
    private TableColumn<Reservation, String> dateDebutColumn;
    @FXML
    private TableColumn<Reservation, String> dateFinColumn;
    @FXML
    private TableColumn<Reservation, String> statutColumn;

    private final ReservationService reservationService = new ReservationService();
    private final ClientService clientService = new ClientService();
    private final ChambreService chambreService = new ChambreService();

    @FXML
    private void initialize() {
        clientColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getClient() != null ? data.getValue().getClient().getNom() : ""));
        chambreColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getChambre() != null ? String.valueOf(data.getValue().getChambre().getNumero()) : ""));
        dateDebutColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getDateDebut())));
        dateFinColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getDateFin())));
        statutColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStatut())));
        refreshTable();
    }

    @FXML
    private void onCreer() {
        Optional<Reservation> result = showReservationDialog();
        result.ifPresent(reservation -> {
            try {
                reservationService.createReservation(reservation.getClient().getCin(), reservation.getChambre().getNumero(),
                        reservation.getDateDebut(), reservation.getDateFin(), reservation.getTypeReservation());
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation créée.");
            } catch (Exception exception) {
                showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
            }
        });
    }

    @FXML
    private void onAnnuler() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner une réservation.");
            return;
        }
        try {
            reservationService.cancelReservation(selected.getIdReservation());
            refreshTable();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation annulée.");
        } catch (Exception exception) {
            showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
        }
    }

    @FXML
    private void onFiltrer() {
        LocalDate debut = filterDebut.getValue();
        LocalDate fin = filterFin.getValue();
        if (debut == null || fin == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez renseigner les dates.");
            return;
        }
        List<Chambre> disponibles = reservationService.listChambresDisponibles(debut, fin);
        String result = disponibles.stream()
                .map(chambre -> String.valueOf(chambre.getNumero()))
                .collect(Collectors.joining(", "));
        showAlert(Alert.AlertType.INFORMATION, "Chambres disponibles",
                result.isEmpty() ? "Aucune chambre disponible." : "Chambres: " + result);
    }

    private Optional<Reservation> showReservationDialog() {
        Dialog<Reservation> dialog = new Dialog<>();
        dialog.setTitle("Créer une réservation");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Client> clientBox = new ComboBox<>();
        clientBox.getItems().setAll(clientService.findAll());
        clientBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Client client) {
                return client == null ? "" : client.getNom() + " " + client.getPrenom();
            }

            @Override
            public Client fromString(String string) {
                return null;
            }
        });

        ComboBox<Chambre> chambreBox = new ComboBox<>();
        chambreBox.getItems().setAll(chambreService.findAll());
        chambreBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Chambre chambre) {
                return chambre == null ? "" : "Chambre " + chambre.getNumero();
            }

            @Override
            public Chambre fromString(String string) {
                return null;
            }
        });

        DatePicker debutPicker = new DatePicker();
        DatePicker finPicker = new DatePicker();
        TextField typeField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Client"), clientBox);
        grid.addRow(1, new Label("Chambre"), chambreBox);
        grid.addRow(2, new Label("Date début"), debutPicker);
        grid.addRow(3, new Label("Date fin"), finPicker);
        grid.addRow(4, new Label("Type"), typeField);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                Reservation reservation = new Reservation();
                reservation.setClient(clientBox.getValue());
                reservation.setChambre(chambreBox.getValue());
                reservation.setDateDebut(debutPicker.getValue());
                reservation.setDateFin(finPicker.getValue());
                reservation.setTypeReservation(typeField.getText());
                return reservation;
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void refreshTable() {
        reservationTable.getItems().setAll(reservationService.findAll());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
