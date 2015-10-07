package ru.fizteh.fivt.students.sergmiller.twitterStream;

/**
 * Created by sergmiller on 06.10.15.
 */
public class DeclensionResolver {
    public static final int MOD100 = 100;
    public static final int MOD10 = 10;
    public static final int LEFT_BOUND_MOD_100 = 10;
    public static final int RIGHT_BOUND_MOD_100 = 20;

    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int FIVE = 5;
    private static String[] retweetForms = {"ретвит", "ретвитов", "ретвита"};
    private static String[] minuteForms = {"минуту", "минут", "минуты"};
    private static String[] hourForms = {"час", "часов", "часа"};
    private static String[] dayForms = {"день", "дней", "дня"};

    public enum Word { RETWEET, MINUTE, HOUR, DAY }

    /**
     * Getting correct form for current word.
     *
     * @param word         is input word
     * @param numberOfForm is number of correct form for @word in array of forms
     * @return correct form of @word
     */
    public static String getForm(Word word, final int numberOfForm) {
        String form = new String();
        switch (word) {
            case RETWEET:
                form = retweetForms[numberOfForm];
                break;
            case MINUTE:
                form = minuteForms[numberOfForm];
                break;
            case HOUR:
                form = hourForms[numberOfForm];
                break;
            case DAY:
                form = dayForms[numberOfForm];
                break;
            default:
                break;
        }
        return form;
    }

    /**
     * General function for getting correct declension form for current word.
     *
     * @param word  is input word
     * @param count is quantity of object with name @word
     * @return string with correct form of @word
     */
    public static String getDeclensionForm(Word word, final long count) {
        if (count % MOD100 >= LEFT_BOUND_MOD_100
                && count % MOD100 <= RIGHT_BOUND_MOD_100) {
            return getForm(word, 1);
        }

        long countMod10 = count % MOD10;
        if (countMod10 == ONE) {
            return getForm(word, 0);
        }
        if (countMod10 > ONE && countMod10 < FIVE) {
            return getForm(word, 2);
        }
        return getForm(word, 1);
    }
}
