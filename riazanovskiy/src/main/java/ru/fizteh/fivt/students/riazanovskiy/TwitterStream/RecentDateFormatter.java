package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

class RecentDateFormatter {

    private static final Duration JUST_NOW_THRESHOLD = Duration.ofMinutes(2);

    public static String format(Date date) {
        return format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()), LocalDateTime.now());
    }

    public static String format(LocalDateTime then, LocalDateTime now) {
        Duration timePassed = Duration.between(then, now);
        if (timePassed.compareTo(JUST_NOW_THRESHOLD) < 0) {
            return "только что";
        } else if (timePassed.compareTo(Duration.ofHours(1)) < 0) {
            long minutesPassed = timePassed.toMinutes();
            return String.format("%d %s назад", minutesPassed, RussianWordForms.getWordForm("минута", minutesPassed));
        } else if (then.toLocalDate().equals(now.toLocalDate())) {
            long hoursPassed = timePassed.toHours();
            return String.format("%d %s назад", hoursPassed, RussianWordForms.getWordForm("час", hoursPassed));
        } else {
            long daysPassed = ChronoUnit.DAYS.between(then.toLocalDate(), now.toLocalDate());
            if (daysPassed == 1) {
                return "вчера";
            } else {
                return String.format("%d %s назад", daysPassed, RussianWordForms.getWordForm("день", daysPassed));
            }
        }
    }
}
