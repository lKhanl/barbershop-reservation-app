package edu.eskisehir.controllers;

import edu.eskisehir.*;
import edu.eskisehir.db.DataBaseOperations;
import edu.eskisehir.entity.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.controlsfx.control.CheckComboBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Time;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@SuppressWarnings("ALL")
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
    public TextField txtResSearch;
    public ComboBox<String> comboStatsYear1;
    public ComboBox<String> comboStatsMonth1;
    public Label lblStats1;
    public ComboBox<String> comboStatsYear2;
    public ComboBox<String> comboStatsMonth2;
    public Label lblStats3;
    public ComboBox<String> comboStatsYear3;
    public Label lblStats4;
    public Label lblStats5;
    public Label lblStats2;
    public ImageView clap;
    public TabPane tabPane;
    public Label lblResCount;

    DataBaseOperations db = new DataBaseOperations();
    ObservableList<Barber> barbersData;
    ObservableList<Operation> operationsData;
    ObservableList<Customer> customersData;
    ObservableList<Reservation> resData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editableColsForBarber();
        editableColsForOp();
        editableColsForCustomer();
        editableColsForAllRes();

        loadDataForBarber();
//        loadDataForOp();
//        loadDataForCustomer(null);
//        loadDataForRes(null);
//        loadDataForStats();

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

        tabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                switch (newValue.intValue()) {
                    case 0:
                        loadDataForBarber();
                        break;
                    case 1:
                        loadDataForOp();
                        break;
                    case 2:
                        loadDataForCustomer(null);
                        break;
                    case 3:
                        loadDataForRes(null);
                        break;
                    case 4:
                        loadDataForStats();
                        break;
                }
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
            lblConsoleBarber.setTextFill(Color.GREEN);
            lblConsoleBarber.setText("Updated!");
        });

        barberSurnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        barberSurnameCol.setOnEditCommit(e -> {
            Barber barber = e.getTableView().getItems().get(e.getTablePosition().getRow());
            barber.setSurname(e.getNewValue());
            db.updateBarber(Attribute.SURNAME, e.getNewValue(), barber.getId());
            lblConsoleBarber.setTextFill(Color.GREEN);
            lblConsoleBarber.setText("Updated!");
        });

        barberSalaryCol.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        barberSalaryCol.setOnEditCommit(e -> {
            Barber barber = e.getTableView().getItems().get(e.getTablePosition().getRow());
            barber.setSalary(e.getNewValue());
            db.updateBarber(Attribute.SALARY, String.valueOf(e.getNewValue()), barber.getId());
            lblConsoleBarber.setTextFill(Color.GREEN);
            lblConsoleBarber.setText("Updated!");
        });

        barbersTable.setEditable(true);
        barbersTable.setPlaceholder(new Label("There is no barber to show!"));
    }

    private void editableColsForOp() {
        opNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        opPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        opNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        opNameCol.setOnEditCommit(e -> {
            Operation op = e.getTableView().getItems().get(e.getTablePosition().getRow());
            op.setName(e.getNewValue());
            db.updateOperation(Attribute.NAME, e.getNewValue(), op.getId());
            lblConsoleOp.setTextFill(Color.GREEN);
            lblConsoleOp.setText("Updated!");
        });

        opPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        opPriceCol.setOnEditCommit(e -> {
            Operation op = e.getTableView().getItems().get(e.getTablePosition().getRow());
            op.setPrice(e.getNewValue());
            db.updateOperation(Attribute.PRICE, String.valueOf(e.getNewValue()), op.getId());
            lblConsoleOp.setTextFill(Color.GREEN);
            lblConsoleOp.setText("Updated!");
        });
        operationsTable.setEditable(true);
        operationsTable.setPlaceholder(new Label("There is no op to show, please add some ops!"));

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
            lblConsoleCustomer.setTextFill(Color.GREEN);
            lblConsoleCustomer.setText("Updated!");
        });

        customerSurnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        customerSurnameCol.setOnEditCommit(e -> {
            Customer customer = e.getTableView().getItems().get(e.getTablePosition().getRow());
            customer.setSurname(e.getNewValue());
            db.updateCustomer(Attribute.SURNAME, e.getNewValue(), customer.getId());
            lblConsoleCustomer.setTextFill(Color.GREEN);
            lblConsoleCustomer.setText("Updated!");
        });

        customerEmailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        customerEmailCol.setOnEditCommit(e -> {
            Customer customer = e.getTableView().getItems().get(e.getTablePosition().getRow());
            customer.setEmail(e.getNewValue());
            db.updateCustomer(Attribute.EMAIL, e.getNewValue(), customer.getId());
            lblConsoleCustomer.setTextFill(Color.GREEN);
            lblConsoleCustomer.setText("Updated!");
        });

        customersTable.setEditable(true);
        customersTable.setPlaceholder(new Label("There is no customer to show!"));

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

                for (int i = 0; i < comboOps.getItems().size(); i++) {
                    comboOps.getCheckModel().clearCheck(i);
                }
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
                LocalTime localDate = LocalTime.now();
                String current = localDate.getHour() + ":" + localDate.getMinute() + ":" + localDate.getSecond();
                comboStatus.getItems().clear();
                if (LocalDate.now().compareTo(newSelection.getDate().toLocalDate()) == 0 && Time.valueOf(current).compareTo(newSelection.getTime()) < 0) {
//                    System.out.println("Bugün ama gelecek");
                    comboStatus.getItems().addAll("Waiting", "Canceled");
                } else if (LocalDate.now().compareTo(newSelection.getDate().toLocalDate()) < 0) {
//                    System.out.println("gelecek");
                    comboStatus.getItems().addAll("Waiting", "Canceled");
                } else {
                    comboStatus.getItems().addAll("Done", "Canceled");
                }
            }
        });
        resTable.setPlaceholder(new Label("There is no reservation to show!"));

    }

    private void loadDataForBarber() {
        barbersData = FXCollections.observableArrayList();
        List<Barber> barbers = db.getBarbers();
        barbersData.addAll(barbers);
        barbersTable.setItems(barbersData);

        barberNameCol.setSortType(TableColumn.SortType.ASCENDING);
        barbersTable.getSortOrder().add(barberNameCol);
        barbersTable.sort();
    }

    private void loadDataForOp() {
        operationsData = FXCollections.observableArrayList();
        List<Operation> operations = db.getOperations();
        operationsData.addAll(operations);
        operationsTable.setItems(operationsData);

        opPriceCol.setSortType(TableColumn.SortType.ASCENDING);
        operationsTable.getSortOrder().add(opPriceCol);
        operationsTable.sort();
    }

    private void loadDataForStats() {
        List<String> monthsList = new LinkedList<>(Arrays.asList(new DateFormatSymbols().getMonths()));
        monthsList.remove(monthsList.size() - 1);

        comboStatsMonth1.getItems().clear();
        comboStatsMonth2.getItems().clear();
        comboStatsMonth1.getItems().addAll(monthsList);
        comboStatsMonth2.getItems().addAll(monthsList);

        comboStatsYear1.getItems().clear();
        comboStatsYear2.getItems().clear();
        comboStatsYear3.getItems().clear();
        List<String> years = db.getExistingYear();
        comboStatsYear1.getItems().addAll(years);
        comboStatsYear2.getItems().addAll(years);
        comboStatsYear3.getItems().addAll(years);

        Time time = db.busiestTime();
        if (time == null) {
            lblStats3.setText("time");
        } else
            lblStats3.setText(time.toString());

        Customer customer = db.mostVisitedCustomer();
        if (customer == null) {
            lblStats5.setText("");
        } else
            lblStats5.setText(db.mostVisitedCustomer().toString());

        clap.setVisible(false);
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
        customerNameCol.setSortType(TableColumn.SortType.ASCENDING);
        customersTable.getSortOrder().add(customerNameCol);
        customersTable.sort();
    }

    private void loadDataForRes(List<Reservation> list) {
        if (list == null) {
            resData = FXCollections.observableArrayList();
            List<Reservation> reservations = db.adminResList();
            resData.addAll(reservations);
            resTable.setItems(resData);
        } else {
            ObservableList<Reservation> temp = FXCollections.observableArrayList();
            temp.addAll(list);
            resTable.setItems(temp);
        }
        lblResCount.setText("There are " + resTable.getItems().size() + " reservation in the table!");
    }

    public void addBarber(ActionEvent event) {
        if (!(txtBarberName.getText().equals("") || txtBarberSurname.getText().equals("") || txtBarberSalary.getText().equals(""))) {
            int id = db.addBarber(txtBarberName.getText(), txtBarberSurname.getText(), Integer.parseInt(txtBarberSalary.getText()));
            barbersData.add(new Barber(id, txtBarberName.getText(), txtBarberSurname.getText(), Integer.parseInt(txtBarberSalary.getText())));
            lblConsoleBarber.setTextFill(Color.GREEN);
            lblConsoleBarber.setText("Barber was added!");

            txtBarberName.setText("");
            txtBarberSurname.setText("");
            txtBarberSalary.setText("");
        } else {
            lblConsoleBarber.setTextFill(Color.RED);
            lblConsoleBarber.setText("Some fields are empty!");
            event.consume();
        }
    }

    public void deleteBarber(ActionEvent event) {
        TableView.TableViewSelectionModel<Barber> selectionModel = barbersTable.getSelectionModel();
        ObservableList<Barber> selectedItems = selectionModel.getSelectedItems();
        if (selectedItems.size() == 0) {
            lblConsoleBarber.setTextFill(Color.RED);
            lblConsoleBarber.setText("Select once!");
            return;
        }
        Optional<ButtonType> result = showAlert("barber");
        if (result.get() == ButtonType.OK) {
            db.deleteBarber(selectedItems.get(0).getId());
            barbersData.remove(selectedItems.get(0));
            lblConsoleBarber.setTextFill(Color.RED);
            lblConsoleBarber.setText("Barber was deleted!");
        } else {
            event.consume();
        }

    }

    public void addOp(ActionEvent event) {
        if (!(txtOpName.getText().equals("") || txtOpPrice.getText().equals(""))) {
            int id = db.addOperation(txtOpName.getText(), Integer.parseInt(txtOpPrice.getText()));
            operationsData.add(new Operation(id, txtOpName.getText(), Integer.parseInt(txtOpPrice.getText())));
            lblConsoleOp.setTextFill(Color.GREEN);
            lblConsoleOp.setText("Operation was added!");

            txtOpPrice.setText("");
            txtOpName.setText("");
        } else {
            lblConsoleOp.setTextFill(Color.RED);
            lblConsoleOp.setText("Some fields are empty!");
            event.consume();
        }
    }

    public void deleteOp(ActionEvent event) {
        TableView.TableViewSelectionModel<Operation> selectionModel = operationsTable.getSelectionModel();
        ObservableList<Operation> selectedItems = selectionModel.getSelectedItems();
        if (selectedItems.size() == 0) {
            lblConsoleOp.setTextFill(Color.RED);
            lblConsoleOp.setText("Select once!");
            return;
        }
        Optional<ButtonType> result = showAlert("operation");
        if (result.get() == ButtonType.OK) {
            db.deleteOperation(selectedItems.get(0).getId());
            operationsData.remove(selectedItems.get(0));
            lblConsoleOp.setTextFill(Color.RED);
            lblConsoleOp.setText("Operation was deleted!");
        } else {
            event.consume();
        }

    }

    private Optional<ButtonType> showAlert(String obj) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        try {
            stage.getIcons().add(new Image(new FileInputStream("src/main/resources/edu/eskisehir/media/error.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        alert.setTitle("Warning!");
        alert.setHeaderText("Do you really want to delete?");
        alert.setContentText("All reservations belongs to the " + obj + " you selected will be removed!");
        return alert.showAndWait();
    }

    public void deleteCustomer(ActionEvent event) {
        TableView.TableViewSelectionModel<Customer> selectionModel = customersTable.getSelectionModel();
        ObservableList<Customer> selectedItems = selectionModel.getSelectedItems();
        if (selectedItems.size() == 0) {
            lblConsoleCustomer.setTextFill(Color.RED);
            lblConsoleCustomer.setText("Select once!");
            return;
        }
        Optional<ButtonType> result = showAlert("customer");
        if (result.get() == ButtonType.OK) {
            db.deleteCustomer(selectedItems.get(0).getId());
            customersData.remove(selectedItems.get(0));
            lblConsoleCustomer.setTextFill(Color.RED);
            lblConsoleCustomer.setText("Customer was deleted!");
            txtCustomerSearch.setText("");
            loadDataForCustomer(null);
        } else {
            event.consume();
        }
    }

    public void searchAnyForCustomers(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            loadDataForCustomer(db.searchCustomer(txtCustomerSearch.getText()));
        }
    }

    public void changeStatus(ActionEvent event) {
        if (comboStatus.getSelectionModel().getSelectedItem() != null) {
            Reservation reservation = resTable.getSelectionModel().getSelectedItem();
            reservation.setIsDone(comboStatus.getValue());

            db.updateIsDone(reservation.getId(), comboStatus.getValue());
            resTable.getSelectionModel().getSelectedItem().setIsDone(comboStatus.getValue());
            resTable.refresh();

            lblConsoleRes.setTextFill(Color.GREEN);
            lblConsoleRes.setText("Successfully, changed status!");
        } else if (comboStatus.getSelectionModel().getSelectedItem() == null) {
            lblConsoleRes.setTextFill(Color.RED);
            lblConsoleRes.setText("First, select a row!");
        }
    }

    public void updateOp(ActionEvent event) {
        if (comboOps.getCheckModel().getCheckedItems().size() == 0) {
            lblConsoleRes.setTextFill(Color.RED);
            lblConsoleRes.setText("You must select at least one!");
        } else if (comboOps.getCheckModel().getCheckedItems() != null) {
            ObservableList<Operation> selectedOps = comboOps.getCheckModel().getCheckedItems();
            List<Operation> updated = new LinkedList<>();
            updated.addAll(selectedOps);
            resTable.getSelectionModel().getSelectedItem().setOps(updated);
            db.updateCustomersOperations(resTable.getSelectionModel().getSelectedItem().getId(), updated);
            lblConsoleRes.setTextFill(Color.GREEN);
            lblConsoleRes.setText("Successfully, updated operations!");
            resTable.refresh();
        }

    }

    public void getMostSelectedOp(ActionEvent event) {
        if (comboStatsMonth1.getSelectionModel().getSelectedItem() != null && comboStatsYear1.getSelectionModel().getSelectedItem() != null) {
            int index = comboStatsMonth1.getSelectionModel().getSelectedIndex() + 1;
            Operation op = db.mostSelectedOperation(comboStatsYear1.getSelectionModel().getSelectedItem(), index);
            if (op == null) {
                lblStats1.setStyle("-fx-font-size: 14");
                lblStats1.setTextFill(Color.RED);
                lblStats1.setText("There is no operation for this month!");
            } else {
                lblStats1.setStyle("-fx-font-size: 18");
                lblStats1.setTextFill(Color.BLACK);
                lblStats1.setText(op.getName());
            }

        }
    }

    public void getMostChosenBarber(ActionEvent event) {
        if (comboStatsMonth2.getSelectionModel().getSelectedItem() != null && comboStatsYear2.getSelectionModel().getSelectedItem() != null) {
            int index = comboStatsMonth2.getSelectionModel().getSelectedIndex() + 1;
            Barber barber = db.mostSelectedBarber(comboStatsYear2.getSelectionModel().getSelectedItem(), index);
            if (barber == null) {
                clap.setVisible(false);
                lblStats2.setStyle("-fx-font-size: 14");
                lblStats2.setTextFill(Color.RED);
                lblStats2.setText("There is no most chosen barber!");
            } else {
                clap.setVisible(true);
                lblStats2.setStyle("-fx-font-size: 18");
                lblStats2.setTextFill(Color.BLACK);
                lblStats2.setText(barber.toString());
            }
        }
    }

    public void getAverageMonthlyIncome(ActionEvent event) {
        double income = db.averageMonthlyIncome(comboStatsYear3.getSelectionModel().getSelectedItem());
        lblStats4.setText(String.valueOf(income));
    }

    public void pauseAndPlay(ActionEvent event) {
        Main.pauseAndPlay();

    }

    public void refresh(ActionEvent event) {
        txtResSearch.setText("");
        loadDataForRes(null);
    }

    public void searchForRes(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            List<Reservation> reservations = db.searchReservation(txtResSearch.getText());
////            reservations.forEach(reservation -> System.out.println(reservation.getId()));
            loadDataForRes(reservations);
        }

    }
}
