package resource.artifact.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import resource.artifact.domains.Friendship;
import resource.artifact.domains.User;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.SceneSwitch;

import java.io.IOException;
import java.time.LocalDateTime;

public class FriendsAdminController implements SceneChangerController{
    @FXML
    public AnchorPane thisAnchorPane;
    ObservableList<Friendship> model = FXCollections.observableArrayList();

    public SocialNetworking service;
    @FXML
    public TableView<Friendship> tableView;
    @FXML
    public TableColumn<Friendship,Long> tableFirstUser;
    @FXML
    public TableColumn<Friendship,Long>  tableSecondUser;
    @FXML
    public TableColumn<Friendship,String>  tableDate;

    @Override
    public void setService(SocialNetworking service) {
        this.service = service;
        initModel();
    }

    private void initModel() {
        service.get_all_friendships().forEach(model::add);
        tableView.setItems(model);
    }

    @FXML
    public void initialize() {
        tableFirstUser.setCellValueFactory(new PropertyValueFactory<Friendship,Long>("first"));
        tableSecondUser.setCellValueFactory(new PropertyValueFactory<Friendship,Long>("last"));
        tableDate.setCellValueFactory(new PropertyValueFactory<Friendship, String>("fDate"));
        tableView.setItems(model);
    }

    @Override
    public void ChangeScene(ActionEvent actionEvent) throws IOException {
        new SceneSwitch(thisAnchorPane,"userAdmin-view.fxml",service);
    }
}
