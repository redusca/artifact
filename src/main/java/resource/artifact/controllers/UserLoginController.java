package resource.artifact.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import resource.artifact.MainApplication;
import resource.artifact.services.SocialNetworking;

import java.io.IOException;
import java.util.Objects;

public class UserLoginController implements SceneChangerController{
    @FXML
    private TextField usernameField;
    private SocialNetworking service;
    @FXML
    private Pane thisPane;


    @Override
    public void setService(SocialNetworking service) {
        this.service=service;
    }

    @Override
    public void ChangeScene(ActionEvent actionEvent) throws IOException {
    }

    @FXML
    private void LoginAction(ActionEvent actionEvent) throws  IOException{
        if(Objects.equals(usernameField.getText(), "andrei")){
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("userAcc-view.fxml"));
            Stage stage = new Stage();
            AnchorPane userLayout = fxmlLoader.load();
            stage.setScene(new Scene(userLayout));

            UsersAdminController userController = fxmlLoader.getController();
            userController.setService(service);

            stage.setTitle("andrei account");
            stage.show();
            stage = (Stage) thisPane.getScene().getWindow();
            stage.close();
            return;

        }
        if(Objects.equals(usernameField.getText(), "admin")){
            LoginActionAdmin();
            return;
        }

        Alert alert =new Alert(Alert.AlertType.WARNING);
        alert.setContentText("User doesn't exist!Try to sign up maybe");;
        alert.show();
    }

    private void LoginActionAdmin() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("userAdmin-view.fxml"));
        Stage stage = new Stage();
        AnchorPane userLayout = fxmlLoader.load();
        stage.setScene(new Scene(userLayout));

        UsersAdminController userController = fxmlLoader.getController();
        userController.setService(service);

        stage.setTitle("Admin Panel");
        stage.show();

        stage = (Stage) thisPane.getScene().getWindow();
        stage.close();

    }
}
