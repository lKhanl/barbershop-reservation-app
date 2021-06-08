package edu.eskisehir.controllers;

import edu.eskisehir.entity.Customer;
import edu.eskisehir.db.DataBaseOperations;
import edu.eskisehir.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.net.MalformedURLException;
import java.nio.file.Paths;

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

    public void login(ActionEvent event){
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

    private void abstractLogin(Node nodee)   {
        Customer customer = db.logIn(txtEmail.getText(), txtPass.getText());

        if (customer == null) {
            lblConsole.setText("Email is not registered");
        } else if (!customer.getPassword().equals(txtPass.getText())) {
            lblConsole.setText("Password is not matched");
        } else {
            Node node = nodee;
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();

            cid = customer.getId();

            FXMLLoader fxmlLoader = null;
            try {
                fxmlLoader = new FXMLLoader(Paths.get("src/main/resources/edu/eskisehir/fxml/Reservation.fxml").toUri().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage newStage = new Stage();
            Scene newScene = new Scene(root);
            newStage.setScene(newScene);
            newStage.getIcons().add(new Image(Paths.get("src/main/resources/edu/eskisehir/media/res.jpg").toUri().toString()));
            newStage.setTitle("Welcome " + customer.getName() + " " + customer.getSurname());
            newStage.setResizable(false);

            ReservationController ctrl = fxmlLoader.getController();
            ctrl.txtName.setText(customer.getName());
            ctrl.txtSurname.setText(customer.getSurname());
            ctrl.txtEmail.setText(customer.getEmail());

            newStage.show();
        }

    }
}
