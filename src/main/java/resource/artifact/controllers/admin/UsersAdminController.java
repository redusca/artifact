package resource.artifact.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import resource.artifact.MainApplication;
import javafx.event.ActionEvent;
import resource.artifact.controllers.SceneChangerController;
import resource.artifact.domains.User;
import resource.artifact.domains.validators.ValidationException;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.fx.AlterCreator;
import resource.artifact.utils.fx.SceneSwitch;
import resource.artifact.utils.events.AccountEvent;
import resource.artifact.utils.events.ChangeEvent;
import resource.artifact.utils.observers.Observer;

import java.io.IOException;

public class UsersAdminController implements SceneChangerController, Observer<AccountEvent> {
    private SocialNetworking service;
    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField UsernameField;
    @FXML
    private Label message;
    @FXML
    private TableView<User> tableView;
    @FXML
    TableColumn<User,String> tableColumnFirstName;
    @FXML
    TableColumn<User,String> tableColumnLastName;
    @FXML
    TableColumn<User,String> tableColumnUsername;
    @FXML
    private AnchorPane thisAnchorPane;

    public void setService(SocialNetworking service){
            this.service = service;
            initModel();
            service.addObserver(this);
        }

    @Override
    public void ChangeScene(ActionEvent actionEvent) throws IOException {
        SceneSwitch.SceneSwitchAction(thisAnchorPane,"friendsAdmin-view.fxml",service);
    }

    private void initModel() {
        service.get_all_users().forEach(model::add);
        tableView.setItems(model);
    }

    @FXML
    public void initialize() {
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User,String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User,String>("lastName"));
        tableView.setItems(model);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateTextFields(newValue);
            }
        });

    }

    @Override
    public void update(AccountEvent event) {
        if(event.getEvent() == ChangeEvent.ADD)
            model.add(event.getData());
        if(event.getEvent() == ChangeEvent.DELETE)
            model.remove(event.getData());
        if(event.getEvent() == ChangeEvent.UPDATE)
            model.set(model.indexOf(event.getOldData()),
                    event.getData());
    }

    @FXML
    private void AddUser(ActionEvent actionEvent) throws  IOException{
        Stage addStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("addUser-view.fxml"));

        StackPane userAddLayout = fxmlLoader.load();
        addStage.setScene(new Scene(userAddLayout,300,500));
        addStage.setTitle("Add User");
        addStage.setResizable(false);

        AddUserController userController = fxmlLoader.getController();
        userController.setService(service);

        addStage.show();
    }

    @FXML
    private void DeleteUser(ActionEvent actionEvent){
        try {
            User selectedItem = tableView.getSelectionModel().getSelectedItem();
            if(selectedItem != null)
             service.delete_user(selectedItem.getId().toString());
        } catch (ValidationException e){
            AlterCreator.create(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    private void populateTextFields(User value){
        firstNameField.setText(value.getFirstName());
        lastNameField.setText(value.getLastName());
        UsernameField.setText(value.getUsername());
    }

    @FXML
    private void UpdateUser(ActionEvent actionEvent){
        User selectItem = tableView.getSelectionModel().getSelectedItem();
        if(selectItem != null)
            try {
                service.update_user(selectItem.getId().toString(), firstNameField.getText(), lastNameField.getText(),
                        selectItem.getPassword(),UsernameField.getText());
            } catch (ValidationException e){
                AlterCreator.create(Alert.AlertType.ERROR ,e.getMessage());
            }
        else
            AlterCreator.create(Alert.AlertType.INFORMATION,"Select an account!");
    }

}