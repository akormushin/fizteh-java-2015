package ru.fizteh.fivt.students.fminkin.twitterstream;

/**
 * Created by Федор on 28.09.2015.
 */
public class RussianDeclense {
    public static final Integer MODULE_CONSTANT = 20;
    public static final Integer MODULE_VALUE_FIRST = 1;
    public static final Integer MODULE_VALUE_SECOND = 2;
    public static final Integer MODULE_VALUE_THIRD = 3;
    public static final Integer MODULE_VALUE_FOURTH = 4;
    static final String[] HOURS = {"час", "часа", "часов"};
    static final String[] DAYS = {"день", "дня", "дней"};
    static final String[] MINUTES = {"минута", "минуты",
            "минут"};
    static final String[] RETWEETS = {"ретвит", "ретвита",
            "ретвитов"};
    private static String module(long raw, String[] d) {
        if (raw % MODULE_CONSTANT == MODULE_VALUE_FIRST) {
            return d[0];
        }
        if (raw % MODULE_CONSTANT == MODULE_VALUE_SECOND
                || raw % MODULE_CONSTANT == MODULE_VALUE_THIRD
                || raw % MODULE_CONSTANT == MODULE_VALUE_FOURTH) {
            return d[1];
        }
        return d[2];
    }
    public static String getHours(long raw) {
        return module(raw, HOURS);
    }
    public static String getDays(long raw) {
        return module(raw, DAYS);
    }
    public static String getMinutes(long raw) {
        return module(raw, MINUTES);
    }
    public static String getRetweet(long raw) {
        return module(raw, RETWEETS);
    }
}
