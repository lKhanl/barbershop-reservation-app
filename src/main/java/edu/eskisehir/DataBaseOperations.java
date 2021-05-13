package edu.eskisehir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseOperations {

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

    public void changeCustomerInformation(){


    }

    public void makeReservation(){

    }

    public void changeReservation(){


    }

    public void addBarber(){

    }

    public void changeBarberInformation(){

    }

    public void addOperation(){

    }

    public void changeAnOperation(){

    }


}
