package edu.eskisehir.redundant;

import edu.eskisehir.db.DataBaseOperations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Deprecated
public class AddAllOperationSelections {
    public static void main(String[] args) throws FileNotFoundException {

        DataBaseOperations dataBaseOperations = new DataBaseOperations();

        /** Create resIDs and operations file */

       /* List<Long> reservationIDs = dataBaseOperations.getAllResIDs();
        List<Integer> operations = new LinkedList<>();

        for (int i = 0; i < 2423; i++) {
            Collections.shuffle(reservationIDs);
            reservationIDs.add(reservationIDs.get(i));
        }

        Collections.shuffle(reservationIDs);

        File resIDsTxt = new File("resIDs.txt");
        File operationsTxt = new File("operations.txt");

        PrintWriter printResIDs = new PrintWriter(resIDsTxt);

        for (int i = 0; i < reservationIDs.size(); i++) {
            printResIDs.println(reservationIDs.get(i));
        }
        printResIDs.close();

        PrintWriter printOperations = new PrintWriter(operationsTxt);
        for (int i = 0; i < reservationIDs.size(); i++) {
            printOperations.println((int) (Math.random() * 7 + 1));
        }
        printOperations.close();*/



      String resIDsFileName = "resIDs.txt";
        String operationsFileName = "operations.txt";


        List<String> resIDs = new LinkedList<>();
        List<String> operations = new LinkedList<>();


        try (Stream<String> stream = Files.lines(Paths.get(resIDsFileName))) {
            resIDs = stream

                    .collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }


        try (Stream<String> stream = Files.lines(Paths.get(operationsFileName))) {
            operations = stream

                    .collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < resIDs.size(); i++) {
            dataBaseOperations.fillOperationSelection(Long.parseLong(resIDs.get(i)), Integer.parseInt(operations.get(i)));
        }


    }
}

