package ru.fizteh.fivt.students.fminkin.twitterstream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import ru.fizteh.fivt.students.fminkin.twitterstream.library.*;
import ru.fizteh.fivt.students.fminkin.twitterstream.library.GeoLocation;
import ru.fizteh.fivt.students.fminkin.twitterstream.library.Location;
import ru.fizteh.fivt.students.fminkin.twitterstream.library.TimeAlign;
import twitter4j.*;

import java.util.List;
import java.util.Scanner;

/**
 * Created by Fedor on 22.09.2015.
*/
public class TwitterStream {
    public static final Integer MINUSES_COUNT = 120;

    static void waitCtrlD() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            boolean mustHaveAtLeastOneStatement;
        }
        scanner.close();
        System.exit(0);
    }

    public static void main(String[] args) throws TwitterException {
        JCommanderConfig jcc = new JCommanderConfig();
        try {
            JCommander jc = new JCommander(jcc, args);
            jc.setProgramName("TwitterStream");
            if (jcc.isHelp()) {
                jc.usage();
                return;
            }
        } catch (ParameterException e) {
            JCommander jc = new JCommander(jcc, new String[] {"--query", "Hello"});
            jc.setProgramName("TwitterStream");
            jc.usage();
            return;
        }
        Location loc = new Location(0, 0, null);
        GeoLocation g = new GeoLocation();
        try {
            loc = g.getLocationGoogle(jcc.getLocation(), new JSonReader());
        } catch (Throwable e) {
            System.out.println("IOException has occured");
            e.printStackTrace();
            return;
        }
        System.out.println("Твитты по запросу " + jcc.getQueries() + " для " + jcc.getLocation());
        for (int i = 0; i < MINUSES_COUNT; ++i) {
            System.out.print("-");
        }
        System.out.println();

        SearchTweets tweetSearch = new SearchTweets();
        try {
            if (jcc.isStream()) {
                tweetSearch.handleStream(jcc, loc, new TwitterStreamFactory().getInstance(), s -> {
                    System.out.println(s);
                    for (int i = 0; i < TwitterStream.MINUSES_COUNT; ++i) {
                        System.out.print("-");
                    }
                    System.out.println();
                });

                waitCtrlD();
            } else {
                List<Status> tweets = tweetSearch.search(jcc, loc,  new TwitterFactory().getInstance());
                if (tweets.isEmpty()) {
                    System.out.println("Нет результатов");
                    for (int i = 0; i < TwitterStream.MINUSES_COUNT; ++i) {
                        System.out.print("-");
                    }
                    System.out.println();
                }
                for (Status tweet : tweets) {
                    if (!tweet.isRetweet() || !jcc.isRetweetsHidden()) {
                        System.out.print("[" + TimeAlign.formatTime(
                                System.currentTimeMillis(), tweet.getCreatedAt().getTime()) + "] ");
                        tweetSearch.printTweet(tweet);
                    }
                }
            }
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            System.out.println("tweetSearch Failed\n");
            return;
        }

    }
}
