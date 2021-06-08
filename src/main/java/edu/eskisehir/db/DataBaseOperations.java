package edu.eskisehir.db;

import edu.eskisehir.entity.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DataBaseOperations {

    public Customer logIn(String email) {
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
            customerStatement.setString(4, getMd5(password));
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
            System.out.println("Çekme işlemi başarısız.");
            e.printStackTrace();
        }
        System.out.println("Çekme işlemi başarılı.");

        return list;
    }

    public Customer getCustomerByID(int customerID) {
        String sql = "SELECT CustomerID,CustomerName,CustomerSurname,Email FROM customer WHERE  CustomerID=" + "'" + customerID + "'";
        Customer customer = null;
        try (Connection connection = DBConnection.connect();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);


            while (rs.next()) {
                customer = new Customer(rs.getInt("CustomerID"), rs.getString("CustomerName"),
                        rs.getString("CustomerSurname"), rs.getString("Email"));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return customer;
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

    @Deprecated
    public void isDoneTemp(long resID) {

        String sql = "SELECT isDone FROM reservation WHERE ReservationID=" + "'" + resID + "'";
        String sql2 = "UPDATE reservation SET isDone=? WHERE ReservationID=" + "'" + resID + "'";

        try (Connection con = DBConnection.connect();
             Statement st = con.createStatement();
             PreparedStatement st2 = con.prepareStatement(sql2)
        ) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String strIsDone = rs.getString("isDone");
                switch (strIsDone) {
                    case "1": {
                        st2.setString(1, "Done");
                        st2.executeUpdate();
                        break;
                    }
                    case "0": {
                        st2.setString(1, "Waiting");
                        st2.executeUpdate();
                        break;
                    }
                    case "-1": {
                        st2.setString(1, "Canceled");
                        st2.executeUpdate();
                        break;
                    }

                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Time> getBusyTimes(int barberID, String date) {
        Date dateSql = Date.valueOf(date);

        List<Time> busyTimes = new LinkedList<>();
        String sql = "SELECT ReservationTime FROM reservation WHERE ReservationDate=" + "'" + dateSql + "'" + " AND BarberID=" + "'" + barberID + "'" + "AND isDone='Waiting'";

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

    public Barber getBarberByID(int barberID) {
        String sql = "SELECT * FROM barber WHERE BarberId=" + "'" + barberID + "'";
        Barber barber = null;

        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement();) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                barber = new Barber(rs.getInt("BarberID"), rs.getString("BarberName"),
                        rs.getString("BarberSurname"), rs.getInt("Salary"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return barber;
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

    public Operation getOperationByID(int operationID) {
        String sql = "SELECT * FROM operation WHERE OperationID=" + "'" + operationID + "'";
        Operation operation = null;

        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement();) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                operation = new Operation(rs.getInt("OperationID"), rs.getString("OperationName"), rs.getInt("Price"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return operation;
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

    public void updateCustomersOperations(long resID, List<Operation> newOperations) {
        List<Integer> IDs = new LinkedList();
        for (Operation newOperation : newOperations) {
            IDs.add(newOperation.getId());
        }

        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement()) {
            String sql = "SELECT OperationID from operation_selection WHERE ReservationID=  " + resID;
            ResultSet rs = statement.executeQuery(sql);
            List<Integer> operations = new LinkedList<>();
            while (rs.next()) {
                operations.add(rs.getInt("OperationID"));
            }

            if (IDs.size() - operations.size() > 0) {
                IDs.removeAll(operations);
                for (int i = 0; i < IDs.size(); i++) {
                    fillOperationSelection(resID, IDs.get(i));
                }
            } else {
                operations.removeAll(IDs);

                for (int i = 0; i < operations.size(); i++) {
                    String sql2 = "SELECT SelectionID FROM operation_selection WHERE ReservationID=" + "'" + resID +
                            "'" + " AND OperationID=" + "'" + operations.get(i) + "'";
                    ResultSet rs2 = statement.executeQuery(sql2);
                    while (rs2.next()) {
                        deleteOperationSelection(rs2.getLong("SelectionID"));
                    }
                }
            }

            updateTotalPrice(resID);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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

    public void deleteOperationSelection(long selectionID) {
        String sql = "DELETE FROM operation_selection WHERE SelectionID=?";

        try (Connection connection = DBConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setLong(1, selectionID);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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
        String updateIsDoneSql = "UPDATE reservation SET isDone=? WHERE ReservationID=  " + reservationID;

        try (Connection connection = DBConnection.connect();
             PreparedStatement updateIsDoneStatement = connection.prepareStatement(updateIsDoneSql);
        ) {
            updateIsDoneStatement.setString(1, isDone);
            updateIsDoneStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Update işlemi başarısız.");
        }
        System.out.println("Update işlemi başarılı.");
    }

    public List<Reservation> adminResList() {
        String sql = "SELECT * FROM reservation INNER JOIN barber ON " +
                "reservation.BarberID=barber.BarberID";

        List<Reservation> res = new LinkedList<>();
        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement();
             Statement statement2 = connection.createStatement();
             Statement statement3 = connection.createStatement();
        ) {
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                int cid = rs.getInt("CustomerID");
                long rid = rs.getLong("ReservationID");
                Date date = rs.getDate("ReservationDate");
                Time time = rs.getTime("ReservationTime");
                int cost = rs.getInt("TotalPrice");
                String isDone = rs.getString("isDone");
                Barber barber = new Barber(rs.getInt("BarberID"), rs.getString("BarberName"), rs.getString("BarberSurname"), rs.getInt("Salary"));
                String sql2 = "SELECT operation.Price,operation.OperationID,operation.OperationName FROM `operation_selection` " +
                        "INNER JOIN operation ON operation_selection.OperationID=operation.OperationID WHERE ReservationID=" + "'" + rid + "'";
                List<Operation> ops = new LinkedList<>();
                ResultSet rs2 = statement2.executeQuery(sql2);
                while (rs2.next()) {
                    Operation operation = new Operation(rs2.getInt("OperationID"), rs2.getString("OperationName"), rs2.getInt("Price"));
                    ops.add(operation);
                }
                String sql3 = "SELECT CustomerName, CustomerSurname, Email FROM customer WHERE CustomerID=" + "'" + cid + "'";
                ResultSet rs3 = statement3.executeQuery(sql3);
                Customer customer = null;
                while (rs3.next()) {
                    customer = new Customer(cid, rs3.getString("CustomerName"), rs3.getString("CustomerSurname"), rs3.getString("Email"));
                }
                Reservation reservation = new Reservation(customer, rid, date, time, cost, barber, ops, isDone);
                res.add(reservation);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return res;
    }

    public List<Reservation> fillResHistory(int customerID) {
        List<Reservation> history = new LinkedList<>();
        String sql = "SELECT * FROM reservation INNER JOIN barber ON " +
                "reservation.BarberID=barber.BarberID WHERE CustomerID=" + "'" + customerID + "'";


        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement();
             Statement statement2 = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(sql);
            //
            while (rs.next()) {
                long id = rs.getLong("ReservationID");
                Date date = rs.getDate("ReservationDate");
                Time time = rs.getTime("ReservationTime");
                int cost = rs.getInt("TotalPrice");
                Barber barber = new Barber(rs.getInt("BarberID"), rs.getString("BarberName"), rs.getString("BarberSurname"), rs.getInt("Salary"));
                List<Operation> ops = new LinkedList<>();

                String sql2 = "SELECT operation.Price,operation.OperationID,operation.OperationName FROM `operation_selection` " +
                        "INNER JOIN operation ON operation_selection.OperationID=operation.OperationID WHERE ReservationID=" + "'" + id + "'";
                ResultSet rs2 = statement2.executeQuery(sql2);
                while (rs2.next()) {
                    Operation operation = new Operation(rs2.getInt("OperationID"), rs2.getString("OperationName"), rs2.getInt("Price"));
                    ops.add(operation);
                }

                String isDone = rs.getString("isDone");

                Reservation reservation = new Reservation(id, date, time, cost, barber, ops, isDone);
                history.add(reservation);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return history;
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

    public List<Customer> searchCustomer(String name) {
        List<Customer> list = new LinkedList<>();
        String sql = "SELECT CustomerID,CustomerName,CustomerSurname,Email FROM customer WHERE CustomerName LIKE '%" + name + "%'" +
                " OR CustomerSurname LIKE '%" + name + "%' OR Email LIKE '%" + name + "%'";
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

    public List<Reservation> searchReservation(String name) {
        List<Reservation> list = new LinkedList<>();
        String sql = "SELECT * FROM adminres WHERE ReservationID LIKE '%" + name + "%'" +
                " OR BarberName LIKE '%" + name + "%' OR BarberSurname LIKE '%" + name + "%' OR CustomerName LIKE '%" +
                name + "%' OR CustomerSurname LIKE '%" + name + "%' OR isDone LIKE '%" + name + "%'";


        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement();
             Statement statement2 = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int cid = rs.getInt("CustomerID");
                String customerName = rs.getString("CustomerName");
                String customerSurname = rs.getString("CustomerSurname");
                String email = rs.getString("Email");
                long rid = rs.getLong("ReservationID");
                Date date = rs.getDate("ReservationDate");
                Time time = rs.getTime("ReservationTime");
                int cost = rs.getInt("TotalPrice");
                String isDone = rs.getString("isDone");
                int bid = rs.getInt("BarberID");
                String barberName = rs.getString("BarberName");
                String barberSurname = rs.getString("BarberSurname");
                int salary = rs.getInt("Salary");
                String sql2 = "SELECT operation.Price,operation.OperationID,operation.OperationName FROM `operation_selection` " +
                        "INNER JOIN operation ON operation_selection.OperationID=operation.OperationID WHERE ReservationID=" + "'" + rid + "'";
                List<Operation> ops = new LinkedList<>();
                ResultSet rs2 = statement2.executeQuery(sql2);
                while (rs2.next()) {
                    Operation operation = new Operation(rs2.getInt("OperationID"), rs2.getString("OperationName"), rs2.getInt("Price"));
                    ops.add(operation);
                }
                Reservation reservation = new Reservation(new Customer(cid, customerName, customerSurname, email), rid, date, time, cost, new Barber(bid, barberName, barberSurname, salary), ops, isDone);
                list.add(reservation);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public List<String> getExistingYear() {
        List<String> years = new LinkedList<>();
        String sql = "SELECT DISTINCT YEAR(ReservationDate) AS 'year' FROM `reservation` ORDER BY `year` ASC";
        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                years.add(resultSet.getString("year"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return years;
    }

    public Operation mostSelectedOperation(String year, int month) {
        int most = -1;
        String sql = "SELECT operation_selection.OperationID, COUNT(operation_selection.OperationID) AS freq FROM `reservation`" +
                "INNER JOIN operation_selection ON operation_selection.ReservationID=reservation.ReservationID " +
                "WHERE reservation.isDone='Done'" +
                "AND YEAR(reservation.ReservationDate)= " + "'" + year + "' " +
                "AND MONTH(reservation.ReservationDate)= " + "'" + month + "' " +
                "GROUP BY operation_selection.OperationID ORDER BY freq DESC ";

        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement();) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                most = rs.getInt("OperationID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return getOperationByID(most);
    }

    public Barber mostSelectedBarber(String year, int month) {
        int most = -1;
        String sql = "SELECT barber.BarberID, COUNT(barber.BarberID) AS freq FROM reservation " +
                "INNER JOIN barber ON barber.BarberID=reservation.BarberID " +
                "WHERE reservation.isDone='Done'" +
                "AND YEAR(reservation.ReservationDate)= " + "'" + year + "' " +
                "AND MONTH(reservation.ReservationDate)= " + "'" + month + "' " +
                "GROUP BY barber.BarberID ORDER BY freq DESC ";


        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement();) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                most = rs.getInt("BarberID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return getBarberByID(most);
    }

    public Time busiestTime() {
        List<Time> times = new LinkedList<>();
        List<String> freqs = new LinkedList<>();

        String sql = "SELECT reservation.ReservationTime, " +
                "COUNT(reservation.ReservationTime) AS freq FROM reservation " +
                "WHERE reservation.isDone='Done' " +
                "GROUP BY reservation.ReservationTime ORDER BY freq DESC ";
        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement();) {
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                times.add(rs.getTime("ReservationTime"));
                freqs.add(rs.getString("freq"));
            }
            if (freqs.stream()
                    .distinct()
                    .count() <= 1)
                return null;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return times.get(0);

    }

    public double averageMonthlyIncome(String year) {
        int currentMonth = LocalDate.now().getMonth().getValue();
        double income = 0;
        String sql = "SELECT SUM(TotalPrice) AS 'income' FROM `reservation` WHERE isDone='Done' AND YEAR(ReservationDate)=" + "'" + year + "'";

        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement();) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                income = rs.getInt("income");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (Integer.parseInt(year) < LocalDate.now().getYear())
            return income / 12;
        else
            return income / currentMonth;
    }

    public Customer mostVisitedCustomer() {
        int most = -1;
        String sql = "SELECT CustomerID, operation.OperationID, COUNT(adminres.CustomerID) AS 'freq' " +
                "FROM adminres " +
                "INNER JOIN operation_selection ON operation_selection.ReservationID=adminres.ReservationID " +
                "INNER JOIN operation ON operation_selection.OperationID=operation.OperationID " +
                "WHERE adminres.isDone='Done' " +
                "GROUP BY adminres.CustomerID ORDER BY freq DESC " +
                "LIMIT 1";

        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement();) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                most = rs.getInt("CustomerID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return getCustomerByID(most);
    }

    public String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
