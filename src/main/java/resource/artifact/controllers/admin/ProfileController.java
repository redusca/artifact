package resource.artifact.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import resource.artifact.MainApplication;
import resource.artifact.controllers.SceneChangerController;
import resource.artifact.domains.User;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.events.AccountEvent;
import resource.artifact.utils.observers.Observable;
import resource.artifact.utils.observers.Observer;
import resource.artifact.utils.page.Pageable;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class ProfileController implements Observer<AccountEvent>{

    private User currUser, profileUser;
    private SocialNetworking service;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private ImageView profileImageView;
    @FXML
    private Label friendsCountLabel;
    @FXML
    private Button uploadImageButton;
    @FXML
    private Button changeUsernameButton;
    @FXML
    private TextField newUsernameField;
    @FXML
    private Button saveUsernameButton;

    public void setUser(User user)
    {
        profileUser = user;
        usernameLabel.setText("Username: " + user.getUsername());
        firstNameLabel.setText("First Name: " + user.getFirstName());
        lastNameLabel.setText("Last Name: " + user.getLastName());

    }

    public void setCurrUser(User user) {
        currUser = user;
    }

    public void setService(SocialNetworking service) {
        this.service = service;
        init();
    }

    private void init() {
        updateFriendsCount();
        if (currUser.equals(profileUser)) {
            changeUsernameButton.setVisible(true);
        }
        loadImage();
    }

    private void loadImage() {
        String username = profileUser.getUsername();
        username = username.replace(" ", "_");
        String imagePath = "images/" + username + ".jpg";
        URL imageUrl = MainApplication.class.getResource(imagePath);
        if (imageUrl != null) {
            profileImageView.setImage(new Image(imageUrl.toString()));
        } else {
            profileImageView.setImage(new Image(String.valueOf(MainApplication.class.getResource("images/default.jpg"))));
        }
    }

    private void updateFriendsCount() {
        int friendsCount = service.get_all_friends_of_User(
                profileUser.getId().toString(), new Pageable(0,Integer.MAX_VALUE))
                        .getTotalNumberOfElements();
        friendsCountLabel.setText("Number of Friends: " + friendsCount);
    }

    @FXML
    private void uploadImage() throws IOException, URISyntaxException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            String username = profileUser.getUsername().replace(" ", "_");
            Path destinationPath = Paths.get(Objects.requireNonNull(MainApplication.class.getResource("images")).toURI())
                    .resolve(username + ".jpg");
            Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            Image image = new Image(selectedFile.toURI().toString());
            profileImageView.setImage(image);
        }
    }

    @FXML
    private void openChangeUsernameBox() {
        newUsernameField.setVisible(true);
        saveUsernameButton.setVisible(true);
    }

    @FXML
    private void saveUsername() {
        String newUsername = newUsernameField.getText();
        if (!newUsername.isEmpty()) {
            profileUser.setUsername(newUsername);
            service.update_user(profileUser.getId().toString(),
                    profileUser.getFirstName(), profileUser.getLastName(),
                    profileUser.getPassword(), newUsername);

            usernameLabel.setText("Username: " + newUsername);
        }
        newUsernameField.clear();
        newUsernameField.setVisible(false);
        saveUsernameButton.setVisible(false);
    }


    @Override
    public void update(AccountEvent accountEvent) {
        updateFriendsCount();
        usernameLabel.setText("Username: " + profileUser.getUsername());
    }
}