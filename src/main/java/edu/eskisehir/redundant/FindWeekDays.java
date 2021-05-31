package edu.eskisehir.redundant;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Deprecated
public class FindWeekDays {
    public static void main(String[] args) {

      /* List<String> list = new LinkedList<>();
        List<String> weekdays= new LinkedList<>();

        try (Stream<String> stream = Files.lines(Paths.get("temp.txt"))) {
            list = stream

                    .collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < list.size(); i++) {
            LocalDate dt = LocalDate.parse(list.get(i), dtf);
            DayOfWeek selectedDay = DayOfWeek.of(dt.get(ChronoField.DAY_OF_WEEK));
            switch (selectedDay) {
                case SATURDAY:
                case SUNDAY:

                    break;
                default:
                    weekdays.add(list.get(i));

            }


            }

        for (int i = 0; i < weekdays.size(); i++) {
            System.out.println(weekdays.get(i));
        }*/

        /** Random hours*/
        
      /*  String[] times={"08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30",
                "13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30",
                "19:00","19:30","20:00",};
        
        LinkedList<String> timesList=new LinkedList<>(Arrays.asList(times));



        for (int i = 0; i < 1595; i++) {
            Collections.shuffle(timesList);
            System.out.println(timesList.get(0));
        }*/

        for (int i = 0; i < 200; i++) {
            System.out.println((int) (Math.random()*7+1));
        }

    }
}
