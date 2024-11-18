package resource.artifact.controllers;

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
import resource.artifact.domains.Account;
import javafx.event.ActionEvent;
import resource.artifact.domains.validators.ValidationException;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.AlterCreator;
import resource.artifact.utils.SceneSwitch;
import resource.artifact.utils.events.AccountEvent;
import resource.artifact.utils.events.ChangeEvent;
import resource.artifact.utils.observers.Observer;

import java.io.IOException;

public class UsersAdminController implements SceneChangerController, Observer<AccountEvent> {
    private SocialNetworking service;
    ObservableList<Account> model = FXCollections.observableArrayList();

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField UsernameField;
    @FXML
    private Label message;
    @FXML
    private TableView<Account> tableView;
    @FXML
    TableColumn<Account,String> tableColumnFirstName;
    @FXML
    TableColumn<Account,String> tableColumnLastName;
    @FXML
    TableColumn<Account,String> tableColumnUsername;
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
        service.get_all_Accounts().forEach(model::add);
        tableView.setItems(model);
    }

    @FXML
    public void initialize() {
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<Account,String>("username"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<Account,String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<Account,String>("lastName"));
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
            model.add(event.getACC());
        if(event.getEvent() == ChangeEvent.DELETE)
            model.remove(event.getACC());
        if(event.getEvent() == ChangeEvent.UPDATE)
            model.set(model.indexOf(event.getOldACC()),
                    event.getACC());
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
            service.del_Account(tableView.getSelectionModel().getSelectedItem().getUser().getId().toString());
        } catch (ValidationException e){
            AlterCreator.create(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    private void populateTextFields(Account value){
        firstNameField.setText(value.getFirstName());
        lastNameField.setText(value.getLastName());
        UsernameField.setText(value.getUsername());
    }

    @FXML
    private void UpdateUser(ActionEvent actionEvent){
        Account selectItem = tableView.getSelectionModel().getSelectedItem();
        if(selectItem != null)
            try {
                service.update_user(selectItem.getUser().getId().toString(), firstNameField.getText(), lastNameField.getText());
                service.update_Account(UsernameField.getText(), selectItem.getPassword(), selectItem.getUser().getId().toString());
            } catch (ValidationException e){
                AlterCreator.create(Alert.AlertType.ERROR ,e.getMessage());
            }
        else
            AlterCreator.create(Alert.AlertType.INFORMATION,"Select an account!");
    }

}