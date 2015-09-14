package ru.fizteh.fivt.students.xmanatee.reverser;

/**
 * Class Reverser for reversing arguments of the command line.
 */

final class Reverser {
    /**
     * also for checkstyle...
     */
    private Reverser() {
        // Prevent instantiation
        // Optional: throw an exception e.g. AssertionError
        // if this ever *is* called
    }
    /**
     * Method main.
     * @param args **arguments in the command line**
     */
    public static void main(final String... args) {
        for (int i = args.length - 1; i >= 0; i--) {
            String[] tokens = args[i].split("\\s+");
            for (int j = tokens.length - 1; j >= 0; j--) {
                System.out.print(tokens[j] + " ");
            }
        }
    }

}
