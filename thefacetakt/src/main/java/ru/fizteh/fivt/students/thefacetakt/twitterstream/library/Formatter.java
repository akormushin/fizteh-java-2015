package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import twitter4j.Status;

/**
 * Created by thefacetakt on 11.10.15.
 */
class Formatter {
    static String formatNick(String nick) {
        return "@" + nick + ": ";
    }

    static String formatTweet(Status tweet) {
        String nickPart = formatNick(tweet.getUser().getScreenName());

        if (!tweet.isRetweet()) {
            String retweetCountPart = "";
            if (tweet.getRetweetCount() != 0) {
                retweetCountPart = "(" + tweet.getRetweetCount() + " "
                        + Declenser.retweetDeclension(tweet.getRetweetCount())
                        + ")";
            }

            return nickPart + tweet.getText() + retweetCountPart;
        }
        String retweetPart = "ретвитнул "
                + formatNick(tweet.getRetweetedStatus().getUser()
                .getScreenName());

        return nickPart + retweetPart + tweet.getRetweetedStatus().getText();
    }
}
