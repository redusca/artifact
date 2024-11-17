package resource.artifact.controllers;

import com.fasterxml.jackson.core.json.DupDetector;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import resource.artifact.domains.validators.ValidationException;
import resource.artifact.services.SocialNetworking;
import javafx.event.ActionEvent;


public class AddUserController {
    @FXML
    public TextField FirstNameField;
    @FXML
    public TextField LastNameField;
    private SocialNetworking service;
    @FXML
    private StackPane stackPane;

    public void setService(SocialNetworking service) {
        this.service = service;
    }

    @FXML
    private void AddUserAction(ActionEvent actionEvent){
        try{
            service.add_user(FirstNameField.getText(),LastNameField.getText());
            Stage stage = (Stage) stackPane.getScene().getWindow();
            stage.close();
        } catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

}
