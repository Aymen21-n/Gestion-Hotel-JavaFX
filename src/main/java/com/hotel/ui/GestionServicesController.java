package com.hotel.ui;

import com.hotel.domain.*;
import com.hotel.service.HotelService;
import com.hotel.service.ServiceHotelierService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Optional;

public class GestionServicesController {
    @FXML
    private TableView<Service> serviceTable;
    @FXML
    private TableColumn<Service, String> nomColumn;
    @FXML
    private TableColumn<Service, String> ouvertureColumn;
    @FXML
    private TableColumn<Service, String> fermetureColumn;
    @FXML
    private TableColumn<Service, String> typeColumn;

    private final ServiceHotelierService serviceHotelierService = new ServiceHotelierService();
    private final HotelService hotelService = new HotelService();

    @FXML
    private void initialize() {
        nomColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomService()));
        ouvertureColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoraireOuverture()));
        fermetureColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoraireFermeture()));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClass().getSimpleName()));
        refreshTable();
    }

    @FXML
    private void onAjouter() {
        Optional<Service> result = showServiceDialog(null);
        result.ifPresent(service -> {
            try {
                serviceHotelierService.create(service);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Service ajouté.");
            } catch (Exception exception) {
                showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
            }
        });
    }

    @FXML
    private void onModifier() {
        Service selected = serviceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un service.");
            return;
        }
        Optional<Service> result = showServiceDialog(selected);
        result.ifPresent(service -> {
            try {
                serviceHotelierService.update(service);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Service mis à jour.");
            } catch (Exception exception) {
                showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
            }
        });
    }

    @FXML
    private void onSupprimer() {
        Service selected = serviceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un service.");
            return;
        }
        try {
            serviceHotelierService.delete(selected);
            refreshTable();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Service supprimé.");
        } catch (Exception exception) {
            showAlert(Alert.AlertType.ERROR, "Erreur", exception.getMessage());
        }
    }

    private Optional<Service> showServiceDialog(Service service) {
        Dialog<Service> dialog = new Dialog<>();
        dialog.setTitle(service == null ? "Ajouter un service" : "Modifier un service");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Spa", "Restauration", "Piscine", "SalleDeSport", "Club");
        if (service != null) {
            typeBox.setValue(service.getClass().getSimpleName());
            typeBox.setDisable(true);
        } else {
            typeBox.setValue("Spa");
        }

        TextField nomField = new TextField(service != null ? service.getNomService() : "");
        TextField ouvertureField = new TextField(service != null ? service.getHoraireOuverture() : "");
        TextField fermetureField = new TextField(service != null ? service.getHoraireFermeture() : "");

        TextField nbSallesMassageField = new TextField();
        TextField typesSoinsField = new TextField();
        TextField typeCuisineField = new TextField();
        TextField capaciteField = new TextField();
        TextField menuField = new TextField();
        TextField profondeurField = new TextField();
        CheckBox estChauffeeBox = new CheckBox("Chauffée");
        TextField superficieField = new TextField();
        TextField nbAppareilsField = new TextField();
        CheckBox entraineurBox = new CheckBox("Entraîneur disponible");
        TextField horairesCoursField = new TextField();
        TextField nomDjField = new TextField();
        TextField styleMusicalField = new TextField();
        TextField capaciteClubField = new TextField();
        TextField ageMinimumField = new TextField();

        if (service instanceof Spa spa) {
            nbSallesMassageField.setText(String.valueOf(spa.getNbSallesMassage()));
            typesSoinsField.setText(spa.getTypesSoins());
        } else if (service instanceof Restauration restauration) {
            typeCuisineField.setText(restauration.getTypeCuisine());
            capaciteField.setText(String.valueOf(restauration.getCapacite()));
            menuField.setText(restauration.getMenu());
        } else if (service instanceof Piscine piscine) {
            profondeurField.setText(String.valueOf(piscine.getProfondeur()));
            estChauffeeBox.setSelected(piscine.isEstChauffee());
            superficieField.setText(String.valueOf(piscine.getSuperficie()));
        } else if (service instanceof SalleDeSport salleDeSport) {
            nbAppareilsField.setText(String.valueOf(salleDeSport.getNbAppareils()));
            entraineurBox.setSelected(salleDeSport.isEntraineurDisponible());
            horairesCoursField.setText(salleDeSport.getHorairesCours());
        } else if (service instanceof Club club) {
            nomDjField.setText(club.getNomDJ());
            styleMusicalField.setText(club.getStyleMusical());
            capaciteClubField.setText(String.valueOf(club.getCapacite()));
            ageMinimumField.setText(String.valueOf(club.getAgeMinimum()));
        }

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
        if (service != null) {
            hotelBox.setValue(service.getHotel());
        } else if (!hotels.isEmpty()) {
            hotelBox.setValue(hotels.get(0));
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Type"), typeBox);
        grid.addRow(1, new Label("Nom"), nomField);
        grid.addRow(2, new Label("Ouverture"), ouvertureField);
        grid.addRow(3, new Label("Fermeture"), fermetureField);
        grid.addRow(4, new Label("Hôtel"), hotelBox);
        grid.addRow(5, new Label("Nb salles massage"), nbSallesMassageField);
        grid.addRow(6, new Label("Types de soins"), typesSoinsField);
        grid.addRow(7, new Label("Type cuisine"), typeCuisineField);
        grid.addRow(8, new Label("Capacité"), capaciteField);
        grid.addRow(9, new Label("Menu"), menuField);
        grid.addRow(10, new Label("Profondeur"), profondeurField);
        grid.addRow(11, estChauffeeBox);
        grid.addRow(12, new Label("Superficie"), superficieField);
        grid.addRow(13, new Label("Nb appareils"), nbAppareilsField);
        grid.addRow(14, entraineurBox);
        grid.addRow(15, new Label("Horaires cours"), horairesCoursField);
        grid.addRow(16, new Label("Nom DJ"), nomDjField);
        grid.addRow(17, new Label("Style musical"), styleMusicalField);
        grid.addRow(18, new Label("Capacité club"), capaciteClubField);
        grid.addRow(19, new Label("Âge minimum"), ageMinimumField);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String type = typeBox.getValue();
                Service result = service;
                if (result == null) {
                    if ("Spa".equals(type)) {
                        result = new Spa(nomField.getText(), ouvertureField.getText(), fermetureField.getText(),
                                parseInt(nbSallesMassageField.getText()), typesSoinsField.getText());
                    } else if ("Restauration".equals(type)) {
                        result = new Restauration(nomField.getText(), ouvertureField.getText(), fermetureField.getText(),
                                typeCuisineField.getText(), parseInt(capaciteField.getText()), menuField.getText());
                    } else if ("Piscine".equals(type)) {
                        result = new Piscine(nomField.getText(), ouvertureField.getText(), fermetureField.getText(),
                                parseDouble(profondeurField.getText()), estChauffeeBox.isSelected(),
                                parseDouble(superficieField.getText()));
                    } else if ("SalleDeSport".equals(type)) {
                        result = new SalleDeSport(nomField.getText(), ouvertureField.getText(), fermetureField.getText(),
                                parseInt(nbAppareilsField.getText()), entraineurBox.isSelected(),
                                horairesCoursField.getText());
                    } else {
                        result = new Club(nomField.getText(), ouvertureField.getText(), fermetureField.getText(),
                                nomDjField.getText(), styleMusicalField.getText(),
                                parseInt(capaciteClubField.getText()), parseInt(ageMinimumField.getText()));
                    }
                } else {
                    result.setNomService(nomField.getText());
                    result.setHoraireOuverture(ouvertureField.getText());
                    result.setHoraireFermeture(fermetureField.getText());
                    if (result instanceof Spa spa) {
                        spa.setNbSallesMassage(parseInt(nbSallesMassageField.getText()));
                        spa.setTypesSoins(typesSoinsField.getText());
                    } else if (result instanceof Restauration restauration) {
                        restauration.setTypeCuisine(typeCuisineField.getText());
                        restauration.setCapacite(parseInt(capaciteField.getText()));
                        restauration.setMenu(menuField.getText());
                    } else if (result instanceof Piscine piscine) {
                        piscine.setProfondeur(parseDouble(profondeurField.getText()));
                        piscine.setEstChauffee(estChauffeeBox.isSelected());
                        piscine.setSuperficie(parseDouble(superficieField.getText()));
                    } else if (result instanceof SalleDeSport salleDeSport) {
                        salleDeSport.setNbAppareils(parseInt(nbAppareilsField.getText()));
                        salleDeSport.setEntraineurDisponible(entraineurBox.isSelected());
                        salleDeSport.setHorairesCours(horairesCoursField.getText());
                    } else if (result instanceof Club club) {
                        club.setNomDJ(nomDjField.getText());
                        club.setStyleMusical(styleMusicalField.getText());
                        club.setCapacite(parseInt(capaciteClubField.getText()));
                        club.setAgeMinimum(parseInt(ageMinimumField.getText()));
                    }
                }
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
        serviceTable.getItems().setAll(serviceHotelierService.findAll());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
