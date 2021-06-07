package edu.eskisehir.redundant;

import edu.eskisehir.db.DBConnection;
import edu.eskisehir.db.DataBaseOperations;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Deprecated
public class deneme {
    public static void main(String[] args) {
        /** Update isDone and totalPrice*/
        /* DataBaseOperations dataBaseOperations=new DataBaseOperations();


    List<Long> resIDs= dataBaseOperations.getAllResIDs();
        for (int i = 0; i < resIDs.size(); i++) {
            List<Integer> random= new java.util.ArrayList<>(List.of(1, -1));
            Collections.shuffle(random);
            dataBaseOperations.updateIsDone(resIDs.get(i),String.valueOf(random.get(0)));
        }



       java.util.Date date2= new java.util.Date("2021-05-10");
        System.out.println(date2);


        dataBaseOperations.updateIsDone(21080909301L,LocalDate.now().toString());

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
*/

        /** Busy times*/
    /*    DataBaseOperations dataBaseOperations = new DataBaseOperations();
        List<Time> list = dataBaseOperations.getBusyTimes(7, "2021-12-31");
        for (Time time : list) {
            System.out.println(time);
        }*/

        /** Book reservation*/
       /* DataBaseOperations dataBaseOperations = new DataBaseOperations();
        Date date = Date.valueOf("2022-1-1");
        Time time=Time.valueOf("13:00:00");
        List<Integer> list= new LinkedList<>();
        list.add(1);
        list.add(2);

        dataBaseOperations.bookReservation(date,time,1,601,list);*/
       /* DataBaseOperations dataBaseOperations = new DataBaseOperations();

        List<Long> resIDs = dataBaseOperations.getAllResIDs();


        for (int i = 0; i < resIDs.size(); i++) {
            long resID = resIDs.get(i);
            dataBaseOperations.isDoneTemp(resID);


        }*/

       /* DataBaseOperations dataBaseOperations1 = new DataBaseOperations();
        List<Reservation> list = dataBaseOperations1.searchReservation("21010610001");
        System.out.println(list.get(0).getCustomer().getName());*/
    /*    DataBaseOperations dataBaseOperations1 = new DataBaseOperations();
        System.out.println(dataBaseOperations1.averageMonthlyIncome("2021"));*/
       /* LocalTime localDate = LocalTime.now();
        String current = localDate.getHour() + ":" + localDate.getMinute() + ":" + localDate.getSecond();
        List<String> temp = new java.util.ArrayList<>(List.of("08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00", "11:00:00", "11:30:00", "12:00:00",
                "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00", "15:30:00", "16:00:00", "16:30:00", "17:00:00", "17:30:00",
                "18:00:00", "18:30:00", "19:00:00", "19:30:00", "20:00:00"));

        temp.removeIf(time -> (Time.valueOf(time).compareTo(Time.valueOf(current)) < 0));

        for (int i = 0; i < temp.size(); i++) {
            System.out.println(temp.get(i));
        }*/


        try (Connection connection = DBConnection.connect();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT  * FROM deneme");
            System.out.println(rs.next());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
