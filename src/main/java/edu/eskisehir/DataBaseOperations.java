package edu.eskisehir;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

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

        } catch (SQLException e) {
            System.out.println("Update işlemi başarısız.");
            System.out.println(e.getMessage());
        }
        System.out.println("Update işlemi başarılı.");
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

        } catch (SQLException e) {
            System.out.println("Ekleme işlemi başarısız.");
            System.out.println(e.getMessage());
        }
        System.out.println("Ekleme işlemi başarılı.");

    }

    public void addBarber(String name, String surname, int salary) {
        String sql = "INSERT INTO barber (BarberName,BarberSurname,Salary) VALUES (?,?,?) ";
        try (Connection connection = DBConnection.connect();
             PreparedStatement barberStatement = connection.prepareStatement(sql)
        ) {

            barberStatement.setString(1, name);
            barberStatement.setString(2, surname);
            barberStatement.setInt(3, salary);
            barberStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Ekleme işlemi başarısız.");
            e.printStackTrace();
        }
        System.out.println("Ekleme işlemi başarılı.");
    }

    public void addOperation(String name, int price) {
        String sql = "INSERT INTO operation (OperationName,Price) VALUES (?,?) ";

        try (Connection connection = DBConnection.connect();
             PreparedStatement operationStatement = connection.prepareStatement(sql)
        ) {

            operationStatement.setString(1, name);
            operationStatement.setInt(2, price);
            operationStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Ekleme işlemi başarısız.");
            e.printStackTrace();
        }
        System.out.println("Ekleme işlemi başarılı.");

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


        } catch (SQLException e) {
            System.out.println("Ekleme işlemi başarısız.");
            System.out.println(e.getMessage());
        }
        System.out.println("Ekleme işlemi başarılı.");

    }

    public long bookReservation(Date date, Time time, int barberId, int customerId) {
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

        } catch (SQLException e) {
            System.out.println("Rezervation işlemi başarısız.");
            System.out.println(e.getMessage());
        }
        System.out.println("Rezervation işlemi başarılı.");
        return resID;
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


    public void updateIsDone(long reservationID, String isDone) {
        Date todaySql = Date.valueOf(LocalDate.now().toString());
        Time todayTime = Time.valueOf(LocalTime.now());

        String getDateSql = "SELECT ReservationDate FROM reservation WHERE ReservationID=" + reservationID;
        String getTimeSql = "SELECT ReservationTime FROM reservation WHERE ReservationID=" + reservationID;
        String updateIsDoneSql = "UPDATE reservation SET isDone=? WHERE ReservationID=  " + reservationID;
        Date date = null;
        Time time = null;

        try (Connection connection = DBConnection.connect();
             PreparedStatement updateIsDoneStatement = connection.prepareStatement(updateIsDoneSql);
        ) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getDateSql);

            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(getTimeSql);


            while (resultSet.next()) {
                date = resultSet.getDate("ReservationDate");
            }

            while (resultSet1.next()) {
                time = resultSet1.getTime("ReservationTime");
            }

            if (todaySql.compareTo(date) > 0 && (isDone.equals("1") || isDone.equals("-1"))) {//geçmiş
                updateIsDoneStatement.setString(1, isDone);
                updateIsDoneStatement.executeUpdate();
            } else if (todaySql.compareTo(date) < 0 && (isDone.equals("1") || isDone.equals("-1"))) { //gelecek
                System.out.println("Adamın tarihi gelmedi");
            } else if (todaySql.compareTo(date) == 0) {
                if (todayTime.compareTo(time) > 0) { //zaman geçti
                    updateIsDoneStatement.setString(1, isDone);
                    updateIsDoneStatement.executeUpdate();
                } else if (todayTime.compareTo(time) < 0) { //zaman geçmedi
                    System.out.println("Adamın tarihi gelmedi");
                }
            } else {
                System.out.println("Geçersiz");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Update işlemi başarısız.");
        }
        System.out.println("Update işlemi başarılı.");
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

    public boolean checkAdmin(String userName, String pass) {
        String sql = "SELECT * FROM admin";
        try (Connection connection = DBConnection.connect();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
//                System.out.println(rs.getString("UserName"));
                if (rs.getString("UserName").equals(userName) && rs.getString("Password").equals(pass)) {
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public List<Barber> getBarbers() {
        List<Barber> list = new LinkedList<>();
        String sql = "SELECT * FROM barber";

        try (Connection connection = DBConnection.connect();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Barber(rs.getInt("BarberID"), rs.getString("BarberName"), rs.getString("BarberSurname"), rs.getInt("Salary")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public void updateBarber(Attribute attName, String value, int id) {
        String sql = null;
        switch (attName) {
            case NAME:
                sql = "UPDATE barber SET BarberName=? WHERE BarberID=?";

                break;
            case SURNAME:
                break;
            case SALARY:
                break;
        }
        try (Connection connection = DBConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
