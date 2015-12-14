package ru.fizteh.fivt.students.w4r10ck1337.moduletests;

import ru.fizteh.fivt.students.w4r10ck1337.moduletests.library.ArgsParser;
import ru.fizteh.fivt.students.w4r10ck1337.moduletests.library.NiceTime;
import ru.fizteh.fivt.students.w4r10ck1337.moduletests.library.TweetBuilder;
import ru.fizteh.fivt.students.w4r10ck1337.moduletests.library.TwitterServices;
import twitter4j.*;

import ru.fizteh.fivt.students.w4r10ck1337.moduletests.library.TwitterServices.*;


public class TwitterStream {
    private static final String SEPARATOR = "--------------------------------------------"
                                          + "--------------------------------------------";
    private static Twitter twitter = null;

    private static ArgsParser.Parameters parameters;

    public static String getSeparator() {
        return SEPARATOR;
    }

    private static void printTweet(Status status) {
        if (!parameters.isStream()) {
            System.out.print("[" + NiceTime.diff(status.getCreatedAt(), System.currentTimeMillis()) + "] ");
        }
        System.out.println(TweetBuilder.formatTweet(status));
        System.out.println(SEPARATOR);
    }

    public static void main(String[] args) {
        parameters = ArgsParser.parseArgs(args);
        if (parameters.isHelp()) {
            return;
        }
        if (parameters.getQuery().length() > 0) {
            System.out.println("Твиты по запросу " + parameters.getQuery() + " для " + parameters.getPlace() + ":");
        } else {
            System.out.println("Твиты для " + parameters.getPlace() + ":");
        }
        System.out.println(SEPARATOR);
        twitter = TwitterFactory.getSingleton();
        if (parameters.isHideRetweets()) {
            parameters.setQuery(parameters.getQuery() + " +exclude:retweets");
        }
        if (parameters.isStream()) {
            TwitterServices.streamTweets(parameters.getQuery(), parameters.getPlace(), twitter,
                                         parameters.getLimit(), TwitterStream::printTweet);
        } else {
            TwitterServices.printTweets(parameters.getQuery(), parameters.getPlace(), twitter,
                                        parameters.getLimit(), TwitterStream::printTweet);
        }
    }
}
