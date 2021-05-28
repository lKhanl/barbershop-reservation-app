package edu.eskisehir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Deprecated
public class AddAllCustomer {
    public static void main(String[] args) throws SQLException {
        DataBaseOperations dataBaseOperations= new DataBaseOperations();

        String mailFileName = "mails.txt";
        String passwordFileName = "passwords.txt";

        String nameFileName = "names.txt";
        String surnameFileName = "surnames.txt";


        List<String> mails = new LinkedList<>();
        List<String> passwords = new LinkedList<>();

        List<String> names = new LinkedList<>();
        List<String> surnames = new LinkedList<>();


        /** take names*/
        try (Stream<String> stream = Files.lines(Paths.get(nameFileName))) {
            names = stream

                    .collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }
        /** take surnames*/
        try (Stream<String> stream = Files.lines(Paths.get(surnameFileName))) {
            surnames = stream

                    .collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }

        /** take mails*/
        try (Stream<String> stream = Files.lines(Paths.get(mailFileName))) {
            mails = stream

                    .collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }
        /** take passwords*/
        try (Stream<String> stream = Files.lines(Paths.get(passwordFileName))) {
            passwords = stream

                    .collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 1000; i++) {
            dataBaseOperations.addCustomer(names.get(i),surnames.get(i),mails.get(i),passwords.get(i));
        }
    }
}
