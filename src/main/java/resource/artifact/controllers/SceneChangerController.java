package resource.artifact.controllers;

import javafx.event.ActionEvent;
import resource.artifact.services.SocialNetworking;

import java.io.IOException;

public interface SceneChangerController {

    public void setService(SocialNetworking service);

    public void ChangeScene(ActionEvent actionEvent) throws IOException ;
}
