package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import twitter4j.*;

import java.util.Date;

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

    static String getRetweetText (Status status) {
        String[] splitText = status.getText().split(":", 2);
        String tweetText = splitText[1];
        return new StringBuilder().append("[").append(status.getCreatedAt().toString()).append("] @").append(ANSI_BLUE).append(status.getUser().getScreenName()).append(ANSI_RESET).append(" ретвитнул @").append(ANSI_BLUE).append(status.getRetweetedStatus().getUser().getScreenName()).append(ANSI_RESET).append(": ").append(tweetText).toString();
    }

    static String getTweetText (Status status) {
        String text = "[" + status.getCreatedAt().toString() + "] @" + ANSI_BLUE + status.getUser().getScreenName()
                + ANSI_RESET + ": " + status.getText();
        if (status.getRetweetCount() > 0) {
            text = text + " (" + status.getRetweetCount() + " ретвитов)";
        }
        return  text;
    }

    static String getTextToPrint(Status status) {
        if (status.isRetweet()) {
            return getRetweetText(status)
        }
        else {
            return  getTweetText(status);
        }
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

