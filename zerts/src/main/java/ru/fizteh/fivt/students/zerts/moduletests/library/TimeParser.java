package ru.fizteh.fivt.students.zerts.moduletests.library;

import ru.fizteh.fivt.students.zerts.TwitterStream.Printer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeParser {
    static final int SEC_MODE = 0;
    static final int MIN_MODE = 1;
    static final int HOUR_MODE = 2;
    static final int DAY_MODE = 3;
    static final int RT_MODE = 4;

    static final int FIRST_FORM = 0;
    static final int SECOND_FORM = 1;
    static final int THIRD_FORM = 2;

    static final int TEN_MOD = 10;
    static final int FIVE = 5;
    static final int HUNDRED_MOD = 100;
    static final int TWENTY = 20;

    static final String[][] WORDS = {
            {"секунд", "секунда", "секунды"},
            {"минут", "минута", "минуты"},
            {"часов", "час", "часа"},
            {"дней", "день", "дня"},
            {"ретвитов", "ретвит", "ретвита"}
    };

    public static void rightWordPrinting(long goneTime, int mode) {
        System.out.print(goneTime + " ");
        if (goneTime % TEN_MOD >= FIVE || goneTime % TEN_MOD == 0 || (goneTime % HUNDRED_MOD > TEN_MOD
                && goneTime % HUNDRED_MOD < TWENTY)) {
            Printer.print(WORDS[mode][FIRST_FORM]);
        } else if (goneTime % TEN_MOD == 1) {
            Printer.print(WORDS[mode][SECOND_FORM]);
        } else {
            Printer.print(WORDS[mode][THIRD_FORM]);
        }
        if (mode < RT_MODE) {
            Printer.print(" назад");
        }
    }
    public static void printGoneDate(long givenTime) {
        long nowTime = System.currentTimeMillis(), goneTime = nowTime - givenTime;
        //System.out.print("Время: " + tweetTime + " " + currTime + "   ");
        Printer.print("[");
        if (goneTime <= 2 * TimeUnit.SECONDS.toMillis(1L)) {
            Printer.print("Только что");
        } else if (goneTime < TimeUnit.MINUTES.toMillis(1L)) {
            rightWordPrinting(goneTime / TimeUnit.SECONDS.toMillis(1L), SEC_MODE);
        } else if (goneTime < TimeUnit.HOURS.toMillis(1L)) {
            rightWordPrinting(goneTime / TimeUnit.MINUTES.toMillis(1L), MIN_MODE);
        } else if (new Date(givenTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().
                equals(LocalDate.now())) {
            rightWordPrinting(goneTime / TimeUnit.HOURS.toMillis(1L), HOUR_MODE);
        } else if (new Date(givenTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().
                equals(LocalDate.now().minusDays(1))) {
            Printer.print("Вчера");
        } else {
            rightWordPrinting((goneTime + TimeUnit.DAYS.toMillis(1L) - 1) / TimeUnit.DAYS.toMillis(1L), DAY_MODE);
        }
        Printer.print("] ");
    }
}
