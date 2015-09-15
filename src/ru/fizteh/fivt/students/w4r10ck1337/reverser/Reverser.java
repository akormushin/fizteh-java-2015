package ru.fizteh.fivt.students.w4r10ck1337.reverser;

/**
 * Created by Zuev Stepan on 15.09.2015.
 */

public class Reverser {

    public static void main(String[] args) {
        for (int i = args.length - 1; i >= 0; i--) {
            String[] words = args[i].replaceAll("[^\\d\\.\\-\\s]", "").split("\\s+");
            for (int j = words.length - 1; j >= 0; j--) {
                System.out.print(words[j] + " ");
            }
        }
    }
}
