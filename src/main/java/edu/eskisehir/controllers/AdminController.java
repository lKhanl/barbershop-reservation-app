package edu.eskisehir.controllers;

import edu.eskisehir.db.DataBaseOperations;
import edu.eskisehir.Main;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {
    public TextField txtUserName;
    public PasswordField txtPassword;
    public Button btnLogin;
    public AnchorPane mainPane;

    public void login(ActionEvent event) throws IOException {
        DataBaseOperations db = new DataBaseOperations();

//        txtUserName.setText("admin");
//        txtPassword.setText("admin");

        String adminUserName = txtUserName.getText();
        String adminPass = txtPassword.getText();

        System.out.println(adminUserName + " " + adminPass);

        if (db.checkAdmin(adminUserName, adminPass)) {

            Stage adminStg = (Stage) ((Node) event.getSource()).getScene().getWindow();
            adminStg.close();
            ((Stage)adminStg.getOwner()).close();

            Stage stage = Main.openNewStage("AdminScreen","admin.png");
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
