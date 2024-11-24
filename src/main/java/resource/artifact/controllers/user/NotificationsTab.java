package resource.artifact.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import resource.artifact.domains.FriendRequest;
import resource.artifact.domains.User;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.events.AccountEvent;
import resource.artifact.utils.events.ChangeEvent;
import resource.artifact.utils.observers.Observer;

import static java.lang.Long.parseLong;


public class NotificationsTab implements Observer<AccountEvent> {

    @FXML
    private VBox tab;
    private User user;
    private SocialNetworking service;
    private int colorCode = 1;

    @Override
    public void update(AccountEvent accountEvent) {
        if(accountEvent.getEvent().equals(ChangeEvent.SEND_FRIENDREQUEST)){
          service.get_all_friend_requests().stream()
                  .filter(friendRequest -> friendRequest.getReceiver().equals(user.getId()) &&
                          friendRequest.getSender().equals(accountEvent.getData().getId()) &&
                          friendRequest.getStatus().equals(false))
                  .forEach(this::addNotification);
        }
    }

    public void setUser(User user) {
        this.user = user;
        initView();
    }

    private void initView() {
        service.get_all_friend_requests().stream()
                .filter(friendRequest -> friendRequest.getReceiver().equals(user.getId()) && friendRequest.getStatus().equals(false))
                .forEach(this::addNotification);
    }

    private void addNotification(FriendRequest friendRequest) {
        HBox hBox = new HBox();
        Label notification_text = new Label("Friend request from " +
                service.find_user(friendRequest.getSender()).orElse(new User()).toString() );
        notification_text.setMinWidth(400);
        hBox.getChildren().add(notification_text);
        Button delete = new Button("Delete");

        delete.setOnAction(actionEvent -> {
            tab.getChildren().remove(hBox);
            service.update_friendRequest(friendRequest);
        });

        if(colorCode == 1)
            hBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: #b3b3fa;");
        else
            hBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: #b5e2f1;");

        colorCode = (colorCode+1)%2;

        hBox.getChildren().add(delete);
        tab.getChildren().add(hBox);
    }

    public void setService(SocialNetworking service) {
        this.service = service;
        service.addObserver(this);
    }

}
