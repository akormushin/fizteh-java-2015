package ru.fizteh.fivt.students.zakharovas.twitterstream;

import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class StringFormater {
    private static final String COLOR_BLUE = "\u001B[34m";
    private static final String COLOR_RESET = "\u001B[0m";
    private static final String[] ENDING_RETWEETS = {"ретвитов", "ретвит", "ретвита"};
    private static final String[] ENDING_DAYS = {"дней", "день", "дня"};
    private static final String[] ENDING_MUNUTES = {"минут", "минуту", "минуты"};
    private static final String[] ENDING_HOURS = {"часов", "час", "часа"};

    public static String[] separateArguments(String[] unseparatedArguments) {
        String allArgumentsInOneString = String.join(" ", unseparatedArguments);
        return allArgumentsInOneString.split("\\s+");
    }

    public static String tweetForOutput(Status tweet) {
        return dateFormater(tweet.getCreatedAt()) + tweetForOutputWithoutDate(tweet);

    }

    public static String tweetForOutputWithoutDate(Status tweet) {
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
                        .append(fineWords(tweet.getRetweetCount(), ENDING_RETWEETS))
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


    private static String fineWords(int number, String[] endings) {
        number %= Numbers.HUNDRED;
        if (number >= Numbers.ELEVEN
                && number <= Numbers.NINETEEN) {
            return endings[0];
        }
        if (number % Numbers.TEN == Numbers.ZERO) {
            return endings[0];
        } else if (number % Numbers.TEN == Numbers.ONE) {
            return endings[1];
        } else if (number % Numbers.TEN >= Numbers.TWO
                && number % Numbers.TEN <= Numbers.FOUR) {
            return endings[2];
        } else if (number % Numbers.TEN >= Numbers.FIVE) {
            return endings[0];
        }
        return null;
    }

    private static String dateFormater(Date date) {
        LocalDateTime tweetTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime currentTime = LocalDateTime.now();
        long minuteDifference = ChronoUnit.MINUTES.between(tweetTime, currentTime);
        long hourDifference = ChronoUnit.HOURS.between(tweetTime, currentTime);
        long daysDifference = tweetTime.toLocalDate().until(currentTime.toLocalDate(), ChronoUnit.DAYS);
        if (minuteDifference < Numbers.NOW_CONSTANT) {
            return "только что";
        }
        if (daysDifference == 0) {
            //today
            if (hourDifference == 0) {
                return minuteDifference + " " + fineWords((int) minuteDifference, ENDING_MUNUTES) + " назад";
            }
            return hourDifference + " " + fineWords((int) hourDifference, ENDING_HOURS) + " назад";
        } else if (daysDifference == 1) {
            return "вчера";
        }
        return daysDifference + " " + fineWords((int) daysDifference, ENDING_DAYS) + " назад";
    }


}
