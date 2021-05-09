package edu.eskisehir;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class AdminController {
    public Pane mainPane;
    public Label lblUserName;
    public Label lblPassword;
    public TextField txtUserName;
    public PasswordField txtPassword;
    public Button btnLogin;

    public void login(ActionEvent event) {
        System.out.println("loginnnnnnnnnnnnnnnn");
    }
}
