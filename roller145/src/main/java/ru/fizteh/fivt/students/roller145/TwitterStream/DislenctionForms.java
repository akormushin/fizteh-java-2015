package ru.fizteh.fivt.students.roller145.TwitterStream;

/**
 * Created by riv on 07.10.15.
 */
public class DislenctionForms {
    private static final String[] MINUTES_FORMS = {"минуту", "минуты", "минут"};
    private static final String[] HOURS_FORMS = {"час", "часа", "часов"};
    private static final String[] DAYS_FORMS = {"день", "дня", "дней"};

    private static final int HUNDRED = 100;
    private static final int ELEVEN = 11;
    private static final int TEN = 10;
    private static final int FIVE = 5;
    private static final int ONE = 1;
    private static final int  NINETEEN= 19;

    private static final String[][] TIME_FORMS = {MINUTES_FORMS, HOURS_FORMS, DAYS_FORMS};

    public static final String[] RETWEET_FORMS = {"ретвит", "ретвита", "ретвитов"};

    public enum ETime {
        MINUTE (0),
        HOUR (1),
        DAY (2);

        private int type;

        public int getType() {
            return type;
        }

        ETime(int type) {
            this.type = type;
        }
    }

    public static ETime getCorrectForm(long number) {
        if (number % TEN == ONE && number % HUNDRED != ELEVEN) {
            return ETime.MINUTE;
        }
        if (number % TEN > ONE && number % TEN < FIVE
                && !(number % HUNDRED >= ELEVEN && number % HUNDRED <= NINETEEN)) {
            return ETime.HOUR;
        }
        return ETime.DAY;
    }


    public static String DislForm(long hours, ETime type){
        ETime correctForm = getCorrectForm(hours);
        return " " + TIME_FORMS[type.getType()][correctForm.getType()];
    }

}
