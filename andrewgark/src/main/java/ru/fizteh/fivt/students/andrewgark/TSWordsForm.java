package ru.fizteh.fivt.students.andrewgark;

import twitter4j.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class TSWordsForm {
    private static final Integer ONE = 1;
    private static final Integer TWO = 2;
    private static final Integer FOUR = 4;
    private static final Integer TEN = 10;
    private static final Integer ELEVEN = 11;
    private static final Integer TWENTY = 20;
    private static final Integer HUNDRED = 100;
    private static final String[] RETWEET_FORMS = {"ретвит", "ретвита", "ретвитов"};
    private static final String[] MINUTE_FORMS = {"минута", "минуты", "минут"};
    private static final String[] HOUR_FORMS = {"час", "часа", "часов"};
    private static final String[] DAY_FORMS = {"день", "дня", "дней"};

    public static String getTimeForm(Status tweet) {
        ZonedDateTime dateTime = tweet.getCreatedAt().toInstant().atZone(ZoneId.systemDefault());
        LocalDateTime tweetLocalDateTime = dateTime.toLocalDateTime();
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        return "[" + getTimeBetweenForm(tweetLocalDateTime, nowLocalDateTime) + "]";
    }

    public static String getTimeBetweenForm(LocalDateTime tweetLDT, LocalDateTime nowLDT) {
        LocalDate tweetLD = tweetLDT.toLocalDate();
        LocalDate nowLD = nowLDT.toLocalDate();

        if (tweetLD.equals(nowLD)) {
            if (nowLDT.minusMinutes(2).isBefore(tweetLDT)) {
                return "Только что";
            }
            if (nowLDT.minusHours(1).isBefore(tweetLDT)) {
                Integer numberMinutes = Integer.valueOf(Long.toString(tweetLDT.until(nowLDT, ChronoUnit.MINUTES)));
                return getForm(numberMinutes, MINUTE_FORMS) + " назад";
            }
            Integer numberHours = Integer.valueOf(Long.toString(tweetLDT.until(nowLDT, ChronoUnit.HOURS)));
            return getForm(numberHours, HOUR_FORMS) + " назад";
        } else {
            if (nowLD.minusDays(1).equals(tweetLD)) {
                return "Вчера";
            }
            Integer numberDays = Integer.valueOf(Long.toString(tweetLD.until(nowLD, ChronoUnit.DAYS)));
            return getForm(numberDays, DAY_FORMS) + " назад";
        }
    }

    public static String retweetsForm(Integer retweets) {
        if (retweets == 0) {
            return "";
        }
        return " (" + getForm(retweets, RETWEET_FORMS) + ")";
    }

    public static String getForm(Integer n, String[] forms) {
        if ((n % TEN == ONE) && (n % HUNDRED != ELEVEN)) {
            return Integer.toString(n) + " "  + forms[0];
        } else if ((n % TEN >= TWO) && (n % TEN <= FOUR)  && (n % HUNDRED < TEN || n % HUNDRED >= TWENTY)) {
            return Integer.toString(n) + " "  + forms[1];
        } else {
            return Integer.toString(n) + " "  + forms[2];
        }
    }
}
