package resource.artifact.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import resource.artifact.domains.User;
import resource.artifact.services.SocialNetworking;
import javafx.event.ActionEvent;

public class UserGUIController {
    private User user;
    private SocialNetworking service;
    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    private Label message;
    @FXML
    private TableView<User> tableView;
    @FXML
    TableColumn<User,String> tableColumnFirstName;
    @FXML
    TableColumn<User,String> tableColumnLastName;

    public void setUser(User user){
            this.user = user;
        }

    public void setService(SocialNetworking service){
            this.service = service;
            initModel();
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


    public void helloWorld(ActionEvent actionEvent){

        service.update_user("21","buci","mari");
    }

}