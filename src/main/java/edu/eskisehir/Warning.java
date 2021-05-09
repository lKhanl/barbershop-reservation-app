package edu.eskisehir;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Warning {
    public Warning(String message,ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../../../../../Desktop/DB/src/main/resources/edu/eskisehir/Warning.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);


            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage)((Node)event.getSource()).getScene().getWindow());

            Label node = (Label)scene.lookup("#lblMessage");
            node.setText(message);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
