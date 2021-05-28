package edu.eskisehir;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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
    public TableColumn<Barber, String> barberNameCol;
    public TableColumn<Barber, Integer> barberSalaryCol;
    public TableColumn<Barber, String> barberSurnameCol;
    public TextField txtBarberName;
    public TextField txtBarberSurname;
    public TextField txtBarberSalary;
    public Button btnBarberAdd;
    public Label lblConsole;
    public Button btnBarberDelete;
    public TableView<Operation> operationsTable;
    public Button btnOpDelete;
    public Button btnOpAdd;
    public TextField txtOpName;
    public TextField txtOpPrice;
    public TableColumn<Operation, String> opNameCol;
    public TableColumn<Operation, Integer> opPriceCol;
    public Tab tabOp;
    public Tab tabBarbers;
    public Tab tabCustomers;
    public Tab tabRes;
    public TableView<Customer> customersTable;
    public TableColumn<Customer, String> customerNameCol;
    public TableColumn<Customer, String> customerSurnameCol;
    public TableColumn<Customer, String> customerEmailCol;
    public Button btnCustomerDelete;
    public TextField txtCustomerSearch;

    DataBaseOperations db = new DataBaseOperations();
    ObservableList<Barber> barbersData;
    ObservableList<Operation> operationsData;
    ObservableList<Customer> customersData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editableColsForBarber();
        editableColsForOp();
        editableColsForCustomer();

        loadDataForBarber();
        loadDataForOp();
        loadDataForCustomer(null);

        //Accept only numbers for int
        txtBarberSalary.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtBarberSalary.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        txtOpPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtOpPrice.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void editableColsForBarber() {
        barberNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        barberSurnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        barberSalaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));

        barberNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        barberNameCol.setOnEditCommit(e -> {
            Barber barber = e.getTableView().getItems().get(e.getTablePosition().getRow());
            barber.setName(e.getNewValue());
            db.updateBarber(Attribute.NAME, e.getNewValue(), barber.getId());
        });

        barberSurnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        barberSurnameCol.setOnEditCommit(e -> {
            Barber barber = e.getTableView().getItems().get(e.getTablePosition().getRow());
            barber.setSurname(e.getNewValue());
            db.updateBarber(Attribute.SURNAME, e.getNewValue(), barber.getId());
        });

        barberSalaryCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        barberSalaryCol.setOnEditCommit(e -> {
            Barber barber = e.getTableView().getItems().get(e.getTablePosition().getRow());
            barber.setSalary(e.getNewValue());
            db.updateBarber(Attribute.SALARY, String.valueOf(e.getNewValue()), barber.getId());
        });

        barbersTable.setEditable(true);
    }

    private void editableColsForOp() {
        opNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        opPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        opNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        opNameCol.setOnEditCommit(e -> {
            Operation op = e.getTableView().getItems().get(e.getTablePosition().getRow());
            op.setName(e.getNewValue());
            db.updateOperation(Attribute.NAME, e.getNewValue(), op.getId());
        });

        opPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        opPriceCol.setOnEditCommit(e -> {
            Operation op = e.getTableView().getItems().get(e.getTablePosition().getRow());
            op.setPrice(e.getNewValue());
            db.updateOperation(Attribute.PRICE, String.valueOf(e.getNewValue()), op.getId());
        });
        operationsTable.setEditable(true);

    }

    private void editableColsForCustomer() {
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerSurnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        customerEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        customerNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        customerNameCol.setOnEditCommit(e -> {
            Customer customer = e.getTableView().getItems().get(e.getTablePosition().getRow());
            customer.setName(e.getNewValue());
            db.updateCustomer(Attribute.NAME, e.getNewValue(), customer.getId());
        });

        customerSurnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        customerSurnameCol.setOnEditCommit(e -> {
            Customer customer = e.getTableView().getItems().get(e.getTablePosition().getRow());
            customer.setSurname(e.getNewValue());
            db.updateCustomer(Attribute.SURNAME, e.getNewValue(), customer.getId());
        });

        customerEmailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        customerEmailCol.setOnEditCommit(e -> {
            Customer customer = e.getTableView().getItems().get(e.getTablePosition().getRow());
            customer.setEmail(e.getNewValue());
            db.updateCustomer(Attribute.EMAIL, e.getNewValue(), customer.getId());
        });

        customersTable.setEditable(true);
    }

    private void loadDataForBarber() {
        barbersData = FXCollections.observableArrayList();
        List<Barber> barbers = db.getBarbers();
        barbersData.addAll(barbers);
        barbersTable.setItems(barbersData);
    }

    private void loadDataForOp() {
        operationsData = FXCollections.observableArrayList();
        List<Operation> operations = db.getOperations();
        operationsData.addAll(operations);
        operationsTable.setItems(operationsData);
    }

    private void loadDataForCustomer(List<Customer> list) {
        if (list == null){
            customersData = FXCollections.observableArrayList();
            List<Customer> customers = db.getCustomers();
            customersData.addAll(customers);
            customersTable.setItems(customersData);
        }else {
            ObservableList<Customer> temp = FXCollections.observableArrayList();
            temp.addAll(list);
            customersTable.setItems(temp);
        }

    }

    public void addBarber(ActionEvent event) {

        if (!(txtBarberName.getText().equals("") || txtBarberSurname.getText().equals("") || txtBarberSalary.getText().equals(""))) {
            int id = db.addBarber(txtBarberName.getText(), txtBarberSurname.getText(), Integer.parseInt(txtBarberSalary.getText()));
            barbersData.add(new Barber(id, txtBarberName.getText(), txtBarberSurname.getText(), Integer.parseInt(txtBarberSalary.getText())));
            clearFieldsForBarber();
            lblConsole.setTextFill(Color.web("#42ba96"));
            lblConsole.setText("Added!");
        } else {
            clearFieldsForBarber();
            lblConsole.setTextFill(Color.web("#ff0033"));
            lblConsole.setText("Some fields are empty!");
            event.consume();
        }

    }

    private void clearFieldsForBarber() {
        txtBarberName.setText("");
        txtBarberSurname.setText("");
        txtBarberSalary.setText("");
        lblConsole.setText("");
    }

    public void deleteBarber(ActionEvent event) {
        Optional<ButtonType> result = showAlert("barber");
        if (result.get() == ButtonType.OK) {
            TableView.TableViewSelectionModel<Barber> selectionModel = barbersTable.getSelectionModel();
            ObservableList<Barber> selectedItems = selectionModel.getSelectedItems();
            System.out.println(selectedItems.get(0).getId() + " " + selectedItems.get(0).getName());
            db.deleteBarber(selectedItems.get(0).getId());
            barbersData.remove(selectedItems.get(0));
            lblConsole.setTextFill(Color.web("#ff0033"));
            lblConsole.setText("Barber is deleted!");
        } else {
            event.consume();
        }

    }

    public void addOp(ActionEvent event) {
        if (!(txtOpName.getText().equals("") || txtOpPrice.getText().equals(""))) {
            int id = db.addOperation(txtOpName.getText(), Integer.parseInt(txtOpPrice.getText()));
            operationsData.add(new Operation(id, txtOpName.getText(), Integer.parseInt(txtOpPrice.getText())));
            clearFieldsForOp();
//            lblConsole.setTextFill(Color.web("#42ba96"));
//            lblConsole.setText("Added!");
        } else {
            clearFieldsForOp();
//            lblConsole.setTextFill(Color.web("#ff0033"));
//            lblConsole.setText("Some fields are empty!");
            event.consume();
        }
    }

    public void deleteOp(ActionEvent event) {
        Optional<ButtonType> result = showAlert("operation");
        if (result.get() == ButtonType.OK) {
            TableView.TableViewSelectionModel<Operation> selectionModel = operationsTable.getSelectionModel();
            ObservableList<Operation> selectedItems = selectionModel.getSelectedItems();
            db.deleteOperation(selectedItems.get(0).getId());
            operationsData.remove(selectedItems.get(0));
//            lblConsole.setTextFill(Color.web("#ff0033"));
//            lblConsole.setText("Barber is deleted!");
        } else {
            event.consume();
        }


    }

    private Optional<ButtonType> showAlert(String obj) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResource("media/error.png")).toString()));
        alert.setTitle("Warning!");
        alert.setHeaderText("Do you really want to delete?");
        alert.setContentText("All reservations belongs to the " + obj + " you selected will be removed!");
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    private void clearFieldsForOp() {
        txtOpPrice.setText("");
        txtOpName.setText("");
//        lblConsole.setText("");
    }

    public void deleteCustomer(ActionEvent event) {
        Optional<ButtonType> result = showAlert("customer");
        if (result.get() == ButtonType.OK) {
            TableView.TableViewSelectionModel<Customer> selectionModel = customersTable.getSelectionModel();
            ObservableList<Customer> selectedItems = selectionModel.getSelectedItems();
//            System.out.println(selectedItems.get(0).getId() + " " + selectedItems.get(0).getName());
            db.deleteCustomer(selectedItems.get(0).getId());
            customersData.remove(selectedItems.get(0));
            lblConsole.setTextFill(Color.web("#ff0033"));
            lblConsole.setText("Barber is deleted!");
            txtCustomerSearch.setText("");
            loadDataForCustomer(null);
        } else {
            event.consume();
        }
    }

    public void keypress(KeyEvent keyEvent) {
        if (txtCustomerSearch.getText().equals("")) {
            keyEvent.consume();
        } else {
            loadDataForCustomer(db.searchName(txtCustomerSearch.getText()));
        }
    }
}
