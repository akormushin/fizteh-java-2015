package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

/**
 * Created by thefacetakt on 23.09.15.
 */
class Declenser {
    static final int FIRST_IMPORTANT_MOD = 100;
    static final int SECOND_IMPORTANT_MOD = 10;

    static final int FIRST_CASE_LEFT_BORDER = 11;
    static final int FIRST_CASE_RIGHT_BORDER = 19;

    static final int SECOND_CASE = 1;

    static final int THIRD_CASE_LEFT_BORDER = 2;
    static final int THIRD_CASE_RIGHT_BORDER = 4;

    private static String declensionScheme(long n, String[] cases) {
        n = n % FIRST_IMPORTANT_MOD;
        if (FIRST_CASE_LEFT_BORDER <= n
                && n <= FIRST_CASE_RIGHT_BORDER) {
            return cases[0];
        }

        n = n % SECOND_IMPORTANT_MOD;
        if (n == SECOND_CASE) {
            return cases[1];
        }
        if (THIRD_CASE_LEFT_BORDER <= n
                && n <= THIRD_CASE_RIGHT_BORDER) {
            return cases[2];
        }
        return cases[0];
    }

    static final String[] RETWEETS_CASES = {"ретвитов", "ретвит", "ретвита"};
    static String retweetDeclension(long n) {
        return declensionScheme(n, RETWEETS_CASES);
    }

    static final String[] MINUTES_CASES = {"минут", "минуту", "минуты"};
    static String minutesDeclension(long n) {
        return declensionScheme(n, MINUTES_CASES);
    }

    static final String[] HOURS_CASES = {"часов", "час", "часа"};
    static String hoursDeclension(long n) {
        return declensionScheme(n, HOURS_CASES);
    }

    static final String[] DAYS_CASES = {"дней", "день", "дня"};
    static String daysDeclension(long n) {
        return declensionScheme(n, DAYS_CASES);
    }
}
