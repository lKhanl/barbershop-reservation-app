package edu.eskisehir;

import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class DataBaseOperations {

    public void updateTotalPrice(long reservationID) {
        String getOpIDsSQL = "SELECT OperationID FROM operation_selection WHERE ReservationID=" + reservationID;
        String totalPriceSQL = "UPDATE reservation SET TotalPrice=? WHERE ReservationID= " + reservationID;
        String getPriceSQL;
        try (Connection connection = DBConnection.connect();
             PreparedStatement totalPriceStatement = connection.prepareStatement(totalPriceSQL);
             Statement getOpIDs = connection.createStatement();
             Statement getPrice = connection.createStatement();
        ) {


            ResultSet selectedOpIDS = getOpIDs.executeQuery(getOpIDsSQL);
            ResultSet prices;
            int opID;
            int total = 0;

            while (selectedOpIDS.next()) {
                opID = selectedOpIDS.getInt("OperationID");
                getPriceSQL = "Select Price FROM operation WHERE OperationID=" + opID;
                prices = getPrice.executeQuery(getPriceSQL);
                while (prices.next()) {
                    total += prices.getInt("Price");
                }

            }

            totalPriceStatement.setInt(1, total);
            totalPriceStatement.executeUpdate();

        } catch (SQLException throwables) {
            System.out.println("Ekleme işlemi başarısız.");
            System.out.println(throwables.getMessage());
        }
        System.out.println("Ekleme işlemi başarılı.");
    }

    public void addCustomer(String customerName, String customerSurname, String email, String password) {


        try (Connection connection = DBConnection.connect();
             PreparedStatement customerStatement = connection.prepareStatement("INSERT INTO `customer`(`CustomerName`, " +
                     "`CustomerSurname`, `Email`, `Password`) VALUES (?,?,?,?)")) {

            customerStatement.setString(1, customerName);
            customerStatement.setString(2, customerSurname);
            customerStatement.setString(3, email);
            customerStatement.setString(4, password);
            customerStatement.executeUpdate();

        } catch (SQLException throwables) {
            System.out.println("Ekleme işlemi başarısız.");
            System.out.println(throwables.getMessage());
        }
        System.out.println("Ekleme işlemi başarılı.");

    }

    public void changeCustomerInformation() {


    }

    public void fillOperationSelection(long resID, int operationID) {
        long selectionID = makeSelectionID(resID, operationID);
        try (Connection connection = DBConnection.connect();
             PreparedStatement reservationStatement = connection.prepareStatement("INSERT INTO operation_selection (SelectionID ,ReservationID , OperationID) " +
                     "VALUES (?,?,?)")) {

            reservationStatement.setLong(1, selectionID);
            reservationStatement.setLong(2, resID);
            reservationStatement.setInt(3, operationID);
            reservationStatement.executeUpdate();


        } catch (SQLException throwables) {
            System.out.println("Ekleme işlemi başarısız.");
            System.out.println(throwables.getMessage());
        }
        System.out.println("Ekleme işlemi başarılı.");

    }

    public void makeReservation(Date date, Time time, int barberId, int customerId) {
        long resID = makeReservationID(date.toString(), time.toString(), barberId);

        try (Connection connection = DBConnection.connect();
             PreparedStatement reservationStatement = connection.prepareStatement("INSERT INTO reservation (ReservationID , ReservationDate, ReservationTime, BarberID, " +
                     "CustomerID) VALUES (?,?,?,?,?)")
        ) {
            reservationStatement.setLong(1, resID);
            reservationStatement.setDate(2, date);
            reservationStatement.setTime(3, time);
            reservationStatement.setInt(4, barberId);
            reservationStatement.setInt(5, customerId);

            reservationStatement.executeUpdate();

        } catch (SQLException throwables) {
            System.out.println("Ekleme işlemi başarısız.");
            System.out.println(throwables.getMessage());
        }
        System.out.println("Ekleme işlemi başarılı.");
    }

    public List<Long> getAllResIDs() {
        List<Long> resIDs = new LinkedList<>();
        try (Connection connection = DBConnection.connect();
             Statement reservationID_from_reservation = connection.createStatement()) {

            ResultSet resultSet = reservationID_from_reservation.executeQuery("Select ReservationID from reservation");
            while (resultSet.next()) {
                resIDs.add(resultSet.getLong("ReservationID"));
            }

        } catch (SQLException throwables) {
            System.out.println("Çekme işlemi başarısız");
        }
        System.out.println("Çekme işlemi başarılı");
        return resIDs;
    }

    public void changeReservation() {


    }

    public void addBarber() {

    }

    public void changeBarberInformation() {

    }

    public void addOperation() {

    }

    public void changeAnOperation() {

    }

    private long makeReservationID(String date, String time, int barberID) {
        String[] splittedDate = date.split("-");
        String[] splittedTime = time.split(":");

        String year = splittedDate[0].substring(2, 4);
        String resID = year + splittedDate[1] + splittedDate[2] + splittedTime[0] + splittedTime[1] + barberID;

        return Long.parseLong(resID);

    }

    private long makeSelectionID(long resID, int opID) {
        String selectionID = String.valueOf(resID) + String.valueOf(opID);
        return Long.parseLong(selectionID);

    }

}
