package ru.fizteh.fivt.students.duha666.TwitterStream;

import com.beust.jcommander.JCommander;
import twitter4j.GeoLocation;
import twitter4j.TwitterException;

public class TwitterStream {
    public static final String TWEET_SEPARATOR =
            "\n----------------------------------------------------------------------------------------\n";
    public static void main(String[] args) throws TwitterException {
        JCommanderSettings jcs = new JCommanderSettings();
        try {
            JCommander jc = new JCommander(jcs, args);
            if (jcs.isPrintHelp()) {
                jc.usage();
                return;
            }
        } catch (Exception e) {
            JCommander jc = new JCommander(jcs, new String[]{"--query", "Hello"});
            jc.usage();
            return;
        }
        System.out.print("Твиты по запросу " + jcs.getQuery() + " для " + jcs.getPlace() + ":");
        System.out.print(TWEET_SEPARATOR);
        try {
            GeoLocation location = LocationGetter.getLocationByPlace(jcs.getPlace());
            if (jcs.isStream()) {
                TweetPrinter.streamTweets(jcs, location);
            } else {
                TweetPrinter.printTweets(jcs, location);
            }
        } catch (Exception e) {
            System.err.print("Some error occured: " + e.toString());
            e.printStackTrace();
        }
    }
}

