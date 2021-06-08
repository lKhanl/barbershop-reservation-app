package edu.eskisehir.controllers;

import edu.eskisehir.db.DataBaseOperations;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController {
    public Label lblName;
    public Label lblSurname;
    public Label lblEmail;
    public Label lblPassword;
    public TextField txtName;
    public TextField txtSurname;
    public TextField txtEmail;
    public PasswordField txtPassword;
    public Pane mainPane;
    public Button btnRegister;

    private final DataBaseOperations db = new DataBaseOperations();

    public void register(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        abstractRegister(node);
    }

    public void enterRegister(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            Node node = (Node) keyEvent.getSource();
            abstractRegister(node);
        }
    }

    private void abstractRegister(Node nodee) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        Matcher m = p.matcher(txtEmail.getText());
        if (!m.find()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid email address!");
            alert.show();
            txtEmail.setText("@");
        } else {
            if (!txtName.getText().equals("") && !txtSurname.getText().equals("") && !txtEmail.getText().equals("") && !txtPassword.getText().equals("")) {
                try {
                    db.addCustomer(txtName.getText(), txtSurname.getText(), txtEmail.getText(), txtPassword.getText());
                } catch (SQLException throwables) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("This e-mail address is already taken!");
                    alert.setTitle("Error");
                    alert.show();
                    return;
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Register is done!");
                alert.setTitle("Done");
                alert.show();
                Stage stage = (Stage) (nodee).getScene().getWindow();
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("All sections should be filled!");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}
