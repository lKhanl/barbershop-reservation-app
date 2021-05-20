package edu.eskisehir;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {
    public Pane mainPane;
    public Label lblUserName;
    public Label lblPassword;
    public TextField txtUserName;
    public PasswordField txtPassword;
    public Button btnLogin;
    public AnchorPane ap;

    public void login(ActionEvent event) throws IOException {
        DataBaseOperations db = new DataBaseOperations();

        txtUserName.setText("admin");
        txtPassword.setText("admin");

        String adminUserName = txtUserName.getText();
        String adminPass = txtPassword.getText();



        System.out.println(adminUserName + " " + adminPass);

        if (db.checkAdmin(adminUserName, adminPass)) {

            Stage adminStg = (Stage) ((Node) event.getSource()).getScene().getWindow();
            adminStg.close();
            ((Stage)adminStg.getOwner()).close();

            Stage stage = Main.openNewStage("AdminScreen", AdminScreenController.class,"admin.png");
            stage.setTitle("Admin Screen");
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong username or password!");
            alert.show();
        }
    }
}
