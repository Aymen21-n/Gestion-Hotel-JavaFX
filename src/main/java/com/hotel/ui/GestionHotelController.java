package com.hotel.ui;

import com.hotel.domain.Hotel;
import com.hotel.service.HotelService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class GestionHotelController {
    @FXML
    private TableView<Hotel> hotelTable;
    @FXML
    private TableColumn<Hotel, String> nomColumn;
    @FXML
    private TableColumn<Hotel, String> adresseColumn;
    @FXML
    private TableColumn<Hotel, Number> noteColumn;
    @FXML
    private TableColumn<Hotel, String> telephoneColumn;
    @FXML
    private TableColumn<Hotel, Number> nbChambresColumn;

    private final HotelService hotelService = new HotelService();

    @FXML
    private void initialize() {
        nomColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        adresseColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAdresse()));
        noteColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getNote()));
        telephoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelephone()));
        nbChambresColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNbChambres()));
        refreshTable();
    }

    @FXML
    private void onAjouter() {
        Optional<Hotel> result = showHotelDialog(null);
        result.ifPresent(hotel -> {
            try {
                hotelService.create(hotel);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Hôtel ajouté avec succès.");
            } catch (Exception exception) {
                showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
            }
        });
    }

    @FXML
    private void onModifier() {
        Hotel selected = hotelTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un hôtel.");
            return;
        }
        Optional<Hotel> result = showHotelDialog(selected);
        result.ifPresent(hotel -> {
            try {
                hotel.setId(selected.getId());
                hotelService.update(hotel);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Hôtel mis à jour.");
            } catch (Exception exception) {
                showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
            }
        });
    }

    @FXML
    private void onSupprimer() {
        Hotel selected = hotelTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un hôtel.");
            return;
        }
        try {
            hotelService.delete(selected);
            refreshTable();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Hôtel supprimé.");
        } catch (Exception exception) {
            showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
        }
    }

    private Optional<Hotel> showHotelDialog(Hotel hotel) {
        Dialog<Hotel> dialog = new Dialog<>();
        dialog.setTitle(hotel == null ? "Ajouter un hôtel" : "Modifier un hôtel");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField nomField = new TextField(hotel != null ? hotel.getNom() : "");
        TextField adresseField = new TextField(hotel != null ? hotel.getAdresse() : "");
        TextField noteField = new TextField(hotel != null ? String.valueOf(hotel.getNote()) : "");
        TextField telephoneField = new TextField(hotel != null ? hotel.getTelephone() : "");
        TextField nbChambresField = new TextField(hotel != null ? String.valueOf(hotel.getNbChambres()) : "");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Nom"), nomField);
        grid.addRow(1, new Label("Adresse"), adresseField);
        grid.addRow(2, new Label("Note"), noteField);
        grid.addRow(3, new Label("Téléphone"), telephoneField);
        grid.addRow(4, new Label("Nb chambres"), nbChambresField);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                Hotel result = new Hotel();
                result.setNom(nomField.getText());
                result.setAdresse(adresseField.getText());
                result.setNote(parseDouble(noteField.getText()));
                result.setTelephone(telephoneField.getText());
                result.setNbChambres(parseInt(nbChambresField.getText()));
                return result;
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return 0.0;
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private void refreshTable() {
        hotelTable.getItems().setAll(hotelService.findAll());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
