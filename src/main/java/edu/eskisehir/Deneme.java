package edu.eskisehir;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Deneme {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b");
        Matcher m = p.matcher("abc");

        if (m.find())
            System.out.println("Correct!");
        else
            System.out.println("cortingen");
    }
}
