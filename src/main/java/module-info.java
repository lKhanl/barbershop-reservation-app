module edu.eskisehir {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens edu.eskisehir to javafx.fxml;
    exports edu.eskisehir;
}