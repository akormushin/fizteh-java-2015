package ru.fizteh.fivt.students.thefacetakt.twitterstream.exceptions;

/**
 * Created by thefacetakt on 06.10.15.
 */
public class TwitterException extends Exception {
    public TwitterException() {
    }

    public TwitterException(String message) {
        super(message);
    }

    public TwitterException(String message, Throwable cause) {
        super(message, cause);
    }
}
