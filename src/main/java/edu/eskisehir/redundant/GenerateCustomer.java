package edu.eskisehir.redundant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Deprecated
public class GenerateCustomer {
    public static void main(String[] args) throws FileNotFoundException {
        String nameFileName = "names.txt";
        String surnameFileName = "surnames.txt";

        List<String> names = new LinkedList<>();
        List<String> surnames = new LinkedList<>();

        List<String> mails = new LinkedList<>();
        List<String> passwords = new LinkedList<>();


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

        /** Generate mails and passwords*/
        for (int i = 0; i <names.size() ; i++) {
            mails.add(mailGenerator(names.get(i),surnames.get(i)));
            passwords.add(passwordGenerator(names.get(i),surnames.get(i)));
        }
        File mailsTxt = new File("mails.txt");
        File passwordsTxt = new File("passwords.txt");

        PrintWriter printMails = new PrintWriter(mailsTxt);
        for (int i = 0; i < mails.size(); i++) {
            printMails.println(mails.get(i));
        }
        printMails.close();

        PrintWriter printPasswords = new PrintWriter(passwordsTxt);
        for (int i = 0; i < passwords.size(); i++) {
            printPasswords.println(passwords.get(i));
        }

        printPasswords.close();

    }

    public static String mailGenerator(String name, String surname) {
        ArrayList<String> mailEnds = new ArrayList<>();
        mailEnds.add("outlook.com");
        mailEnds.add("gmail.com");
        mailEnds.add("yahoo.com.");
        mailEnds.add("msn.com");
        mailEnds.add("windowslive.com");
        mailEnds.add("live.com");

        Collections.shuffle(mailEnds);

        String selected = mailEnds.get(0);



        return name.toLowerCase() + surname.toLowerCase() + "@" + selected;

    }

    public static String passwordGenerator(String name, String surname) {
        String[] characters = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        int random1 = (int) (Math.random() * 10);
        int random2 = (int) (Math.random() * 10);
        int random3 = (int) (Math.random() * 10);
        int random4 = (int) (Math.random() * 10);

        return name.toLowerCase() + surname.toLowerCase() + random1 + random2 + random3 + random4;
    }


}
