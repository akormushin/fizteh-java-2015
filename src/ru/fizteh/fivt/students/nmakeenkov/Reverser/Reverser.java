package ru.fizteh.fivt.students.nmakeenkov.Reverser;

public class Reverser {
    public static void main(String[] args) {
        for (int i = args.length - 1; i >= 0; i--) {
            String[] curArg = args[i].split("\\s+");
            for (int j = curArg.length - 1; j >= 0; j--)
                System.out.print(curArg[j] + " ");
        }
        System.out.println();
    }
}
