package ru.fizteh.fivt.students.EGoltsova.Reverser;

/**
 * Created by cache-nez on 9/14/15.
 */
public class Reverser {
    public static void main(String[] args) {
        for (int i = args.length - 1; i >= 0; --i) {
            String[] current = args[i].split("\\s+");
            for (int print = current.length - 1; print >= 0; --print) {
                System.out.print(current[print] + " ");
            }
        }
        System.out.println();
    }
}
