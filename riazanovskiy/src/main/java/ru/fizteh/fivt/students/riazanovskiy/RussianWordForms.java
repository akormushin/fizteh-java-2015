package ru.fizteh.fivt.students.riazanovskiy;

import java.util.HashMap;
import java.util.Map;

public class RussianWordForms {
    private static final Map<String, String[]> WORD_FORMS = new HashMap<String, String[]>() { {
                put("день", new String[]{"дней", "день", "дня"});
                put("минута", new String[]{"минут", "минуту", "минуты"});
                put("час", new String[]{"часов", "час", "часа"});
            } };

    public static String getWordForm(String word, long count) {
        long remainder = count % 10;
        if ((5 <= count && count <= 20) || (remainder == 0) || (5 <= remainder && remainder <= 9))  {
            return WORD_FORMS.get(word)[0];
        } else if (remainder == 1) {
            return WORD_FORMS.get(word)[1];
        }
        return WORD_FORMS.get(word)[2];
    }
}
