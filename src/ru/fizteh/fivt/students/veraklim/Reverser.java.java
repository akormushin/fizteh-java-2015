package ru.fizteh.fivt.students.veraklim;

/**
 * Created by Vanya on 15.09.2015.
 */
public class Reverser {
    public static void main(String[] args) {
        for (int i = args.length - 1; i >= 0; i--) {
            String[] a = args[i].split("\\s+");
            for (int j = a.length - 1; j >= 0; j--)
                System.out.print(a[j] + " ");
        }
    }
}