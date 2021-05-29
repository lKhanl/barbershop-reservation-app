package edu.eskisehir;

import javafx.scene.control.Alert;
import javafx.util.converter.IntegerStringConverter;

public class CustomIntegerStringConverter extends IntegerStringConverter {
    private final IntegerStringConverter converter = new IntegerStringConverter();

    @Override
    public String toString(Integer object) {
        try {
            return converter.toString(object);
        } catch (NumberFormatException e) {
            showAlert(e);
        }
        return null;
    }

    @Override
    public Integer fromString(String string) {
        try {
            return converter.fromString(string);
        } catch (NumberFormatException e) {
            showAlert(e);
        }
        return -1;
    }

    private void showAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Number Error");
        alert.setHeaderText("Number excepted, but string provided!");
        alert.show();
    }
}