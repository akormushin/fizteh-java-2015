package ru.fizteh.fivt.students.thefacetakt.TwitterStream;

/**
 * Created by thefacetakt on 23.09.15.
 */
class QueryLimitException extends Exception {
    QueryLimitException() {
    }

    QueryLimitException(String message) {
        super(message);
    }
}
