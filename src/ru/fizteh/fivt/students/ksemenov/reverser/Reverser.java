/**
 * Created by zemen on 12.09.15.
 */

package ru.fizteh.fivt.students.ksemenov.reverser;

import java.util.StringTokenizer;

public final class Reverser {
    private static StringTokenizer tokenizer;
    private static boolean firstToken = true;

    private Reverser() {
    }

    public static void main(final String[] args) {
        for (int i = args.length - 1; i >= 0; --i) {
            tokenizer = new StringTokenizer(args[i]);
            printTokens();
        }
        System.out.println();
    }

    private static void printTokens() {
        if (!tokenizer.hasMoreTokens()) {
            return;
        }
        String token = tokenizer.nextToken();
        printTokens();
        if (!firstToken) {
            System.out.print(' ');
        } else {
            firstToken = false;
        }
        System.out.print(token);
    }
}
