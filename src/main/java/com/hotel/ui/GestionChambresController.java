package com.hotel.ui;

import com.hotel.domain.Chambre;
import com.hotel.domain.Hotel;
import com.hotel.service.ChambreService;
import com.hotel.service.HotelService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Optional;

public class GestionChambresController {
    @FXML
    private TableView<Chambre> chambreTable;
    @FXML
    private TableColumn<Chambre, Number> numeroColumn;
    @FXML
    private TableColumn<Chambre, String> categorieColumn;
    @FXML
    private TableColumn<Chambre, Number> etageColumn;
    @FXML
    private TableColumn<Chambre, Boolean> reserveColumn;
    @FXML
    private TableColumn<Chambre, Number> prixColumn;

    private final ChambreService chambreService = new ChambreService();
    private final HotelService hotelService = new HotelService();

    @FXML
    private void initialize() {
        numeroColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNumero()));
        categorieColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategorie()));
        etageColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEtage()));
        reserveColumn.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isEstReserve()));
        prixColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrixParNuit()));
        refreshTable();
    }

    @FXML
    private void onAjouter() {
        Optional<Chambre> result = showChambreDialog(null);
        result.ifPresent(chambre -> {
            try {
                chambreService.create(chambre);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Chambre ajoutée.");
            } catch (Exception exception) {
                showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
            }
        });
    }

    @FXML
    private void onModifier() {
        Chambre selected = chambreTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner une chambre.");
            return;
        }
        Optional<Chambre> result = showChambreDialog(selected);
        result.ifPresent(chambre -> {
            try {
                chambre.setNumero(selected.getNumero());
                chambreService.update(chambre);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Chambre mise à jour.");
            } catch (Exception exception) {
                showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
            }
        });
    }

    @FXML
    private void onSupprimer() {
        Chambre selected = chambreTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner une chambre.");
            return;
        }
        try {
            chambreService.delete(selected);
            refreshTable();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Chambre supprimée.");
        } catch (Exception exception) {
            showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
        }
    }

    private Optional<Chambre> showChambreDialog(Chambre chambre) {
        Dialog<Chambre> dialog = new Dialog<>();
        dialog.setTitle(chambre == null ? "Ajouter une chambre" : "Modifier une chambre");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField numeroField = new TextField(chambre != null ? String.valueOf(chambre.getNumero()) : "");
        TextField categorieField = new TextField(chambre != null ? chambre.getCategorie() : "");
        TextField etageField = new TextField(chambre != null ? String.valueOf(chambre.getEtage()) : "");
        TextField prixField = new TextField(chambre != null ? String.valueOf(chambre.getPrixParNuit()) : "");
        CheckBox reserveBox = new CheckBox("Réservée");
        reserveBox.setSelected(chambre != null && chambre.isEstReserve());

        ComboBox<Hotel> hotelBox = new ComboBox<>();
        List<Hotel> hotels = hotelService.findAll();
        hotelBox.getItems().setAll(hotels);
        hotelBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Hotel hotel) {
                return hotel == null ? "" : hotel.getNom();
            }

            @Override
            public Hotel fromString(String string) {
                return null;
            }
        });
        if (chambre != null) {
            hotelBox.setValue(chambre.getHotel());
        } else if (!hotels.isEmpty()) {
            hotelBox.setValue(hotels.get(0));
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Numéro"), numeroField);
        grid.addRow(1, new Label("Catégorie"), categorieField);
        grid.addRow(2, new Label("Étage"), etageField);
        grid.addRow(3, new Label("Prix/Nuit"), prixField);
        grid.addRow(4, new Label("Hôtel"), hotelBox);
        grid.addRow(5, reserveBox);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                Chambre result = new Chambre();
                result.setNumero(parseInt(numeroField.getText()));
                result.setCategorie(categorieField.getText());
                result.setEtage(parseInt(etageField.getText()));
                result.setPrixParNuit(parseDouble(prixField.getText()));
                result.setEstReserve(reserveBox.isSelected());
                result.setHotel(hotelBox.getValue());
                return result;
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return 0.0;
        }
    }

    private void refreshTable() {
        chambreTable.getItems().setAll(chambreService.findAll());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
