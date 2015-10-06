package ru.fizteh.fivt.students.Jettriangle.twitterstream;

/**
 * Created by rtriangle on 01.10.15.
 */

import com.beust.jcommander.JCommander;
import twitter4j.*;

public class TestMain {

    private static final long MILLISEC_IN_SEC = 1000;

    public static void main(String[] args) throws TwitterException {
        JCommanderTwitter jct = new JCommanderTwitter();
        String[] argv = {"-q", "физтех", "--place", "Berlin", "--hideRetweets"};

        try {
            JCommander jcparser = new JCommander(jct, argv);
            if (jct.isHelp()) {
                jcparser.usage();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new JCommander(jct).usage();
        }

        if(jct.isStream()) {
            printStream(jct);
        } else {
            printTweets(jct);
        }

    }

    public static void printTweets(JCommanderTwitter jct) {
        Query query = new Query();
        query.count(jct.getTweetsLimit());
        query.setQuery(jct.getQuery());

        Twitter twitter = new TwitterFactory().getInstance();
        try {
            twitter.search(query).getTweets().stream()
                    .map(s -> FormatFactory.getTweetFormat(s, jct)).forEach(System.out::print);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public static void printStream(JCommanderTwitter jct) {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                try {
                    Thread.sleep(MILLISEC_IN_SEC);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(FormatFactory.getTweetFormat(status, jct));
            }

            @Override
            public void onException(Exception ex) {
                TwitterException twitterex = (TwitterException) ex;
                twitterex.printStackTrace();
            }
        });
    }
}
