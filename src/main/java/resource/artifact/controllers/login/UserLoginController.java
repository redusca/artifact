package resource.artifact.controllers.login;

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
import resource.artifact.controllers.SceneChangerController;
import resource.artifact.controllers.admin.UsersAdminController;
import resource.artifact.controllers.user.UserAccController;
import resource.artifact.domains.User;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.fx.AlterCreator;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class UserLoginController implements SceneChangerController {
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
        Optional<User> loginUser = service.find_by_Username_User(usernameField.getText());
        if(loginUser.isPresent()){
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("userAcc-view.fxml"));
            Stage stage = new Stage();
            Pane userLayout = fxmlLoader.load();
            stage.setScene(new Scene(userLayout));

            UserAccController userController = fxmlLoader.getController();
            userController.setService(service);
            userController.setUser(loginUser.get());

            stage.setTitle(usernameField.getText() + " account");
            stage.show();
          //stage = (Stage) thisPane.getScene().getWindow();
          //stage.close();
            return;

        }
        if(Objects.equals(usernameField.getText(), "admin")){
            LoginActionAdmin();
            return;
        }

        AlterCreator.create(Alert.AlertType.INFORMATION,"Username not found!");
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


    }
}
