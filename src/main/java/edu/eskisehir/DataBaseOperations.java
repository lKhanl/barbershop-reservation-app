package edu.eskisehir;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class DataBaseOperations {

    public Customer logIn(String email, String password) {
        Customer customer = null;
        String sql = "SELECT * FROM customer WHERE Email=" + "'" + email + "'";

        try (Connection connection = DBConnection.connect();
             Statement stmt = connection.createStatement()) {


            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                customer = new Customer(rs.getInt("CustomerID"), rs.getString("CustomerName"),
                        rs.getString("CustomerSurname"), rs.getString("Email"), rs.getString("Password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;

    }

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

    public void addCustomer(String customerName, String customerSurname, String email, String password) throws SQLException {

        try (Connection connection = DBConnection.connect();
             PreparedStatement customerStatement = connection.prepareStatement("INSERT INTO `customer`(`CustomerName`, " +
                     "`CustomerSurname`, `Email`, `Password`) VALUES (?,?,?,?)")) {

            customerStatement.setString(1, customerName);
            customerStatement.setString(2, customerSurname);
            customerStatement.setString(3, email);
            customerStatement.setString(4, password);
            customerStatement.executeUpdate();
        }
    }

    public List<Customer> getCustomers() {
        List<Customer> list = new LinkedList<>();
        String sql = "SELECT CustomerID,CustomerName,CustomerSurname,Email FROM customer";

        try (Connection connection = DBConnection.connect();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Customer(rs.getInt("CustomerID"), rs.getString("CustomerName"), rs.getString("CustomerSurname"), rs.getString("Email")));
            }

        } catch (SQLException e) {
            System.out.println("Çekme işlemi başarılı.");
            e.printStackTrace();
        }
        System.out.println("Çekme işlemi başarısız.");
        return list;
    }

    public void updateCustomer(Attribute attName, String value, int id) {
        String sql = null;
        switch (attName) {
            case NAME:
                sql = "UPDATE customer SET CustomerName=? WHERE CustomerID=?";
                break;
            case SURNAME:
                sql = "UPDATE customer SET CustomerSurname=? WHERE CustomerID=?";
                break;
            case EMAIL:
                sql = "UPDATE customer SET Email=? WHERE CustomerID=?";
                break;
            case PASSWORD:
                sql = "UPDATE customer SET Password=? WHERE CustomerID=?";

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

    public void deleteCustomer(int customerID) {
        String sql = "DELETE FROM customer WHERE CustomerID=?";
        try (Connection connection = DBConnection.connect();
             PreparedStatement deleteStatement = connection.prepareStatement(sql);
        ) {
            deleteStatement.setInt(1, customerID);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Silme işlemi başarısız.");
            e.printStackTrace();
        }
        System.out.println("Silme işlemi başarılı." + customerID);

    }

    public int addBarber(String name, String surname, int salary) {
        String sql = "INSERT INTO barber (BarberName,BarberSurname,Salary) VALUES (?,?,?) ";
        int barberID = 0;
        String getterSql = "SELECT BarberID FROM barber ORDER BY BarberID DESC LIMIT 1;";

        try (Connection connection = DBConnection.connect();
             PreparedStatement barberStatement = connection.prepareStatement(sql);
             Statement getBarberStatement = connection.createStatement();
        ) {

            barberStatement.setString(1, name);
            barberStatement.setString(2, surname);
            barberStatement.setInt(3, salary);
            barberStatement.executeUpdate();

            ResultSet resultSet = getBarberStatement.executeQuery(getterSql);

            while (resultSet.next()) {
                barberID = resultSet.getInt("BarberID");
            }

        } catch (SQLException e) {
            System.out.println("Ekleme işlemi başarısız.");
            e.printStackTrace();
        }
        System.out.println("Ekleme işlemi başarılı.");
        return barberID;
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

    public List<Time> getBusyTimes(int barberID, String date) {
        Date dateSql = Date.valueOf(date);

        List<Time> busyTimes = new LinkedList<>();
        String sql = "SELECT ReservationTime FROM reservation WHERE ReservationDate=" + "'" + dateSql + "'" + " AND BarberID=" + "'" + barberID + "'"+"AND isDone='0'";

        try (Connection connection = DBConnection.connect();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                busyTimes.add(rs.getTime("ReservationTime"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return busyTimes;
    }

    public void deleteBarber(int barberID) {
        String sql = "DELETE FROM barber WHERE BarberID=?";
        try (Connection connection = DBConnection.connect();
             PreparedStatement deleteStatement = connection.prepareStatement(sql);
        ) {
            deleteStatement.setInt(1, barberID);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Silme işlemi başarısız.");
            e.printStackTrace();
        }
        System.out.println("Silme işlemi başarılı." + barberID);

    }

    public void updateBarber(Attribute attName, String value, int id) {
        String sql = null;
        switch (attName) {
            case NAME:
                sql = "UPDATE barber SET BarberName=? WHERE BarberID=?";
                break;
            case SURNAME:
                sql = "UPDATE barber SET BarberSurname=? WHERE BarberID=?";
                break;
            case SALARY:
                sql = "UPDATE barber SET Salary=? WHERE BarberID=?";
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

    public int addOperation(String name, int price) {
        int operationID = 0;
        String sql = "INSERT INTO operation (OperationName,Price) VALUES (?,?) ";
        String getterSql = "SELECT OperationID FROM operation ORDER BY OperationID DESC LIMIT 1;";

        try (Connection connection = DBConnection.connect();
             PreparedStatement operationStatement = connection.prepareStatement(sql);
             Statement getOperationsStatement = connection.createStatement();
        ) {

            operationStatement.setString(1, name);
            operationStatement.setInt(2, price);
            operationStatement.executeUpdate();

            ResultSet resultSet = getOperationsStatement.executeQuery(getterSql);

            while (resultSet.next()) {
                operationID = resultSet.getInt("OperationId");
            }


        } catch (SQLException e) {
            System.out.println("Ekleme işlemi başarısız.");
            e.printStackTrace();
        }
        System.out.println("Ekleme işlemi başarılı.");
        return operationID;

    }

    public List<Operation> getOperations() {
        List<Operation> operations = new LinkedList<>();
        String sql = "SELECT * FROM operation";

        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                operations.add(new Operation(resultSet.getInt("OperationID"), resultSet.getString("OperationName"), resultSet.getInt("Price")));
            }

        } catch (SQLException e) {
            System.out.println("Çekme işlemi başarısız.");
            e.printStackTrace();
        }
        System.out.println("Çekme işlemi başarılı.");
        return operations;
    }

    public void deleteOperation(int operationID) {
        String sql = "DELETE FROM operation WHERE OperationID=?";
        try (Connection connection = DBConnection.connect();
             PreparedStatement deleteStatement = connection.prepareStatement(sql);
        ) {
            deleteStatement.setInt(1, operationID);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Silme işlemi başarısız.");
            e.printStackTrace();
        }
        System.out.println("Silme işlemi başarılı." + operationID);
    }

    public void updateOperation(Attribute attName, String value, int id) {
        String sql = null;
        switch (attName) {
            case NAME:
                sql = "UPDATE operation SET OperationName=? WHERE OperationID=?";
                break;
            case PRICE:
                sql = "UPDATE operation SET Price=? WHERE OperationID=?";
                break;
        }
        try (Connection connection = DBConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update işlemi başarısız.");
            e.printStackTrace();
        }
        System.out.println("Update işlemi başarılı.");
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

    public long bookReservation(String date, Time time, int barberId, int customerId, List<Integer> operationID) {
        long resID = makeReservationID(date, time.toString(), barberId);
        Date dateSQL = Date.valueOf(date);

        try (Connection connection = DBConnection.connect();
             PreparedStatement reservationStatement = connection.prepareStatement("INSERT INTO reservation (ReservationID , ReservationDate, ReservationTime, BarberID, " +
                     "CustomerID) VALUES (?,?,?,?,?)");
        ) {


            reservationStatement.setLong(1, resID);
            reservationStatement.setDate(2, dateSQL);
            reservationStatement.setTime(3, time);
            reservationStatement.setInt(4, barberId);
            reservationStatement.setInt(5, customerId);

            reservationStatement.executeUpdate();
            for (Integer integer : operationID) {
                fillOperationSelection(resID, integer);
            }


            updateTotalPrice(resID);

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

    public List<Customer> searchName(String name) {
        List<Customer> list = new LinkedList<>();
        String sql = "SELECT CustomerID,CustomerName,CustomerSurname,Email FROM customer WHERE CustomerName or CustomerSurname or Email LIKE " + "'%" + name + "%'";
        try (Connection connection = DBConnection.connect();
             Statement stmt = connection.createStatement()) {

            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                list.add(new Customer(resultSet.getInt("CustomerID"), resultSet.getString("CustomerName"),
                        resultSet.getString("CustomerSurname"), resultSet.getString("Email")));
            }

        } catch (SQLException e) {
            System.out.println("Search işlemi başarısız.");
            e.printStackTrace();
        }
        System.out.println("Search işlemi başarılı.");
        return list;
    }


}
