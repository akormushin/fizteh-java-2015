package ru.fizteh.fivt.students.Jettriangle.twitterstream;

import twitter4j.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by rtriangle on 05.10.15.
 */

public class FormatFactory {

    private static final String SEPARATETWEETS =
            "\n----------------------------------------------------------------------------------------\n";

    public static String getTweetFormat(Status st, JCommanderTwitter jct) {
        String correctTweetFormat = new String();
        LocalDateTime current = LocalDateTime.now();

        if (!jct.isStream()) {
            correctTweetFormat = "[" + timeFormat(current, st.getCreatedAt()) + "]";
        }

        if (!jct.isHideRetweets()) {
            if (st.isRetweet()) {
                correctTweetFormat = correctTweetFormat.concat("@" + st.getUser().getName()
                        + " ретвитнул @" + st.getRetweetedStatus().getUser().getName() + ": "
                        + st.getRetweetedStatus().getText());
            } else {
                correctTweetFormat = correctTweetFormat.concat("@" + st.getUser().getName() + ": " + st.getText());
                if (st.isRetweeted()) {
                    correctTweetFormat = correctTweetFormat.concat(" (" + Long.toString(st.getRetweetCount())
                            + " " + Declension.tweetDeclension(st.getRetweetCount()) + ")");

                } else {
                    correctTweetFormat = correctTweetFormat.concat("@" + st.getUser().getName() + ": " + st.getText());
                }
            }
            correctTweetFormat = correctTweetFormat.concat(SEPARATETWEETS);
            return correctTweetFormat;
        } else {
            if (!st.isRetweet()) {
                correctTweetFormat = correctTweetFormat.concat("@" + st.getUser().getName() + ": "
                        + st.getText() + SEPARATETWEETS);
                return correctTweetFormat;
            }
            return "";
        }
    }

    public static String timeFormat(LocalDateTime current, Date tweetAddTime) {

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime tweetTime = tweetAddTime.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (ChronoUnit.MINUTES.between(tweetTime, currentTime) < 2) {
            return "только что";
        }

        if (ChronoUnit.HOURS.between(tweetTime, currentTime) <= 0) {
            long n = ChronoUnit.MINUTES.between(tweetTime, currentTime);
            return String.valueOf(n)
                    + " " + Declension.minutesDeclension(n) + " назад";
        }

        if (ChronoUnit.DAYS.between(tweetTime, currentTime) <= 0) {
            long n = ChronoUnit.HOURS.between(tweetTime, currentTime);
            return String.valueOf(n) + " " + Declension.hoursDeclension(n)
                    + " назад";
        }

        if (ChronoUnit.DAYS.between(tweetTime, currentTime) == 1) {
            return "вчера";
        }

        long n = ChronoUnit.DAYS.between(tweetTime, currentTime);
        return String.valueOf(n) + " " + Declension.daysDeclension(n)
                + " назад";
    }
}
