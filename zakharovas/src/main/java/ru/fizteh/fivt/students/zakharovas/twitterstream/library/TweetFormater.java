package ru.fizteh.fivt.students.zakharovas.twitterstream.library;

import twitter4j.Status;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TweetFormater {
    private static final String COLOR_BLUE = "\u001B[34m";
    private static final String COLOR_RESET = "\u001B[0m";
    private static final String[] ENDING_RETWEETS = {"ретвитов", "ретвит", "ретвита"};
    private static final String[] ENDING_DAYS = {"дней", "день", "дня"};
    private static final String[] ENDING_MUNUTES = {"минут", "минуту", "минуты"};
    private static final String[] ENDING_HOURS = {"часов", "час", "часа"};

    private Status tweet;
    private Clock clock;

    TweetFormater(Status tweet) {
        this.tweet = tweet;
        clock = Clock.systemDefaultZone();
    }

    TweetFormater(Status tweet, Clock clock) {
        this.tweet = tweet;
        this.clock = clock;
    }

    public String tweetForOutput() {
        return dateFormater() + " " + tweetForOutputWithoutDate();

    }

    public String tweetForOutputWithoutDate() {
        StringBuilder formatedTweet = new StringBuilder();
        if (!tweet.isRetweet()) {
            formatedTweet.append(COLOR_BLUE)
                    .append(" @")
                    .append(tweet.getUser().getName())
                    .append(COLOR_RESET).append(": ")
                    .append(tweet.getText());
            if (tweet.getRetweetCount() > 0) {
                formatedTweet.append("(")
                        .append(tweet.getRetweetCount())
                        .append(" ")
                        .append(RussianDeclension.declensionWithNumber(tweet.getRetweetCount(), ENDING_RETWEETS))
                        .append(")");
            }
        } else {
            formatedTweet.append(COLOR_BLUE)
                    .append(" @")
                    .append(tweet.getUser().getName())
                    .append(COLOR_RESET)
                    .append(": ")
                    .append("ретвитнул ")
                    .append(COLOR_BLUE).append("@")
                    .append(tweet.getRetweetedStatus().getUser().getName())
                    .append(COLOR_RESET).append(": ")
                    .append(tweet.getRetweetedStatus().getText());
        }
        return formatedTweet.toString();
    }

    //Just for overriding
    protected String dateFormater() {
        LocalDateTime tweetTime = tweet.getCreatedAt().toInstant().atZone(clock.getZone()).toLocalDateTime();
        LocalDateTime currentTime = LocalDateTime.now(clock);
        long minuteDifference = ChronoUnit.MINUTES.between(tweetTime, currentTime);
        long hourDifference = ChronoUnit.HOURS.between(tweetTime, currentTime);
        long daysDifference = tweetTime.toLocalDate().until(currentTime.toLocalDate(), ChronoUnit.DAYS);
        if (minuteDifference < Numbers.NOW_CONSTANT) {
            return "только что";
        }
        if (daysDifference == 0) {
            //today
            if (hourDifference == 0) {
                return minuteDifference + " "
                        + RussianDeclension.declensionWithNumber((int) minuteDifference, ENDING_MUNUTES) + " назад";
            }
            return hourDifference + " "
                    + RussianDeclension.declensionWithNumber((int) hourDifference, ENDING_HOURS) + " назад";
        } else if (daysDifference == 1) {
            return "вчера";
        }
        return daysDifference + " "
                + RussianDeclension.declensionWithNumber((int) daysDifference, ENDING_DAYS) + " назад";
    }


}
