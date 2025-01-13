package resource.artifact;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import resource.artifact.controllers.login.UserLoginController;
import resource.artifact.domains.DataBaseConnectInfo;
import resource.artifact.domains.Message;
import resource.artifact.domains.User;
import resource.artifact.domains.validators.FriendRequestValidator;
import resource.artifact.domains.validators.FriendshipValidator;
import resource.artifact.domains.validators.MessageValidator;
import resource.artifact.domains.validators.UserValidator;
import resource.artifact.repositories.database.FriendRequestDBRepository;
import resource.artifact.repositories.database.FriendshipDBRepository;
import resource.artifact.repositories.database.MessageDBRepository;
import resource.artifact.repositories.database.UserDBRepository;
import resource.artifact.repositories.fromdatabase.UserFDBRepository;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.Encryption;

import java.io.IOException;
import java.time.LocalDateTime;

public class MainApplication extends Application {
    private SocialNetworking userService;

    @Override
    public void start(Stage stage) throws IOException {
        DataBaseConnectInfo infoConnect =new DataBaseConnectInfo("postgres","1231","socialcomunity");

        UserFDBRepository userDBRepository = new UserFDBRepository(
                new UserValidator(),infoConnect
        );

        FriendshipDBRepository friendshipDBRepository = new FriendshipDBRepository(
                userDBRepository, new FriendshipValidator(userDBRepository),
                infoConnect , "friendships"
        );
        FriendRequestDBRepository friendRequestDBRepository =new FriendRequestDBRepository(
                new FriendRequestValidator(userDBRepository),infoConnect,"friendrequests"
        );
        MessageDBRepository messageDBRepository = new MessageDBRepository(
                new MessageValidator(userDBRepository),infoConnect,"messages",userDBRepository
        );
        messageDBRepository.findAll().forEach(System.out::println);
        //Service
        userService = new SocialNetworking(userDBRepository,friendshipDBRepository,friendRequestDBRepository,messageDBRepository);
        initView(stage);
        stage.setTitle("Login Page");
        stage.show();
    }

    private void initView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("userLogin-view.fxml"));

        StackPane userLayout = fxmlLoader.load();
        stage.setScene(new Scene(userLayout));
        stage.setResizable(false);

        UserLoginController userController = fxmlLoader.getController();
        userController.setService(userService);
    }

    public static void main(String[] args) {
        launch();
    }
}