package ru.fizteh.fivt.students.mamaevads.twitterstream;


public class WordForms {
    public static final int ONE = 1;
    public static final int TEN = 10;
    public static final int TWO_TEN = 20;
    public static final int TEN_ONE = 11;
    public static final int TWO = 2;
    public static final int TEN_TEN = 100;
    public static final int TWO_TWO = 4;


    enum Form {
        firstform , secondform , thirdform
    }

    static Form getForm(long number) {
        if (number == ONE || (number > TWO_TEN && number % TEN == ONE && number % TEN_TEN != TEN_ONE)) {
            return Form.firstform;
        } else if ((number % TEN_TEN < TEN || number % TEN_TEN > TWO_TEN)
                && number % TEN >= TWO && number % TEN <= TWO_TWO) {
            return Form.secondform;
        } else {
            return Form.thirdform;
        }
    }

    static String hourForm(long number) {
        Form thisform;
        thisform = getForm(number);
        switch (thisform) {
            case firstform: return "час назад ";
            case secondform: return "часа назад ";
            case thirdform: return "часов назад ";
            default : return "hours ago";
        }
    }

    static String minutesForm(long number) {
        Form thisform;
        thisform = getForm(number);
        switch (thisform) {
            case firstform: return "минута назад ";
            case secondform: return "минуты назад ";
            case thirdform: return "минут назад ";
            default : return "minutes ago";
        }
    }

    static String daysForm(long number) {
        Form thisform;
        thisform = getForm(number);
        switch (thisform) {
            case firstform: return "день назад ";
            case secondform: return "дня назад ";
            case thirdform: return "дней назад ";
            default : return "days ago";
        }
    }
    static String retweetForm(long number) {
        Form thisform;
        thisform = getForm(number);
        switch (thisform) {
            case firstform: return " ретвит ";
            case secondform: return " ретвита ";
            case thirdform: return " ретвитов ";
            default : return "retweets";
        }
    }

}
