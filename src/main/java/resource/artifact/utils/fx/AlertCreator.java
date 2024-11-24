package resource.artifact.utils.fx;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertCreator {
    public static void create(Alert.AlertType type, String message){
            Alert alert = new Alert(type,message,new ButtonType("exit"));
            alert.setTitle("Validation Error");
            alert.show();
    }
}

