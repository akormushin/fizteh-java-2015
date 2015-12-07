package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import java.util.HashMap;
import java.util.Map;

class RussianWordForms {
    private static final Map<String, String[]> WORD_FORMS = new HashMap<>();

    static {
        WORD_FORMS.put("ретвит", new String[]{"ретвитов", "ретвит", "ретвита"});
        WORD_FORMS.put("час", new String[]{"часов", "час", "часа"});
        WORD_FORMS.put("минута", new String[]{"минут", "минуту", "минуты"});
        WORD_FORMS.put("день", new String[]{"дней", "день", "дня"});
    }

    private static final int FIVE = 5;
    private static final int TEN = 10;

    public static String getWordForm(String word, long count) {
        long remainder = count % TEN;
        long secondRemainder = count % (TEN * TEN);
        if ((FIVE <= secondRemainder && secondRemainder <= 2 * TEN) || remainder == 0
                || (FIVE <= remainder && remainder < TEN)) {
            return WORD_FORMS.get(word)[0];
        }
        if (remainder == 1) {
            return WORD_FORMS.get(word)[1];
        }
        return WORD_FORMS.get(word)[2];
    }
}
