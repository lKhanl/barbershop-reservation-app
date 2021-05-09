package edu.eskisehir;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
@Deprecated
public class PrimaryController {

    public Button primaryButton;

    @FXML
    private void switchToSecondary() throws IOException {
        Main.setRoot("secondary");
    }
}
