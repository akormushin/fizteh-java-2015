package ru.fizteh.fivt.students.thefacetakt.twitterstream.exceptions;

/**
 * Created by thefacetakt on 23.09.15.
 */
public class QueryLimitException extends Exception {
    public QueryLimitException() {
    }

    public QueryLimitException(String message) {
        super(message);
    }
}
