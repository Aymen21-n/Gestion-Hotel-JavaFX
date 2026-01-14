package com.hotel.ui;

import com.hotel.domain.Employe;
import com.hotel.domain.Hotel;
import com.hotel.service.EmployeService;
import com.hotel.service.HotelService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Optional;

public class GestionEmployesController {
    @FXML
    private TableView<Employe> employeTable;
    @FXML
    private TableColumn<Employe, String> nomColumn;
    @FXML
    private TableColumn<Employe, String> prenomColumn;
    @FXML
    private TableColumn<Employe, String> posteColumn;
    @FXML
    private TableColumn<Employe, Number> salaireColumn;
    @FXML
    private TableColumn<Employe, String> horaireColumn;

    private final EmployeService employeService = new EmployeService();
    private final HotelService hotelService = new HotelService();

    @FXML
    private void initialize() {
        nomColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        prenomColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenom()));
        posteColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPoste()));
        salaireColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getSalaire()));
        horaireColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoraire()));
        refreshTable();
    }

    @FXML
    private void onAjouter() {
        Optional<Employe> result = showEmployeDialog(null);
        result.ifPresent(employe -> {
            try {
                employeService.create(employe);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Employé ajouté.");
            } catch (Exception exception) {
                showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
            }
        });
    }

    @FXML
    private void onModifier() {
        Employe selected = employeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un employé.");
            return;
        }
        Optional<Employe> result = showEmployeDialog(selected);
        result.ifPresent(employe -> {
            try {
                employeService.update(employe);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Employé mis à jour.");
            } catch (Exception exception) {
                showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
            }
        });
    }

    @FXML
    private void onSupprimer() {
        Employe selected = employeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un employé.");
            return;
        }
        try {
            employeService.delete(selected);
            refreshTable();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Employé supprimé.");
        } catch (Exception exception) {
            showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
        }
    }

    private Optional<Employe> showEmployeDialog(Employe employe) {
        Dialog<Employe> dialog = new Dialog<>();
        dialog.setTitle(employe == null ? "Ajouter un employé" : "Modifier un employé");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField nomField = new TextField(employe != null ? employe.getNom() : "");
        TextField prenomField = new TextField(employe != null ? employe.getPrenom() : "");
        TextField posteField = new TextField(employe != null ? employe.getPoste() : "");
        TextField salaireField = new TextField(employe != null ? String.valueOf(employe.getSalaire()) : "");
        TextField horaireField = new TextField(employe != null ? employe.getHoraire() : "");

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
        if (employe != null) {
            hotelBox.setValue(employe.getHotel());
        } else if (!hotels.isEmpty()) {
            hotelBox.setValue(hotels.get(0));
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Nom"), nomField);
        grid.addRow(1, new Label("Prénom"), prenomField);
        grid.addRow(2, new Label("Poste"), posteField);
        grid.addRow(3, new Label("Salaire"), salaireField);
        grid.addRow(4, new Label("Horaire"), horaireField);
        grid.addRow(5, new Label("Hôtel"), hotelBox);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                Employe result = employe != null ? employe : new Employe();
                result.setNom(nomField.getText());
                result.setPrenom(prenomField.getText());
                result.setPoste(posteField.getText());
                result.setSalaire(parseDouble(salaireField.getText()));
                result.setHoraire(horaireField.getText());
                result.setHotel(hotelBox.getValue());
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

    private void refreshTable() {
        employeTable.getItems().setAll(employeService.findAll());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
