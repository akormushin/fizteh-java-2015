package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import java.io.PrintStream;
import java.util.List;

/**
 * Created by thefacetakt on 11.10.15.
 */
public class Printer {
    private PrintStream out;

    public Printer(PrintStream outStream) {
        this.out = outStream;
    }

    static final int MINUSES_COUNT = 140;
    void printSeparator() {
        for (int i = 0; i < MINUSES_COUNT; ++i) {
            out.print("-");
        }
        out.println();
    }

    public void print(String tweet) {
        out.println(tweet);
        printSeparator();
    }

    public void printTweets(List<String> tweets) {
        if (tweets.isEmpty()) {
            out.println("Не найдено ни одного твита");
        } else {
            tweets.stream().forEach(this::print);
        }
    }
}
