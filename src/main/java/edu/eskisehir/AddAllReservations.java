package edu.eskisehir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Deprecated
public class AddAllReservations {
    public static void main(String[] args) {
        DataBaseOperations dataBaseOperations= new DataBaseOperations();

        String dateFileName = "dates.txt";
        String timesFileName = "times.txt";

        String customersFileName = "customers.txt";
        String barbersFileName = "barbers.txt";


        List<String> dates = new LinkedList<>();
        List<String> times = new LinkedList<>();

        List<String> customers = new LinkedList<>();
        List<String> barbers = new LinkedList<>();


        /** take dates*/
        try (Stream<String> stream = Files.lines(Paths.get(dateFileName))) {
            dates = stream

                    .collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < dates.size(); i++) {
            System.out.println(dates.get(i));
        }
        /** take times*/
        try (Stream<String> stream = Files.lines(Paths.get(timesFileName))) {
            times = stream

                    .collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }

        /** take customers*/
        try (Stream<String> stream = Files.lines(Paths.get(customersFileName))) {
            customers = stream

                    .collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }
        /** take barbers*/
        try (Stream<String> stream = Files.lines(Paths.get(barbersFileName))) {
            barbers = stream

                    .collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < dates.size(); i++) {
         dataBaseOperations.makeReservation(Date.valueOf(dates.get(i)), Time.valueOf(times.get(i)),Integer.parseInt(barbers.get(i)),Integer.parseInt(customers.get(i)));
        }

    }
}
