/**
*   A solution to the /tasks/00-Reverser.md task
*/

package ru.fizteh.fivt.students.thefacetakt.reverser;

/**
*   A class with solution implementation.
*/
public final class Reverser {

    /**
    *   Declaring private constructor in order not to have
    *   default one as stylecheck requires.
    */

    private Reverser() {
    }

    /**
    *   Implementation of solution itself.
    *   @param args **command line arguments**
    */

    public static void main(final String[] args) {
        for (int i = args.length - 1; i >= 0; --i) {
            String[] current = args[i].split("\\s+");
            for (int j = current.length - 1; j >= 0; --j) {
                System.out.print(current[j]);
                if (i + j == 0) {
                    System.out.print("\n");
                } else {
                    System.out.printf(" ");
                }
            }
        }
    }
}
