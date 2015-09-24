package ru.fizteh.fivt.students.zakharovas.TwitterStream;

import twitter4j.Status;

import java.util.Calendar;
import java.util.Date;

public class StringFormater {
    private static final String COLOR_BLUE = "\u001B[34m";
    private static final String COLOR_RESET = "\u001B[0m";
    private static final int MINUTE = 60 * 1000;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    private static final String[] ENDING_RETWEETS = {"ретвитов", "ретвит", "ретвита"};
    private static final String[] ENDING_DAYS = {"дней", "день", "дня"};
    private static final String[] MUNUTES = {"минут", "минуту", "минуты"};
    private static final String[] HOURS = {"часов", "час", "часа"};


    public static String fineWords(int number, String[] endings) {
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

    public static String dateFormater(Date date) {
        Calendar currentDate = Calendar.getInstance();
        Calendar tweetDate = Calendar.getInstance();
        currentDate.add(Calendar.MINUTE, Numbers.NOW_CONSTANT);
        tweetDate.setTime(date);
        if (tweetDate.after(currentDate)) {
            return "только что";
        }
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.add(Calendar.DAY_OF_MONTH, -1);
        if (tweetDate.after(currentDate)) {
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
            if (tweetDate.after(currentDate)) {
                //today
                currentDate = Calendar.getInstance();
                currentDate.set(Calendar.MINUTE, 0);
                currentDate.set(Calendar.SECOND, 0);
                currentDate.set(Calendar.MILLISECOND, 0);
                if (tweetDate.after(currentDate)) {
                    //in this hour
                    currentDate = Calendar.getInstance();
                    int minuteDifference = currentDate.get(Calendar.MINUTE) - tweetDate.get(Calendar.MINUTE);
                    return minuteDifference + " " + fineWords(minuteDifference, MUNUTES) + " назад";
                } else {
                    currentDate = Calendar.getInstance();
                    int hourDifference = currentDate.get(Calendar.HOUR_OF_DAY) - tweetDate.get(Calendar.HOUR_OF_DAY);
                    return hourDifference + " " + fineWords(hourDifference, HOURS) + " назад";
                }
            } else {
                return "вчера";
            }

        } else {
            currentDate = Calendar.getInstance();
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MILLISECOND, 0);
            tweetDate.set(Calendar.MINUTE, 0);
            tweetDate.set(Calendar.SECOND, 0);
            tweetDate.set(Calendar.HOUR_OF_DAY, 0);
            tweetDate.set(Calendar.MILLISECOND, 0);
            Long dayDifference = (currentDate.getTimeInMillis() - tweetDate.getTimeInMillis()) / DAY;
            return dayDifference.toString() + " " + fineWords(dayDifference.intValue(), ENDING_DAYS) + " назад";
        }


    }

    public static String tweetForOutput(Status tweet) {
        return dateFormater(tweet.getCreatedAt()) + tweetForOutputWithoutDate(tweet);

    }

    public static String tweetForOutputWithoutDate(Status tweet) {
        String formatedTweet = "";
        if (!tweet.isRetweet()) {
            formatedTweet += COLOR_BLUE
                    + " @" + tweet.getUser().getName() + COLOR_RESET + ": "
                    + tweet.getText();
            if (tweet.getRetweetCount() > 0) {
                formatedTweet += "(" + tweet.getRetweetCount() + " "
                        + fineWords(tweet.getRetweetCount(), ENDING_RETWEETS) + ")";
            }
        } else {
            formatedTweet += COLOR_BLUE
                    + " @" + tweet.getUser().getName() + COLOR_RESET + ": "
                    + "ретвитнул " + COLOR_BLUE + "@"
                    + tweet.getRetweetedStatus().getUser().getName() + COLOR_RESET
                    + ": " + tweet.getRetweetedStatus().getText();
        }
        return formatedTweet;
    }
}
