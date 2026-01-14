package com.hotel.ui;

import com.hotel.config.HibernateUtil;
import com.hotel.repository.AdministrateurRepository;
import com.hotel.service.AdministrateurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;

public class LoginController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void onLogin(ActionEvent event) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            AdministrateurService service = new AdministrateurService(new AdministrateurRepository(session));
            boolean authenticated = service.authenticate(emailField.getText(), passwordField.getText());
            if (authenticated) {
                openDashboard(event);
                return;
            }
            showAlert(Alert.AlertType.ERROR, "Connexion refusée", "Identifiants incorrects.");
        } catch (IllegalArgumentException exception) {
            showAlert(Alert.AlertType.WARNING, "Validation", exception.getMessage());
        } catch (Exception exception) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de se connecter à la base de données.");
        }
    }

    private void openDashboard(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Gestion d'Hôtel - Tableau de bord");
        stage.setScene(new Scene(loader.load()));
        stage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
