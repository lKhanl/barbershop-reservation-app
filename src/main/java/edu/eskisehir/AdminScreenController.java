package edu.eskisehir;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminScreenController implements Initializable {
    public TableView<Barber> barbersTable;
    public TableColumn<Barber, String> nameCol;
    public TableColumn<Barber, Integer> salaryCol;
    public TableColumn<Barber, String> surnameCol;
    public TextField txtName;
    public TextField txtSurname;
    public TextField txtSalary;
    public Button btnAdd;
    public Label lblConsole;
    public Button btnDelete;

    DataBaseOperations db = new DataBaseOperations();
    ObservableList<Barber> dataTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editableCols();
        loadData();
        //Accept only numbers for salary
        txtSalary.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtSalary.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void editableCols() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));

        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(e -> {
            Barber barber = e.getTableView().getItems().get(e.getTablePosition().getRow());
            barber.setName(e.getNewValue());
            db.updateBarber(Attribute.NAME, e.getNewValue(), barber.getId());
        });

        surnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameCol.setOnEditCommit(e -> {
            Barber barber = e.getTableView().getItems().get(e.getTablePosition().getRow());
            barber.setSurname(e.getNewValue());
            db.updateBarber(Attribute.SURNAME, e.getNewValue(), barber.getId());
        });

        salaryCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        salaryCol.setOnEditCommit(e -> {
            Barber barber = e.getTableView().getItems().get(e.getTablePosition().getRow());
            barber.setSalary(e.getNewValue());
            db.updateBarber(Attribute.SALARY, String.valueOf(e.getNewValue()), barber.getId());
        });

        barbersTable.setEditable(true);
    }

    private void loadData() {
        dataTable = FXCollections.observableArrayList();
        List<Barber> barbers = db.getBarbers();
        dataTable.addAll(barbers);
        barbersTable.setItems(dataTable);
    }

    public void addBarber(ActionEvent event) {

        if (!(txtName.getText().equals("") || txtSurname.getText().equals("") || txtSalary.getText().equals(""))) {
            int id = db.addBarber(txtName.getText(), txtSurname.getText(), Integer.parseInt(txtSalary.getText()));
            dataTable.add(new Barber(id, txtName.getText(), txtSurname.getText(), Integer.parseInt(txtSalary.getText())));
            clearFields();
            lblConsole.setTextFill(Color.web("#42ba96"));
            lblConsole.setText("Added!");
        } else {
            clearFields();
            lblConsole.setTextFill(Color.web("#ff0033"));
            lblConsole.setText("Some fields are empty!");
            event.consume();
        }

    }

    private void clearFields() {
        txtName.setText("");
        txtSurname.setText("");
        txtSalary.setText("");
        lblConsole.setText("");
    }

    public void deleteBarber(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResource("media/error.png")).toString()));
        alert.setTitle("Warning!");
        alert.setHeaderText("Do you really want to delete?");
        alert.setContentText("All reservations belongs to the barber you selected will be removed!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            TableView.TableViewSelectionModel<Barber> selectionModel = barbersTable.getSelectionModel();
            ObservableList<Barber> selectedItems = selectionModel.getSelectedItems();
            System.out.println(selectedItems.get(0).getId()+" "+selectedItems.get(0).getName());
            db.deleteBarber(selectedItems.get(0).getId());
            dataTable.remove(selectedItems.get(0));
            lblConsole.setTextFill(Color.web("#ff0033"));
            lblConsole.setText("Barber is deleted!");
        }

    }
}
