package edu.eskisehir;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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

    private final DataBaseOperations dataBaseOperations = new DataBaseOperations();

    public void register(ActionEvent actionEvent) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        Matcher m = p.matcher(txtEmail.getText());
        if (!m.find()) {
            //Warning w = new Warning("Geçersiz email", actionEvent);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid email address!");
            alert.show();
            txtEmail.setText("@");
        } else {

            if (!txtName.getText().equals("") && !txtSurname.getText().equals("") && !txtEmail.getText().equals("") && !txtPassword.getText().equals("")) {
                System.out.println(txtName.getText());
                System.out.println(txtSurname.getText());
                System.out.println(txtEmail.getText());
                System.out.println(txtPassword.getText());
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("All sections should be filled!");
                alert.setTitle("Error");
                alert.show();
            }
        }
        dataBaseOperations.addCustomer(txtName.getText(), txtSurname.getText(), txtEmail.getText(), txtPassword.getText());

    }
}
