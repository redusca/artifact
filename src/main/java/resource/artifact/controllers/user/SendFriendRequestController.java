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
import resource.artifact.repositories.fromdatabase.UserFilterDTO;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.CommunityStructureUtils;
import resource.artifact.utils.events.AccountEvent;
import resource.artifact.utils.events.ChangeEvent;
import resource.artifact.utils.fx.AlertCreator;
import resource.artifact.utils.observers.Observer;
import resource.artifact.utils.page.Page;
import resource.artifact.utils.page.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    private int pageNumber=0;

    @FXML
    private Label pageLabel;
    @FXML
    private Button nextButton;
    @FXML
    private Button prevButton;

    ObservableList<User> model = FXCollections.observableArrayList();

    public void setService(SocialNetworking service) {
        this.service = service;
        service.addObserver(this);
        initModel("");
    }

    private void initModel(String name) {
        Page<User> page = service.get_non_friend_of_user(user, new Pageable(pageNumber,10),name);
        List<User> userList = StreamSupport.stream(page.getElementsOnPage().spliterator(), false)
                .collect(Collectors.toList());

        prevButton.setDisable(pageNumber==0);
        nextButton.setDisable(pageNumber>=(page.getTotalNumberOfElements()-1)/ 10);

        pageLabel.setText("Page "+(pageNumber+1)+" of "+( (page.getTotalNumberOfElements()-1)/ 10 +1) );

        model.setAll(userList);
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

        searchField.textProperty().addListener((observable, oldValue, newValue) -> initModel(searchField.getText()));

        tableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    setStyle("-fx-background-color: " + CommunityStructureUtils.generateColorCode(item.getUsername()) + ";");
                }
            }
        });

    }


    private void addButtonToTable() {
        TableColumn<User, Void> tableColumnAction = new TableColumn<>("Friend Request");

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                return new TableCell<>() {

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
                                    AlertCreator.create(Alert.AlertType.ERROR, e.getMessage());
                                }
                            });
                            setGraphic(btn);
                        }
                    }
                };
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
               initModel(searchField.getText());
    }

    public void prevPage(ActionEvent actionEvent) {
        pageNumber--;
        initModel(searchField.getText());
    }

    public void nextPage(ActionEvent actionEvent) {
        pageNumber++;
        initModel(searchField.getText());
    }
}
