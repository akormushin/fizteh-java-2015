package ru.fizteh.fivt.students.loulett.TwitterStream;

import java.util.HashMap;
import java.util.Map;

public class RussianEnding {
    static final int TEN = 10;
    static final int FIVE = 5;
    static final int TWENTY = 20;
    static final int HUNDRED = 100;
    private static final Map<String, String[]> ENDINGS = new HashMap<>();
    static {
        ENDINGS.put("ретвит", new String[]{"ретвитов", "ретвит", "ретвита"});
        ENDINGS.put("минута", new String[]{"минут", "минуту", "минуты"});
        ENDINGS.put("час", new String[]{"часов", "час", "часа"});
        ENDINGS.put("день", new String[]{"дней", "день", "дня"});
    }


    public static String russianEnding(int count, String word) {

        if (count % TEN >= FIVE || (count % HUNDRED > TEN
                && count % HUNDRED <= TWENTY) || count % TEN == 0) {
            return count + " " + ENDINGS.get(word)[0];
        } else if (count % TEN == 1) {
            return count + " " + ENDINGS.get(word)[1];
        } else {
            return count + " " + ENDINGS.get(word)[2];
        }
    }
}
