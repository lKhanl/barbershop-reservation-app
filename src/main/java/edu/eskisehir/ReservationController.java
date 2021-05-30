package edu.eskisehir;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.IndexedCheckModel;

import javax.imageio.ImageIO;


import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    int cid = MainController.cid;
    DataBaseOperations db = new DataBaseOperations();
    List<Time> allTimes;
    ObservableList<Reservation> resData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCols();

        setComboBoxesStyle();
        loadDataForProfile();
        loadDataForRes();
        loadDataForServices();
    }

    private void loadDataForProfile() {
        List<Reservation> reservations = db.fillResHistory(cid);
        for (Reservation reservation : reservations) {
            if (reservation.getIsDone().equals("-1")) {
                reservation.setIsDone("Cancel");
            } else if (reservation.getIsDone().equals("0")) {
                reservation.setIsDone("Waiting");
            } else {
                reservation.setIsDone("Done");
            }
        }
        resData = FXCollections.observableArrayList(reservations);
        tableResHistory.setItems(resData);

        colDate.setSortType(TableColumn.SortType.ASCENDING);
        tableResHistory.getSortOrder().add(colDate);
        tableResHistory.sort();
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

        logo.setImage(new Image(Objects.requireNonNull(getClass().getResource("media/logo.jpg")).toExternalForm()));
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
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResource("media/error.png")).toString()));
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
            saveAsPng(event);
            clearFields();

            loadDataForProfile();

            lblConsoleRes.setTextFill(Color.GREEN);
            lblConsoleRes.setText("Successful!");
        } else {
            lblConsoleRes.setTextFill(Color.RED);
            lblConsoleRes.setText("Some fields are empty!");
        }

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
            List<Time> avTime = new LinkedList<>();
            List<Time> busyTimes = db.getBusyTimes(comboBarbers.getValue().getId(), resDate.getValue().toString());

            avTime.addAll(allTimes);
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

    private void clearFields() {
        lblResID.setText("");
        lblConsoleRes.setText("");
        lblSelectedOp.setText("");
        lblSelectedTime.setText("");
        lblSelectedDate.setText("");
        lblSelectedBarber.setText("");

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

}
