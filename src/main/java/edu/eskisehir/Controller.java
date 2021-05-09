package edu.eskisehir;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

@Deprecated
public class Controller {
    public Pane loginPane;
    public Label labelEmail;
    public Label lblPassword;
    public Pane mainPane;
    public PasswordField passwordField;
    public TextField emailTF;
    public Label lblCustomer;
    public Button btnLogin;
    public Label lblTmp;
    public Label lblRegister;
    public Button btnAdminLogin;

    //Open new frame without closing old
    public void openRegisterWindow(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../../../../Desktop/DB/src/main/resources/edu/eskisehir/Register.fxml"));
        Stage stage = new Stage();
        stage.setOnCloseRequest(e -> stage.close());
        stage.setTitle("Register Screen");
        //
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        //
        Scene second = new Scene(root);
        stage.setScene(second);
        stage.setResizable(false);
        stage.show();
    }

    public void openAdminWindow(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../../../../Desktop/DB/src/main/resources/edu/eskisehir/Admin.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Admin Register Screen");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        Scene second = new Scene(root);
        stage.setScene(second);
        stage.setResizable(false);
        stage.show();
    }
}
