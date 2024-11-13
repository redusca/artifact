module resource.artifact {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires org.postgresql.jdbc;

    exports resource.artifact;
    opens resource.artifact to javafx.fxml;
    exports resource.artifact.controllers;
    opens resource.artifact.controllers to javafx.fxml;
    //exports resource.artifact.GUI;
    //opens resource.artifact.GUI to javafx.fxml;

}