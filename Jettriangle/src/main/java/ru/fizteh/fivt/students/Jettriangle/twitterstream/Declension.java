package ru.fizteh.fivt.students.Jettriangle.twitterstream;

/**
 * Created by rtriangle on 05.10.15.
 */

public class Declension {

    private static final String[] RETWEETS = {"ретвитов", "ретвит", "ретвита"};

    private static final String[] MINUTES = {"минут", "минуту", "минуты"};

    private static final String[] HOURS = {"часов", "час", "часа"};

    private static final String[] DAYS = {"дней", "день", "дня"};

    private static final int FIRST_IMPORTANT_MOD = 100;
    private static final int SECOND_IMPORTANT_MOD = 10;
    private static final int NUMBER_ELEVEN = 11;
    private static final int NUMBER_NINETEEN = 19;

    static final int NUMBER_ONE = 1;
    static final int NUMBER_TWO = 2;
    static final int NUMBER_FOUR = 4;

    static String declensionScheme(long n, String[] cases) {
        n = n % FIRST_IMPORTANT_MOD;
        if (NUMBER_ELEVEN <= n && n <= NUMBER_NINETEEN) {
            return cases[0];
        }
        n = n % SECOND_IMPORTANT_MOD;
        if (n == NUMBER_ONE) {
            return cases[1];
        }
        if (NUMBER_TWO <= n && n <= NUMBER_FOUR) {
            return cases[2];
        }
        return cases[0];
    }

    public static String tweetDeclension(long n) {

        return declensionScheme(n, RETWEETS);
    }

    public static String minutesDeclension(long n) {

        return declensionScheme(n, MINUTES);
    }

    public static String hoursDeclension(long n) {
        return declensionScheme(n, HOURS);
    }

    public static String daysDeclension(long n) {
        return declensionScheme(n, DAYS);
    }
}
