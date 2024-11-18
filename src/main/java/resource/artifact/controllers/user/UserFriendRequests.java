package resource.artifact.controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import resource.artifact.domains.User;
import resource.artifact.domains.validators.ValidationException;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.events.AccountEvent;
import resource.artifact.utils.events.ChangeEvent;
import resource.artifact.utils.fx.AlterCreator;
import resource.artifact.utils.observers.Observer;

import java.util.Objects;

public class UserFriendRequests implements Observer<AccountEvent> {
    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User,String> tableColumnUsername;
    @FXML
    private TableColumn<User,String> tableColumnLastName;
    @FXML
    private TableColumn<User,String> tableColumnFirstName;

    private SocialNetworking service;
    private User user;

    ObservableList<User> model = FXCollections.observableArrayList();

    public void setService(SocialNetworking service) {
        this.service =service;
        service.addObserver(this);
        updateTable();
    }

    private void updateTable() {
        service.get_all_friendrequests_of_user(user).forEach(friendRequest ->
                model.add(service.find_user(friendRequest.getSender()).orElse(new User()))
        );
    }

    public void initialize() {
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableView.setItems(model);
        addButtonToTable("Accept_Friend","Accept");
        addButtonToTable("Decline_Friend","Decline");
    }

    private void addButtonToTable(String columnName,String buttonName) {
        TableColumn<User, Void> tableColumnAction = new TableColumn<>(columnName);

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<>() {

                    private final Button btn = new Button(buttonName);
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try {
                                User wantedUser = getTableView().getItems().get(getIndex());

                                if(Objects.equals(buttonName, "Accept"))
                                    service.add_friendship(wantedUser.getId().toString(), user.getId().toString());


                                service.del_friendRequest(wantedUser.getId().toString(),user.getId().toString());

                            } catch (ValidationException e) {
                                AlterCreator.create(Alert.AlertType.ERROR, e.getMessage());
                            }
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {

                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        tableColumnAction.setCellFactory(cellFactory);
        tableColumnAction.setPrefWidth(200D);
        tableView.getColumns().add(tableColumnAction);
    }


    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void update(AccountEvent accountEvent) {
        if(accountEvent.getEvent() == ChangeEvent.REMOVED_FRIENDREQUEST || accountEvent.getEvent() == ChangeEvent.ADD_FRIENDSHIP )
            model.remove(accountEvent.getData());
        if(accountEvent.getEvent() == ChangeEvent.SEND_FRIENDREQUEST)
            model.add(accountEvent.getData());
    }
}
