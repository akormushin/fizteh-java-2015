package ru.fizteh.fivt.students.maked0n.Reverser;

public class CReverser {
    public static void main(String[] args) {
        for (int i = args.length - 1; i >= 0; --i) {
            String[] splitted = args[i].split("\\s+");
            for (int j = splitted.length - 1; j >= 0; --j) {
                System.out.print(splitted[j]);
            }
        }
    }
}
