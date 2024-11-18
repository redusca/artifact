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

public class SendFriendRequestController implements Observer<AccountEvent> {
    @FXML
    private TableColumn<User,String> tableColumnUsername;
    @FXML
    private TableColumn<User,String> tableColumnFirstName;
    @FXML
    private TableColumn<User,String> tableColumnLastName;
    @FXML
    private TableView<User> tableView;
    @FXML
    private TextField searchField;
    private SocialNetworking service;
    private User user;

    ObservableList<User> model = FXCollections.observableArrayList();

    public void setService(SocialNetworking service) {
        this.service =service;
        service.addObserver(this);
        model.addAll(service.get_non_friend_of_user(user));
    }

    public void setUser(User user) {
        this.user = user;
    }


    @FXML
    public void initialize() {
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableView.setItems(model);
        addButtonToTable();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> findByNameUser(newValue));

    }

    private void findByNameUser(String newValue) {
        model.clear();
        service.get_non_friend_of_user(user).stream().filter(user -> user.toString().contains(newValue)).forEach(model::add);
    }

    private void addButtonToTable() {
        TableColumn<User, Void> tableColumnAction = new TableColumn<>("Friend Request");

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Send");

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            User wantedUser = getTableView().getItems().get(getIndex());
                            btn.setDisable(service.friendRequestExists(user.getId().toString(), wantedUser.getId().toString()));
                            btn.setOnAction((ActionEvent event) -> {
                                try {
                                    System.out.println(service.send_friendRequest(user.getId().toString(), wantedUser.getId().toString()));
                                    btn.setDisable(true);
                                } catch (ValidationException e) {
                                    AlterCreator.create(Alert.AlertType.ERROR, e.getMessage());
                                }
                            });
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

    @Override
    public void update(AccountEvent actionEvent) {
            if(actionEvent.getEvent() == ChangeEvent.SEND_FRIENDREQUEST ||
               actionEvent.getEvent() == ChangeEvent.REMOVED_FRIENDSHIP ||
               actionEvent.getEvent() == ChangeEvent.REMOVED_FRIENDREQUEST
            )
               findByNameUser(searchField.getText());
    }
}
