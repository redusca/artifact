package resource.artifact.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import resource.artifact.MainApplication;
import resource.artifact.domains.User;
import javafx.event.ActionEvent;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.SceneSwitch;
import resource.artifact.utils.events.ChangeEvent;
import resource.artifact.utils.events.UserOrFriendShipChangeEvent;
import resource.artifact.utils.observers.Observer;

import java.io.IOException;
import java.util.Optional;

public class UsersAdminController implements SceneChangerController, Observer<UserOrFriendShipChangeEvent> {

    private SocialNetworking service;
    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    private Label message;
    @FXML
    private TableView<User> tableView;
    @FXML
    TableColumn<User,String> tableColumnFirstName;
    @FXML
    TableColumn<User,String> tableColumnLastName;
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
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User,String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User,String>("lastName"));
        tableView.setItems(model);

    }

    @Override
    public void update(UserOrFriendShipChangeEvent userOrFriendChangeEvent) {
        if(userOrFriendChangeEvent.getEvent() == ChangeEvent.ADD)
            model.add(userOrFriendChangeEvent.getData());
        if(userOrFriendChangeEvent.getEvent() == ChangeEvent.DELETE)
            model.remove(userOrFriendChangeEvent.getData());
        if(userOrFriendChangeEvent.getEvent() == ChangeEvent.UPDATE)
            model.set(model.indexOf(userOrFriendChangeEvent.getOldData()),
                    userOrFriendChangeEvent.getData());
    }

    @FXML
    private void AddUser(ActionEvent actionEvent) throws  IOException{
        Stage addStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("addUser-view.fxml"));

        StackPane userAddLayout = fxmlLoader.load();
        addStage.setScene(new Scene(userAddLayout,300,200));
        addStage.setTitle("Add User");
        addStage.setResizable(false);

        AddUserController userController = fxmlLoader.getController();
        userController.setService(service);

        addStage.show();
    }

    @FXML
    private void DeleteUser(ActionEvent actionEvent){
        Optional<User> delUser = service.delete_user(tableView.getSelectionModel().getSelectedItem().getId().toString());
    }
}