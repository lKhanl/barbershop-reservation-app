module edu.eskisehir {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires javafx.swing;

    opens edu.eskisehir to javafx.fxml;
    exports edu.eskisehir;
}