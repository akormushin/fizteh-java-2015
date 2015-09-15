package ru.fizteh.fivt.students.pashmentov.reverser;

/**
 * Created by nikita on 14.09.15.
 */
public class Reverser {
    public static void main(String[] args) {
        for (int i = args.length - 1; i >= 0; --i) {
            String parts[] = args[i].split("\\s+");
            for (int j = parts.length - 1; j >= 0; --j) {
                System.out.print(parts[j] + " ");
            }
        }
        System.out.println();
    }
}
