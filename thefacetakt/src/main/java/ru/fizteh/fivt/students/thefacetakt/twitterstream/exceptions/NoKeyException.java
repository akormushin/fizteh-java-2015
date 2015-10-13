package ru.fizteh.fivt.students.thefacetakt.twitterstream.exceptions;

/**
 * Created by thefacetakt on 23.09.15.
 */

public class NoKeyException extends Exception {
    public NoKeyException() {
        super("Something went terribly wrong: no maps "
                + "key found");
    }

    public NoKeyException(String message) {
        super(message);
    }
}
