module edu.eskisehir {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.eskisehir to javafx.fxml;
    exports edu.eskisehir;
}