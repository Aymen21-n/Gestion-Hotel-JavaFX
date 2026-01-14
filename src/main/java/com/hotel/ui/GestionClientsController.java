package com.hotel.ui;

import com.hotel.domain.Client;
import com.hotel.service.ClientService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class GestionClientsController {
    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TableColumn<Client, String> cinColumn;
    @FXML
    private TableColumn<Client, String> nomColumn;
    @FXML
    private TableColumn<Client, String> prenomColumn;
    @FXML
    private TableColumn<Client, String> telephoneColumn;
    @FXML
    private TableColumn<Client, String> emailColumn;

    private final ClientService clientService = new ClientService();

    @FXML
    private void initialize() {
        cinColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCin()));
        nomColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        prenomColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenom()));
        telephoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelephone()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        refreshTable();
    }

    @FXML
    private void onAjouter() {
        Optional<Client> result = showClientDialog(null);
        result.ifPresent(client -> {
            try {
                clientService.create(client);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Client ajouté.");
            } catch (Exception exception) {
                showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
            }
        });
    }

    @FXML
    private void onModifier() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un client.");
            return;
        }
        Optional<Client> result = showClientDialog(selected);
        result.ifPresent(client -> {
            try {
                client.setCin(selected.getCin());
                clientService.update(client);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Client mis à jour.");
            } catch (Exception exception) {
                showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
            }
        });
    }

    @FXML
    private void onSupprimer() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un client.");
            return;
        }
        try {
            clientService.delete(selected);
            refreshTable();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Client supprimé.");
        } catch (Exception exception) {
            showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
        }
    }

    private Optional<Client> showClientDialog(Client client) {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle(client == null ? "Ajouter un client" : "Modifier un client");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField cinField = new TextField(client != null ? client.getCin() : "");
        cinField.setDisable(client != null);
        TextField nomField = new TextField(client != null ? client.getNom() : "");
        TextField prenomField = new TextField(client != null ? client.getPrenom() : "");
        TextField telephoneField = new TextField(client != null ? client.getTelephone() : "");
        TextField emailField = new TextField(client != null ? client.getEmail() : "");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("CIN"), cinField);
        grid.addRow(1, new Label("Nom"), nomField);
        grid.addRow(2, new Label("Prénom"), prenomField);
        grid.addRow(3, new Label("Téléphone"), telephoneField);
        grid.addRow(4, new Label("Email"), emailField);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                Client result = new Client();
                result.setCin(cinField.getText());
                result.setNom(nomField.getText());
                result.setPrenom(prenomField.getText());
                result.setTelephone(telephoneField.getText());
                result.setEmail(emailField.getText());
                return result;
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void refreshTable() {
        clientTable.getItems().setAll(clientService.findAll());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
