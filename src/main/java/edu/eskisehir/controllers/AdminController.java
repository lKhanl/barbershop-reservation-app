package edu.eskisehir.controllers;

import edu.eskisehir.db.DataBaseOperations;
import edu.eskisehir.Main;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {
    public TextField txtUserName;
    public PasswordField txtPassword;
    public Button btnLogin;
    public AnchorPane mainPane;

    public void login(ActionEvent event) {
        Node node = (Node) event.getSource();
        abstractLogin(node);
    }

    public void enterLogin(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            Node node = (Node) keyEvent.getSource();
            abstractLogin(node);
        }
    }

    private void abstractLogin(Node nodee) {
        DataBaseOperations db = new DataBaseOperations();

        String adminUserName = txtUserName.getText();
        String adminPass = txtPassword.getText();


        if (db.checkAdmin(adminUserName, adminPass)) {

            Stage adminStg = (Stage) (nodee).getScene().getWindow();
            adminStg.close();
            ((Stage) adminStg.getOwner()).close();

            Stage stage = null;
            try {
                stage = Main.openNewStage("AdminScreen", "admin.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setTitle("Admin Screen");
            stage.setResizable(false);
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong username or password!");
            alert.show();
        }
    }
}
