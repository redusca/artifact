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
    requires java.desktop;
    requires com.fasterxml.jackson.core;

    exports resource.artifact;
    opens resource.artifact to javafx.fxml;
    exports resource.artifact.controllers;
    opens resource.artifact.controllers to javafx.fxml;
    exports resource.artifact.domains;
    opens resource.artifact.domains to javafx.fxml;
    opens resource.artifact.services;
    exports resource.artifact.services to javafx.fxml;
    opens resource.artifact.utils.events;
    exports resource.artifact.utils.events to javafx.fxml;
    opens resource.artifact.utils.page;
    exports resource.artifact.utils.page to javafx.fxml;
    opens resource.artifact.repositories.inMemory;
    exports resource.artifact.repositories.inMemory to javafx.fxml;
    opens resource.artifact.utils.observers;
    exports resource.artifact.utils.observers to java.xml;
    opens resource.artifact.repositories;
    exports resource.artifact.repositories to java.xml;
    exports resource.artifact.controllers.admin;
    opens resource.artifact.controllers.admin to javafx.fxml;
    exports resource.artifact.controllers.user;
    opens resource.artifact.controllers.user to javafx.fxml;
    exports resource.artifact.controllers.login;
    opens resource.artifact.controllers.login to javafx.fxml;
    exports resource.artifact.domains.validators;
    opens resource.artifact.domains.validators to javafx.fxml;
    exports resource.artifact.repositories.fromdatabase;
    opens resource.artifact.repositories.fromdatabase to javafx.fxml;
}