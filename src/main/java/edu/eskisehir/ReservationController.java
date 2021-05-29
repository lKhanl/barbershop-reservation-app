package edu.eskisehir;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {

    public Label lbl;
    public DatePicker date;
    public ComboBox<Barber> combo;

    DataBaseOperations db = new DataBaseOperations();

    public void get(ActionEvent event) {
//        System.out.println(date.toString());
        System.out.println(date.getValue());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        combo.setCellFactory();
    }
}
