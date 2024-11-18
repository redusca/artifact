package resource.artifact.utils.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import resource.artifact.MainApplication;
import resource.artifact.controllers.SceneChangerController;
import resource.artifact.services.SocialNetworking;

import java.io.IOException;
import java.util.Objects;

public class SceneSwitch {
    public static void SceneSwitchAction(AnchorPane curentAnchorPane, String fxml, SocialNetworking service) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(MainApplication.class.getResource(fxml)));
        AnchorPane nextAnchorPane = fxmlLoader.load();

        curentAnchorPane.getChildren().removeAll();
        curentAnchorPane.getChildren().setAll(nextAnchorPane);

        SceneChangerController controller = fxmlLoader.getController();
        controller.setService(service);
    }
}
