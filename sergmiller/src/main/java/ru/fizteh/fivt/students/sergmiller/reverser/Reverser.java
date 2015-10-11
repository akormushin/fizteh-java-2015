package ru.fizteh.fivt.students.sergmiller.reverser;
import java.util.ArrayList;

/**
 * Created by sergmiller on 12.09.15...
 */

final class Reverser {
    /**
     * codestyle comment...
     */
    private Reverser() {
    }

    /**
     * @param args is arguments from terminal for Reverser
     */
    public static void main(final String[] args) {
        ArrayList<String> parsedNums = new ArrayList<String>();

        //System.out.println("Hello World!");
        for (int i = 0; i < args.length; ++i) {
            String[] parsedArgs = args[i].split("(\\s)+");

            for (int j = 0; j < parsedArgs.length; ++j) {
                parsedNums.add(parsedArgs[j]);
            }
        }

        for (int i = parsedNums.size() - 1; i >= 0; --i) {
            System.out.print(parsedNums.get(i) + " ");
        }

        System.out.println("");
    }
}
