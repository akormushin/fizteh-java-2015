package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import twitter4j.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cache-nez on 9/23/15.
 */




public class TweetsRetriever {
    private static final int DEFAULT_LIMIT = 10;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    static String getRelativeTime(Date date) {
        /*TODO: set relative time*/
        return date.toString();
    }

    static String getTextToPrint(Status status) {
        /*TODO: make it clear*/
        String text = "[" + status.getCreatedAt().toString() + "] @" + ANSI_BLUE + status.getUser().getScreenName()
                + ANSI_RESET + ": " + status.getText();
        if (status.getRetweetCount() > 0) {
            text = text + " (" + status.getRetweetCount() + " ретвитов)";
        }
        if (status.isRetweet()) {
            String[] splitText = status.getText().split(":", 2);
            String tweetText = splitText[1];
            text = "[" + status.getCreatedAt().toString() + "] @" + ANSI_BLUE +  status.getUser().getScreenName()
                    + ANSI_RESET + " ретвитнул @" + ANSI_BLUE +  status.getRetweetedStatus().getUser().getScreenName()
                    + ANSI_RESET + ": " + tweetText;
        }
        return  text;
    }

    public static void getTweets(String searchFor, int limit) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query;
        if (searchFor.equals("")) {
            query = new Query();
        } else {
            query = new Query(searchFor);
        }
        //query.setLang("ru");
        query.setCount(limit);
        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            //e.printStackTrace();
            throw e;
        }
        if (result != null) {
            for (Status status : result.getTweets()) {
                System.out.println(getTextToPrint(status));
            }
        } else {
            System.out.println("No tweets found");
        }

    }

    public static void main(String[] args) {


    }
}

