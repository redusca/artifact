package resource.artifact.controllers;

import javafx.event.ActionEvent;
import resource.artifact.services.SocialNetworking;

import java.io.IOException;

public interface SceneChangerController {

    void setService(SocialNetworking service);

    void ChangeScene(ActionEvent actionEvent) throws IOException ;
}
