package ru.fizteh.fivt.students.loulett.TwitterStream;

public class RussianEnding {
    static final int TEN = 10;
    static final int FIVE = 5;
    static final int TWENTY = 20;
    static final int HUNDRED = 100;

    public static String russianEnding(int count, int flag) {
        String[][] ending = {{"ретвитов", "ретвит", "ретвита"},
                {"минут", "минуту", "минуты"},
                {"часов", "час", "часа"},
                {"дней", "день", "дня"}};

        if (count % TEN >= FIVE || (count % HUNDRED > TEN
                && count % HUNDRED <= TWENTY) || count % TEN == 0) {
            return count + " " + ending[flag][0];
        } else if (count % TEN == 1) {
            return count + " " + ending[flag][1];
        } else {
            return count + " " + ending[flag][2];
        }
    }
}
