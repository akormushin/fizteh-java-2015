/**
 * Created by zemen on 12.09.15.
 */

package ru.fizteh.fivt.students.ksemenov.reverser;

/**
 * Main class for task Reverser.
 */
public final class Reverser {
    /**
     * Default constructor for utility class is prohibited by checkstyle.
     */
    private Reverser() {
    }

    /**
     * Reverses arguments from command line.
     *
     * @param args Arguments to reverse.
     */
    public static void main(final String[] args) {
        boolean firstArg = true;
        for (int i = args.length - 1; i >= 0; --i) {
            String[] splitted = args[i].trim().split("\\s+");
            for (int j = splitted.length - 1; j >= 0; --j) {
                if (firstArg) {
                    firstArg = false;
                } else {
                    System.out.print(' ');
                }
                System.out.print(splitted[j]);
            }
        }
        System.out.println();
    }
}
