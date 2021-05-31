module edu.eskisehir {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires javafx.swing;
    requires javafx.graphics;
    requires javafx.media;

    opens edu.eskisehir to javafx.fxml;
    exports edu.eskisehir;
    exports edu.eskisehir.redundant;
    opens edu.eskisehir.redundant to javafx.fxml;
    exports edu.eskisehir.controllers;
    opens edu.eskisehir.controllers to javafx.fxml;
    exports edu.eskisehir.entity;
    opens edu.eskisehir.entity to javafx.fxml;
    exports edu.eskisehir.db;
    opens edu.eskisehir.db to javafx.fxml;
}