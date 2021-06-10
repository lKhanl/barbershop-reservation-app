package edu.eskisehir.controllers;

import edu.eskisehir.entity.Customer;
import edu.eskisehir.db.DataBaseOperations;
import edu.eskisehir.Main;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;

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
    static int cid;

    public void login(ActionEvent event) {
        Node node = (Node) event.getSource();
        abstractLogin(node);
    }

    //Open new frame without closing old
    public void openRegisterLogin(MouseEvent mouseEvent) throws IOException {
        Stage stage = Main.openNewStage("Register", "welcome.png");
        //
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());
        //
        stage.setTitle("New Customer Screen!");
        stage.setResizable(false);
        stage.show();
    }

    public void openAdminLogin(ActionEvent event) throws IOException {
        Stage stage = Main.openNewStage("Admin", "admin.png");
        //
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        //
        stage.setTitle("Hello Admin!");
        stage.setResizable(false);
        stage.show();
    }

    public void pauseAndPlay(ActionEvent event) {
        Main.pauseAndPlay();
    }

    public void enterLogin(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            Node node = (Node) keyEvent.getSource();
            abstractLogin(node);
        }
    }

    private void abstractLogin(Node nodee) {
        Customer customer = db.logIn(txtEmail.getText());

        if (customer == null) {
            lblConsole.setText("Email is not registered");
        } else if (!customer.getPassword().equals(db.getMd5(txtPass.getText()))) {
            lblConsole.setText("Password is not matched");
        } else {
            Stage stage = (Stage) nodee.getScene().getWindow();
            Image img = null;
            cid = customer.getId();
            try {
                Main.setRoot("Reservation");
                img = new Image(new FileInputStream("src/main/resources/edu/eskisehir/media/res.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.getIcons().clear();
            stage.getIcons().add(img);
            stage.setTitle("Welcome " + customer.getName() + " " + customer.getSurname());
            stage.setWidth(725);
            stage.setHeight(750);

        }
    }
}
