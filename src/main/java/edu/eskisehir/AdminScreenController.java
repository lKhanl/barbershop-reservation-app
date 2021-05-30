package edu.eskisehir;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.IndexedCheckModel;

import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

public class AdminScreenController implements Initializable {
    public TableView<Barber> barbersTable;
    public TableColumn<Barber, String> barberNameCol;
    public TableColumn<Barber, Integer> barberSalaryCol;
    public TableColumn<Barber, String> barberSurnameCol;
    public TextField txtBarberName;
    public TextField txtBarberSurname;
    public TextField txtBarberSalary;
    public Button btnBarberAdd;
    public Label lblConsoleBarber;
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
    public Label lblConsoleOp;
    public Label lblConsoleCustomer;
    public TableColumn<Reservation, ComboBox<String>> resStatusCol;
    public TableColumn<Reservation, Long> resIDCol;
    public TableColumn<Reservation, Barber> resBarberCol;
    public TableColumn<Reservation, Customer> resCustomerCol;
    public TableColumn<Reservation, Date> resDateCol;
    public TableColumn<Reservation, Time> resTimeCol;
    public TableColumn<Reservation, Integer> resCostCol;
    public TableColumn<Reservation, List<Operation>> resOpsCol;
    public TableView<Reservation> resTable;
    public ComboBox<String> comboStatus;
    public Label lblConsoleRes;
    public CheckComboBox<Operation> comboOps;

    DataBaseOperations db = new DataBaseOperations();
    ObservableList<Barber> barbersData;
    ObservableList<Operation> operationsData;
    ObservableList<Customer> customersData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editableColsForBarber();
        editableColsForOp();
        editableColsForCustomer();
        editableColsForAllRes();

        loadDataForBarber();
        loadDataForOp();
        loadDataForCustomer(null);
        loadDataForRes();

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
            lblConsoleBarber.setTextFill(Color.web("#42ba96"));
            lblConsoleBarber.setText("Updated!");
        });

        barberSurnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        barberSurnameCol.setOnEditCommit(e -> {
            Barber barber = e.getTableView().getItems().get(e.getTablePosition().getRow());
            barber.setSurname(e.getNewValue());
            db.updateBarber(Attribute.SURNAME, e.getNewValue(), barber.getId());
            lblConsoleBarber.setTextFill(Color.web("#42ba96"));
            lblConsoleBarber.setText("Updated!");
        });

        barberSalaryCol.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        barberSalaryCol.setOnEditCommit(e -> {
            Barber barber = e.getTableView().getItems().get(e.getTablePosition().getRow());
            barber.setSalary(e.getNewValue());
            db.updateBarber(Attribute.SALARY, String.valueOf(e.getNewValue()), barber.getId());
            lblConsoleBarber.setTextFill(Color.web("#42ba96"));
            lblConsoleBarber.setText("Updated!");
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
            lblConsoleOp.setTextFill(Color.web("#42ba96"));
            lblConsoleOp.setText("Updated!");
        });

        opPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        opPriceCol.setOnEditCommit(e -> {
            Operation op = e.getTableView().getItems().get(e.getTablePosition().getRow());
            op.setPrice(e.getNewValue());
            db.updateOperation(Attribute.PRICE, String.valueOf(e.getNewValue()), op.getId());
            lblConsoleOp.setTextFill(Color.web("#42ba96"));
            lblConsoleOp.setText("Updated!");
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
            lblConsoleCustomer.setTextFill(Color.web("#42ba96"));
            lblConsoleCustomer.setText("Updated!");
        });

        customerSurnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        customerSurnameCol.setOnEditCommit(e -> {
            Customer customer = e.getTableView().getItems().get(e.getTablePosition().getRow());
            customer.setSurname(e.getNewValue());
            db.updateCustomer(Attribute.SURNAME, e.getNewValue(), customer.getId());
            lblConsoleCustomer.setTextFill(Color.web("#42ba96"));
            lblConsoleCustomer.setText("Updated!");
        });

        customerEmailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        customerEmailCol.setOnEditCommit(e -> {
            Customer customer = e.getTableView().getItems().get(e.getTablePosition().getRow());
            customer.setEmail(e.getNewValue());
            db.updateCustomer(Attribute.EMAIL, e.getNewValue(), customer.getId());
            lblConsoleCustomer.setTextFill(Color.web("#42ba96"));
            lblConsoleCustomer.setText("Updated!");
        });

        customersTable.setEditable(true);
    }

    private void editableColsForAllRes() {
        resBarberCol.setCellValueFactory(new PropertyValueFactory<>("barber"));
        resCostCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
        resDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        resIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        resCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        resOpsCol.setCellValueFactory(new PropertyValueFactory<>("ops"));
        resStatusCol.setCellValueFactory(new PropertyValueFactory<>("isDone"));
        resTimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        resTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

                comboOps.getItems().clear();
                comboOps.setTitle("Operations");
                List<Operation> selectedOps = newSelection.getOps();
                List<Operation> ops = db.getOperations();
                comboOps.getItems().addAll(ops);
                for (int i = 0; i < selectedOps.size(); i++) {
                    for (int j = 0; j < ops.size(); j++) {
                        if (selectedOps.get(i).getId() == ops.get(j).getId()) {
                            comboOps.getCheckModel().check(j);
                        }
                    }
                }

                if (LocalDate.now().compareTo(newSelection.getDate().toLocalDate()) > 0) {
                    comboStatus.getItems().clear();
                    comboStatus.getItems().addAll("Done", "Canceled");
                } else {
                    comboStatus.getItems().clear();
                    comboStatus.getItems().addAll("Done", "Waiting", "Canceled");
                }
                /*comboOps.getCheckModel().getCheckedItems().forEach(e -> {
                    System.out.println(e.getId());
                });*/
            }
        });

        // TODO: sort is running wrongly
        /*resDateCol.setSortType(TableColumn.SortType.ASCENDING);
        resTable.getSortOrder().add(resDateCol);
        resTable.sort();*/
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
        if (list == null) {
            customersData = FXCollections.observableArrayList();
            List<Customer> customers = db.getCustomers();
            customersData.addAll(customers);
            customersTable.setItems(customersData);
        } else {
            ObservableList<Customer> temp = FXCollections.observableArrayList();
            temp.addAll(list);
            customersTable.setItems(temp);
        }

    }

    private void loadDataForRes() {
        List<Reservation> reservations = db.adminResList();
        resTable.getItems().addAll(reservations);
    }

    public void addBarber(ActionEvent event) {
        if (!(txtBarberName.getText().equals("") || txtBarberSurname.getText().equals("") || txtBarberSalary.getText().equals(""))) {
            int id = db.addBarber(txtBarberName.getText(), txtBarberSurname.getText(), Integer.parseInt(txtBarberSalary.getText()));
            barbersData.add(new Barber(id, txtBarberName.getText(), txtBarberSurname.getText(), Integer.parseInt(txtBarberSalary.getText())));
            lblConsoleBarber.setTextFill(Color.web("#42ba96"));
            lblConsoleBarber.setText("Barber was added!");

            txtBarberName.setText("");
            txtBarberSurname.setText("");
            txtBarberSalary.setText("");
        } else {
            lblConsoleBarber.setTextFill(Color.web("#ff0033"));
            lblConsoleBarber.setText("Some fields are empty!");
            event.consume();
        }
    }

    public void deleteBarber(ActionEvent event) {
        TableView.TableViewSelectionModel<Barber> selectionModel = barbersTable.getSelectionModel();
        ObservableList<Barber> selectedItems = selectionModel.getSelectedItems();
        if (selectedItems.size() == 0) {
            lblConsoleBarber.setTextFill(Color.web("#ff0033"));
            lblConsoleBarber.setText("Select once!");
            return;
        }
        Optional<ButtonType> result = showAlert("barber");
        if (result.get() == ButtonType.OK) {
            db.deleteBarber(selectedItems.get(0).getId());
            barbersData.remove(selectedItems.get(0));
            lblConsoleBarber.setTextFill(Color.web("#ff0033"));
            lblConsoleBarber.setText("Barber was deleted!");
        } else {
            event.consume();
        }

    }

    public void addOp(ActionEvent event) {
        if (!(txtOpName.getText().equals("") || txtOpPrice.getText().equals(""))) {
            int id = db.addOperation(txtOpName.getText(), Integer.parseInt(txtOpPrice.getText()));
            operationsData.add(new Operation(id, txtOpName.getText(), Integer.parseInt(txtOpPrice.getText())));
            lblConsoleOp.setTextFill(Color.web("#42ba96"));
            lblConsoleOp.setText("Operation was added!");

            txtOpPrice.setText("");
            txtOpName.setText("");
        } else {
            lblConsoleOp.setTextFill(Color.web("#ff0033"));
            lblConsoleOp.setText("Some fields are empty!");
            event.consume();
        }
    }

    public void deleteOp(ActionEvent event) {
        TableView.TableViewSelectionModel<Operation> selectionModel = operationsTable.getSelectionModel();
        ObservableList<Operation> selectedItems = selectionModel.getSelectedItems();
        if (selectedItems.size() == 0) {
            lblConsoleOp.setTextFill(Color.web("#ff0033"));
            lblConsoleOp.setText("Select once!");
            return;
        }
        Optional<ButtonType> result = showAlert("operation");
        if (result.get() == ButtonType.OK) {
            db.deleteOperation(selectedItems.get(0).getId());
            operationsData.remove(selectedItems.get(0));
            lblConsoleOp.setTextFill(Color.web("#ff0033"));
            lblConsoleOp.setText("Operation was deleted!");
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
        return alert.showAndWait();
    }

    public void deleteCustomer(ActionEvent event) {
        TableView.TableViewSelectionModel<Customer> selectionModel = customersTable.getSelectionModel();
        ObservableList<Customer> selectedItems = selectionModel.getSelectedItems();
        if (selectedItems.size() == 0) {
            lblConsoleCustomer.setTextFill(Color.web("#ff0033"));
            lblConsoleCustomer.setText("Select once!");
            return;
        }
        Optional<ButtonType> result = showAlert("customer");
        if (result.get() == ButtonType.OK) {
            db.deleteCustomer(selectedItems.get(0).getId());
            customersData.remove(selectedItems.get(0));
            lblConsoleCustomer.setTextFill(Color.web("#ff0033"));
            lblConsoleCustomer.setText("Customer was deleted!");
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

    public void changeStatus(ActionEvent event) {
        if (comboStatus.getSelectionModel().getSelectedItem() != null) {
            Reservation reservation = resTable.getSelectionModel().getSelectedItem();
            reservation.setIsDone(comboStatus.getValue());

            db.updateIsDone(reservation.getId(), comboStatus.getValue());
            resTable.getSelectionModel().getSelectedItem().setIsDone(comboStatus.getValue());
            resTable.refresh();

            lblConsoleRes.setTextFill(Color.web("#42ba96"));
            lblConsoleRes.setText("Successful");
        }
    }

    public void updateOp(ActionEvent event) {
        ObservableList<Operation> selectedOps = comboOps.getCheckModel().getCheckedItems();
        List<Operation> updated = new LinkedList<>();
        updated.addAll(selectedOps);
        resTable.getSelectionModel().getSelectedItem().setOps(updated);

        resTable.refresh();

    }

    /*public void getOps(MouseEvent mouseEvent) {
        if (resTable.getSelectionModel().getSelectedItem() != null) {
            List<Operation> selectedOps = resTable.getSelectionModel().getSelectedItem().getOps();
            List<Operation> ops = db.getOperations();
            comboOps.getItems().addAll(ops);

        } *//*else {
            lblConsoleRes.setTextFill(Color.web("#f84040"));
            lblConsoleRes.setText("Row didn't select!");
        }*//*

    }*/
}
