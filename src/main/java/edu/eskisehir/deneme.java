package edu.eskisehir;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Deprecated
public class deneme {
    public static void main(String[] args) {

  /*  DataBaseOperations dataBaseOperations=new DataBaseOperations();


    List<Long> resIDs= dataBaseOperations.getAllResIDs();
        for (int i = 0; i < resIDs.size(); i++) {
            List<Integer> random= new java.util.ArrayList<>(List.of(1, -1));
            Collections.shuffle(random);
            dataBaseOperations.updateIsDone(resIDs.get(i),String.valueOf(random.get(0)));
        }
*/


       /* java.util.Date date2= new java.util.Date("2021-05-10");
        System.out.println(date2);*/

        /*DataBaseOperations dataBaseOperations = new DataBaseOperations();


        dataBaseOperations.updateIsDone(21080909301L,LocalDate.now().toString());*/

  /*    try(  Connection connection= DBConnection.connect();
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
*/
    }
}