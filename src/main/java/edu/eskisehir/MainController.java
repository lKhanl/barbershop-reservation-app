package edu.eskisehir;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    public void login(ActionEvent event) {
        System.out.println("login");
    }

    //Open new frame without closing old
    public void openRegisterLogin(MouseEvent mouseEvent) throws IOException {
        Stage stage = Main.openNewStage("Register", MainController.class);
        //
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node)mouseEvent.getSource()).getScene().getWindow());
        //
        stage.setTitle("New Customer Screen!");
        stage.setResizable(false);
        stage.show();
    }

    public void openAdminLogin(ActionEvent event) throws IOException {
        Stage stage = Main.openNewStage("Admin",MainController.class);
        //
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        //
        stage.setTitle("Hello Admin!");
        stage.setResizable(false);
        stage.show();
    }

}
