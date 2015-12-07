package ru.fizteh.fivt.students.mamaevads.twitterstream;
import twitter4j.*;

public class TwitterStreamer {
    public static void main(String[] args) throws TwitterException {
        Arguments arguments;
        try {
            arguments = ArgumentsInput.inputArguments(args);
        } catch (InvalidArgumentsException e) {
            arguments = new Arguments();
            System.exit(1);
        }

        if (!arguments.isStream()) {
            try {
                TweetSearch.noStreamSearch(arguments);
            } catch (TweetSearchException ex) {
                System.err.print(ex.getMessage());
                System.exit(1);
            }
        } else {
            try {
                TweetSearch.twitterStream(arguments);
            } catch (TweetSearchException ex) {
                System.err.print(ex.getMessage());
                System.exit(1);
            }
        }
    }
}


