package ru.fizteh.fivt.students.thefacetakt.twitterstream.library.exceptions;

/**
 * Created by thefacetakt on 06.10.15.
 */
public class TwitterStreamException extends Exception {
    public TwitterStreamException(String message) {
        super(message);
    }

    public TwitterStreamException(String message, Throwable cause) {
        super(message, cause);
    }
}
