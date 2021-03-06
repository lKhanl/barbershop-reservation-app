package edu.eskisehir.controllers;


import edu.eskisehir.*;
import edu.eskisehir.db.DataBaseOperations;
import edu.eskisehir.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

import javax.imageio.ImageIO;


import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class ReservationController implements Initializable {

    public Pane card;
    public TextField txtName;
    public TextField txtSurname;
    public TextField txtEmail;
    public PasswordField txtPass;
    public Button btnSave;
    public Label lblConsoleProfile;
    public ComboBox<Barber> comboBarbers;
    public Pane resCard;
    public Pane resultCard;
    public DatePicker resDate;
    public ComboBox<Time> comboTime;
    public CheckComboBox<Operation> comboOp;
    public Label lblSelectedBarber;
    public Label lblSelectedDate;
    public Label lblSelectedTime;
    public Label lblSelectedOp;
    public Label lblConsoleRes;
    public Label lblResID;
    public TableView<Reservation> tableResHistory;
    public TableColumn<Reservation, String> colStatus;
    public TableColumn<Reservation, Long> colResID;
    public TableColumn<Reservation, Date> colDate;
    public TableColumn<Reservation, Time> colTime;
    public TableColumn<Reservation, Integer> colCost;
    public TableColumn<Reservation, Barber> colBarber;
    public TableColumn<Reservation, List<Operation>> colOps;
    public Label lblSelectedPrice;
    public TableView<Operation> tableOps;
    public TableColumn<Operation, String> colOpName;
    public TableColumn<Operation, Integer> colOpPrice;
    public CheckComboBox<Operation> comboServices;
    public Label lblSelectedServices;
    public Label lblServicesPrice;
    public ImageView logo;
    public CheckBox isExportCard;

    int cid = MainController.cid;
    DataBaseOperations db = new DataBaseOperations();
    List<Time> allTimes;
    ObservableList<Reservation> resData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCols();

        Customer customer = db.getCustomerByID(cid);
        txtName.setText(customer.getName());
        txtSurname.setText(customer.getSurname());
        txtEmail.setText(customer.getEmail());

        setComboBoxesStyle();
        loadDataForProfile();
        loadDataForRes();
        loadDataForServices();
    }

    private void loadDataForProfile() {
        List<Reservation> reservations = db.fillResHistory(cid);

        resData = FXCollections.observableArrayList(reservations);
        tableResHistory.setItems(resData);

        colDate.setSortType(TableColumn.SortType.ASCENDING);
        tableResHistory.getSortOrder().add(colDate);
        tableResHistory.sort();
        tableResHistory.setPlaceholder(new Label("There is no reservation to show!"));
    }

    private void loadDataForRes() {
        List<Barber> barbers = db.getBarbers();
        comboBarbers.getItems().addAll(barbers);

        allTimes = new LinkedList<>();
        List<String> temp = List.of("08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00", "11:00:00", "11:30:00", "12:00:00",
                "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00", "15:30:00", "16:00:00", "16:30:00", "17:00:00", "17:30:00",
                "18:00:00", "18:30:00", "19:00:00", "19:30:00", "20:00:00");
        for (String s : temp) {
            allTimes.add(Time.valueOf(s));
        }
        List<Operation> ops = db.getOperations();
        comboOp.getItems().addAll(ops);
    }

    private void loadDataForServices() {
        List<Operation> ops = db.getOperations();
        tableOps.getItems().setAll(ops);

        colOpPrice.setSortType(TableColumn.SortType.ASCENDING);
        tableOps.getSortOrder().add(colOpPrice);
        tableOps.sort();

        comboServices.getItems().addAll(ops);

        try {
            logo.setImage(new Image(new FileInputStream("src/main/resources/edu/eskisehir/media/logo.jpg")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setComboBoxesStyle() {
        comboBarbers.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Barber> call(ListView<Barber> l) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Barber barber, boolean empty) {
                        super.updateItem(barber, empty);
                        if (barber == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(barber.getName() + " " + barber.getSurname());
                        }
                    }
                };
            }
        });
        comboBarbers.setConverter(new StringConverter<>() {
            @Override
            public String toString(Barber barber) {
                if (barber == null) {
                    return null;
                } else {
                    return barber.getName() + " " + barber.getSurname();
                }
            }

            @Override
            public Barber fromString(String userId) {
                return null;
            }
        });
        resDate.setDayCellFactory(new Callback<>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        LocalDate today = LocalDate.now();
                        setDisable(empty || date.compareTo(today) < 0);

                        if (date.getDayOfWeek() == DayOfWeek.SATURDAY
                                || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        });
        stringConverterForCheckComboBox(comboOp);
        stringConverterForCheckComboBox(comboServices);

    }

    private void stringConverterForCheckComboBox(CheckComboBox<Operation> comboServices) {
        comboServices.setConverter(new StringConverter<>() {
            @Override
            public String toString(Operation op) {
                if (op == null) {
                    return null;
                } else {
                    return op.getName();
                }
            }

            @Override
            public Operation fromString(String userId) {
                return null;
            }
        });
    }

    public void save(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        try {
            stage.getIcons().add(new Image(new FileInputStream("src/main/resources/edu/eskisehir/media/error.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        alert.setTitle("Warning!");
        alert.setHeaderText("Do you really want to save?");

        if (txtName.getText().equals("") || txtSurname.getText().equals("") || txtEmail.getText().equals("")) {
            lblConsoleProfile.setTextFill(Color.RED);
            lblConsoleProfile.setText("Some fields are empty!");
            return;
        }

        Pattern p = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        Matcher m = p.matcher(txtEmail.getText());
        if (!m.find()) {
            lblConsoleProfile.setTextFill(Color.RED);
            lblConsoleProfile.setText("Invalid email!");
        } else {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                db.updateCustomer(Attribute.NAME, txtName.getText(), cid);
                db.updateCustomer(Attribute.SURNAME, txtSurname.getText(), cid);
                db.updateCustomer(Attribute.EMAIL, txtEmail.getText(), cid);
                if (!txtPass.getText().equals(""))
                    db.updateCustomer(Attribute.PASSWORD, txtPass.getText(), cid);
                lblConsoleProfile.setTextFill(Color.GREEN);
                lblConsoleProfile.setText("Saved!");
                txtPass.setText("");
            } else {
                event.consume();
            }
        }

    }

    public void book(ActionEvent event) {
        if (resDate.getValue() != null && comboTime.getValue() != null && comboBarbers.getValue() != null && comboOp.getCheckModel().getCheckedItems().size() != 0) {
            List<Integer> opIDs = new LinkedList<>();
            for (int i = 0; i < comboOp.getCheckModel().getCheckedItems().size(); i++) {
                opIDs.add(comboOp.getCheckModel().getCheckedItems().get(i).getId());
            }

            long resID = db.bookReservation(resDate.getValue().toString(), comboTime.getValue(), comboBarbers.getValue().getId(), cid, opIDs);

            lblResID.setText("ResID : " + resID);
            if (isExportCard.isSelected()) {
                saveAsPng(event);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info!");
                alert.setHeaderText("Reservation is Done, Please don't forget your resID!");
                alert.setContentText("ResID : " + resID);
                alert.show();
            }
            clearFieldsForResCard();
            resetResTab();
            loadDataForRes();

            lblConsoleRes.setTextFill(Color.GREEN);
            lblConsoleRes.setText("Successful!");
            lblSelectedDate.setText("");
        } else {
            lblConsoleRes.setTextFill(Color.RED);
            lblConsoleRes.setText("Some fields are empty!");
        }

    }

    private void resetResTab() {
        comboBarbers.getItems().clear();
        resDate.getEditor().clear();
        comboTime.getItems().clear();
        comboOp.getItems().clear();
        isExportCard.setSelected(false);
    }

    public void saveAsPng(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

        if (file != null) {
            try {
                WritableImage image = resultCard.snapshot(new SnapshotParameters(), null);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void loadTime(ActionEvent event) {
        if (!(comboBarbers.getValue() == null || resDate.getValue() == null)) {
            comboTime.getItems().clear();
            List<Time> avTime = new LinkedList<>();
            List<Time> busyTimes = db.getBusyTimes(comboBarbers.getValue().getId(), resDate.getValue().toString());
            avTime.addAll(allTimes);

            if (resDate.getValue().toString().equals(LocalDate.now().toString())) {
                LocalTime localTime = LocalTime.now();
                String currentTime = localTime.getHour() + ":" + localTime.getMinute() + ":" + localTime.getSecond();
                avTime.removeIf(time -> (time.compareTo(Time.valueOf(currentTime)) < 0));
            }
            avTime.removeAll(busyTimes);
            comboTime.getItems().addAll(avTime);

        }
        if (resDate.getValue() != null) {
            lblSelectedDate.setText(resDate.getValue().toString());
        }
        if (comboBarbers.getValue() != null) {
            lblSelectedBarber.setText(comboBarbers.getValue().getName() + " " + comboBarbers.getValue().getSurname());
        }

    }

    public void showTime(ActionEvent event) {
        if (comboTime.getValue() != null) {
            lblSelectedTime.setText(comboTime.getValue().toString());
        }
    }

    public void showOp(ActionEvent event) {
        if (!comboOp.getCheckModel().getCheckedItems().isEmpty()) {
            String s = "";
            for (int i = 1; i <= comboOp.getCheckModel().getCheckedItems().size(); i++) {
                if (i % 2 == 0)
                    s += comboOp.getCheckModel().getCheckedItems().get(i - 1).getName() + ", \n";
                else if (i == comboOp.getCheckModel().getCheckedItems().size())
                    s += comboOp.getCheckModel().getCheckedItems().get(i - 1).getName();
                else
                    s += comboOp.getCheckModel().getCheckedItems().get(i - 1).getName() + ", ";
            }
            calculatePrice(s, lblSelectedOp, comboOp, lblSelectedPrice);
        } else {
            lblSelectedOp.setText("");
            lblSelectedPrice.setText("");
        }
    }

    private void clearFieldsForResCard() {
        lblResID.setText("");
        lblConsoleRes.setText("");
        lblSelectedOp.setText("");
        lblSelectedTime.setText("");
        lblSelectedBarber.setText("");
        lblSelectedPrice.setText("");

    }

    public void setCols() {
        colStatus.setCellValueFactory(new PropertyValueFactory<>("isDone"));
        colResID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colBarber.setCellValueFactory(new PropertyValueFactory<>("barber"));
        colOps.setCellValueFactory(new PropertyValueFactory<>("ops"));

        colOpName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colOpPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    public void showServices(ActionEvent event) {
        if (!comboServices.getCheckModel().getCheckedItems().isEmpty()) {
            String s = "";
            for (int i = 1; i <= comboServices.getCheckModel().getCheckedItems().size(); i++) {
                if (i % 4 == 0)
                    s += comboServices.getCheckModel().getCheckedItems().get(i - 1).getName() + ", \n";
                else if (i == comboServices.getCheckModel().getCheckedItems().size())
                    s += comboServices.getCheckModel().getCheckedItems().get(i - 1).getName();
                else
                    s += comboServices.getCheckModel().getCheckedItems().get(i - 1).getName() + ", ";
            }
            calculatePrice(s, lblSelectedServices, comboServices, lblServicesPrice);
        } else {
            lblSelectedServices.setText("");
            lblServicesPrice.setText("");
        }
    }

    private void calculatePrice(String s, Label lblSelectedServices, CheckComboBox<Operation> comboServices, Label lblServicesPrice) {
        lblSelectedServices.setText(s);
        int totalPrice = 0;
        for (int i = 0; i < comboServices.getCheckModel().getCheckedItems().size(); i++) {
            totalPrice += comboServices.getCheckModel().getCheckedItems().get(i).getPrice();
        }
        lblServicesPrice.setText(String.valueOf(totalPrice));
    }

    public void pauseAndPlay(ActionEvent event) {
        Main.pauseAndPlay();
    }

    public void refresh(ActionEvent event) {
        loadDataForProfile();
        tableResHistory.refresh();
    }
}
