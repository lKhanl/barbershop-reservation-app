package edu.eskisehir;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

public class MainController {
    public AnchorPane mainPane;
    public Pane loginPane;
    public Label lblRegisterNow;
    public Button btnLogin;
    public Button btnAdmin;
    public TextField txtEmail;
    public PasswordField txtPass;
    public Label lblConsole;

    DataBaseOperations db = new DataBaseOperations();

    public void login(ActionEvent event) throws IOException {
        txtEmail.setText("a@a.com");
        txtPass.setText("a");

        Customer customer = db.logIn(txtEmail.getText(), txtPass.getText());

        if (customer == null) {
            lblConsole.setText("Email is not registered");
        } else if (!customer.getPassword().equals(txtPass.getText())) {
            lblConsole.setText("Password is not matched");
        } else {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(ReservationController.class.getResource("Reservation.fxml"));
            Parent root = fxmlLoader.load();
            Stage newStage = new Stage();
            Scene newScene = new Scene(root);
            newStage.setScene(newScene);

            ReservationController ctrl = fxmlLoader.getController();
            ctrl.txtName.setText(customer.getName());
            ctrl.txtSurname.setText(customer.getSurname());
            ctrl.txtEmail.setText(customer.getEmail());
            ctrl.cid = customer.getId();

            newStage.show();
        }


    }

    //Open new frame without closing old
    public void openRegisterLogin(MouseEvent mouseEvent) throws IOException {
        Stage stage = Main.openNewStage("Register", MainController.class, "welcome.png");
        //
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());
        //
        stage.setTitle("New Customer Screen!");
        stage.setResizable(false);
        stage.show();
    }

    public void openAdminLogin(ActionEvent event) throws IOException {
        Stage stage = Main.openNewStage("Admin", MainController.class, "admin.png");
        //
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        //
        stage.setTitle("Hello Admin!");
        stage.setResizable(false);
        stage.show();
    }

}
