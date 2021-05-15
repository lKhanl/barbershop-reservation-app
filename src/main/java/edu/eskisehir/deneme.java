package edu.eskisehir;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Deprecated
public class deneme {
    public static void main(String[] args) {


       try(  Connection connection= DBConnection.connect();
             Statement statement=connection.createStatement();
       ) {

          ResultSet resultSet= statement.executeQuery("SELECT ReservationID FROM reservation");

          while (resultSet.next()){
              long resID=resultSet.getLong("ReservationID");
              updateTotalPrice(resID,connection);
          }


       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }


    }
    public static void updateTotalPrice(long reservationID,Connection connection) {
        String getOpIDsSQL = "SELECT OperationID FROM operation_selection WHERE ReservationID=" + reservationID;
        String totalPriceSQL = "UPDATE reservation SET TotalPrice=? WHERE ReservationID= " + reservationID;
        String getPriceSQL;
        try (
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

}
