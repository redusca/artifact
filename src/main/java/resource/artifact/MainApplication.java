package resource.artifact;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import resource.artifact.controllers.UserGUIController;
import resource.artifact.domains.DataBaseConnectInfo;
import resource.artifact.domains.validators.FriendshipValidator;
import resource.artifact.domains.validators.UserValidator;
import resource.artifact.repositories.database.FriendshipDBRepository;
import resource.artifact.repositories.database.UserDBRepository;
import resource.artifact.services.SocialNetworking;
import resource.artifact.utils.CommunityStructureUtils;

import java.io.IOException;

public class MainApplication extends Application {
    private SocialNetworking userService;

    @Override
    public void start(Stage stage) throws IOException {
        DataBaseConnectInfo infoConnect =new DataBaseConnectInfo("postgres","1231","socialcomunity");

        UserDBRepository userDBRepository = new UserDBRepository(
                new UserValidator(),infoConnect,
                "users"
        );

        FriendshipDBRepository friendshipDBRepository = new FriendshipDBRepository(
                userDBRepository, new FriendshipValidator(userDBRepository),
                infoConnect , "friendships"
        );
        //Service
        userService = new SocialNetworking(userDBRepository,friendshipDBRepository);

        initView(stage);
        stage.setTitle("Social Network");
        stage.show();

    }

    private void initView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        stage.setScene(new Scene(userLayout));

        UserGUIController userController = fxmlLoader.getController();
        userController.setService(userService);
    }

    public static void main(String[] args) {
        launch();
    }
}