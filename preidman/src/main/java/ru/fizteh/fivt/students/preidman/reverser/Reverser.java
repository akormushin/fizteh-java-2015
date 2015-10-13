package ru.fizteh.fivt.students.preidman.reverser;


import java.util.Arrays;
import java.util.Collections;

public class Reverser {
    public static void main(String[] args) {
        for (int i = args.length - 1; i >= 0; i--) {

            String[] sIn = args[i].split("[\\s+\\\\\"]");

            Collections.reverse(Arrays.asList(sIn));

            for (int j = 0; j < sIn.length; j++) {
                System.out.print(sIn[j] + " ");
            }
        }
    }
}
