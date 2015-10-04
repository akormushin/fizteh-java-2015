package ru.fizteh.fivt.students.riazanovskiy;

import java.util.HashMap;
import java.util.Map;

class RussianWordForms {
    private static final Map<String, String[]> WORD_FORMS = new HashMap<String, String[]>() {
        {
            put("день", new String[]{"дней", "день", "дня"});
            put("минута", new String[]{"минут", "минуту", "минуты"});
            put("час", new String[]{"часов", "час", "часа"});
            put("ретвит", new String[]{"ретвитов", "ретвит", "ретвита"});
        }
    };

    private static final int FIVE = 5;
    private static final int TEN = 10;

    public static String getWordForm(String word, long count) {
        long remainder = count % TEN;
        long secondRemainder = count % (TEN * TEN);
        if ((FIVE <= secondRemainder && secondRemainder <= 2 * TEN) || remainder == 0
                || (FIVE <= remainder && remainder < TEN)) {
            return WORD_FORMS.get(word)[0];
        } else if (remainder == 1) {
            return WORD_FORMS.get(word)[1];
        }
        return WORD_FORMS.get(word)[2];
    }
}
