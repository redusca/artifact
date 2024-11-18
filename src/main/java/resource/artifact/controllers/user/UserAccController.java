package resource.artifact.controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import resource.artifact.MainApplication;
import resource.artifact.domains.User;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.events.AccountEvent;
import resource.artifact.utils.events.ChangeEvent;
import resource.artifact.utils.observers.Observer;

import java.io.IOException;

public class UserAccController implements Observer<AccountEvent> {

    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User,String> tableColumnUsername;
    @FXML
    private TableColumn<User,String> tableColumnFirstName;
    @FXML
    private TableColumn<User,String> tableColumnLastName;
    @FXML
    private Label usernameLabel;

    ObservableList<User> model = FXCollections.observableArrayList();

    private SocialNetworking service;
    private User user;

    public void setService(SocialNetworking service){
        this.service = service;
        service.addObserver(this);

    }

    public void setUser(User user) {
        this.user = user;
        usernameLabel.setText(user.getUsername());
        initModel();
    }

    private void initModel() {
        service.get_all_friends_of_User(user.getId().toString()).forEach(model::add);
        tableView.setItems(model);
    }

    @FXML
    public void initialize() {
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User,String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User,String>("lastName"));
        tableView.setItems(model);

    }

    @FXML
    private void deleteFriendship(ActionEvent actionEvent) {
        User selectItem = tableView.getSelectionModel().getSelectedItem();
        if(selectItem!=null){
            service.del_friendship(user.getId().toString(),selectItem.getId().toString());
        }
    }

    @Override
    public void update(AccountEvent actionEvent) {

        if(actionEvent.getEvent()== ChangeEvent.REMOVED_FRIENDSHIP){
            User foundUser = service.find_user(actionEvent.getFriendship().getFirst()).orElse(new User());
            if(foundUser == user)
                foundUser = service.find_user(actionEvent.getFriendship().getLast()).orElse(new User());

            model.remove(foundUser);
        }
        if(actionEvent.getEvent()==ChangeEvent.ADD_FRIENDSHIP){
            User foundUser = service.find_user(actionEvent.getFriendship().getFirst()).orElse(new User());
            if(foundUser == user)
                foundUser = service.find_user(actionEvent.getFriendship().getLast()).orElse(new User());

            model.add(foundUser);
        }
    }

    @FXML
    public void sendFriendRequest(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("sendFR-view.fxml"));
        Stage stage = new Stage();
        Pane userLayout = fxmlLoader.load();
        stage.setScene(new Scene(userLayout));

        SendFriendRequestController sendFriendRequestController = fxmlLoader.getController();

        sendFriendRequestController.setUser(user);
        sendFriendRequestController.setService(service);


        stage.setTitle("Send Friend Request");
        stage.show();

    }

    @FXML
    public void showFriendRequests(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("friendRequests-view.fxml"));
        Stage stage = new Stage();
        Pane userLayout = fxmlLoader.load();
        stage.setScene(new Scene(userLayout));

        UserFriendRequests friendRequestsController = fxmlLoader.getController();
        friendRequestsController.setUser(user);
        friendRequestsController.setService(service);

        stage.setTitle("Friend Requests");
        stage.show();
    }
}
