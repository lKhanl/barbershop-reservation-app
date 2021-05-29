package edu.eskisehir;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReservationController implements Initializable {

    public Pane card;
    public TextField txtName;
    public TextField txtSurname;
    public TextField txtEmail;
    public PasswordField txtPass;
    public Button btnSave;
    public Label lblConsoleProfile;

    int cid;
    DataBaseOperations db = new DataBaseOperations();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        combo.setCellFactory();
    }

    public void save(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResource("media/error.png")).toString()));
        alert.setTitle("Warning!");
        alert.setHeaderText("Do you really want to save?");
        if (txtName.getText().equals("") || txtSurname.getText().equals("") || txtEmail.getText().equals("") || txtPass.getText().equals("")) {
            lblConsoleProfile.setTextFill(Color.web("#f84040"));
            lblConsoleProfile.setText("Some fields are empty!");
            return;
        }

        Pattern p = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        Matcher m = p.matcher(txtEmail.getText());
        if (!m.find()) {
            lblConsoleProfile.setTextFill(Color.web("#f84040"));
            lblConsoleProfile.setText("Invalid email!");
        } else {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                db.updateCustomer(Attribute.NAME, txtName.getText(), cid);
                db.updateCustomer(Attribute.SURNAME, txtSurname.getText(), cid);
                db.updateCustomer(Attribute.EMAIL, txtEmail.getText(), cid);
                db.updateCustomer(Attribute.PASSWORD, txtPass.getText(), cid);
                lblConsoleProfile.setTextFill(Color.web("#42ba96"));
                lblConsoleProfile.setText("Saved!");
                txtPass.setText("");
            } else {
                event.consume();
            }
        }

    }
}
