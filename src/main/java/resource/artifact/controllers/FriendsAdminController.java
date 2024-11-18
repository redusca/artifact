package resource.artifact.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import resource.artifact.domains.Friendship;
import resource.artifact.domains.Tuple;
import resource.artifact.domains.validators.ValidationException;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.events.AccountEvent;
import resource.artifact.utils.events.ChangeEvent;
import resource.artifact.utils.fx.AlterCreator;
import resource.artifact.utils.fx.SceneSwitch;
import resource.artifact.utils.observers.Observable;
import resource.artifact.utils.observers.Observer;

import java.io.IOException;

public class FriendsAdminController implements SceneChangerController, Observer<AccountEvent> {
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
        service.addObserver(this);
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
         SceneSwitch.SceneSwitchAction(thisAnchorPane,"userAdmin-view.fxml",service);
    }

    public void DelFriendship(ActionEvent actionEvent) {
        try {
            Tuple<Long,Long> idDel = tableView.getSelectionModel().getSelectedItem().getId();
            service.del_friendship(idDel.first().toString(),idDel.last().toString());
        } catch (ValidationException e){
            AlterCreator.create(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    @Override
    public void update(AccountEvent actionEvent) {
        if(actionEvent.getEvent() == ChangeEvent.REMOVED_FRIENDSHIP)
            model.remove(actionEvent.getFriendship());
    }
}
