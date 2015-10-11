package ru.fizteh.fivt.students.sergmiller.twitterStream;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by sergmiller on 06.10.15.
 */
public class TimeResolver {
    public static final int TWO = 2;

    /**
     * Getting time info.
     *
     * @param createdTime is time of create status
     * @return new string with special format
     */
    public static String getTime(LocalDateTime createdTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        long minutes;
        long hours;
        long days;
        if (ChronoUnit.MINUTES.between(createdTime, currentTime) < TWO) {
            return "Только что";
        }

        if (ChronoUnit.HOURS.between(createdTime, currentTime) < 1) {
            minutes = ChronoUnit.MINUTES.between(createdTime, currentTime);
            return minutes + " " + DeclensionResolver.getDeclensionForm(
                    DeclensionResolver.Word.MINUTE, minutes) + " назад";
        }

        if (ChronoUnit.DAYS.between(createdTime, currentTime) < 1) {
            hours = ChronoUnit.HOURS.between(createdTime, currentTime);
            return hours + " " + DeclensionResolver.getDeclensionForm(
                    DeclensionResolver.Word.HOUR, hours) + " назад";
        }

        if (ChronoUnit.DAYS.between(createdTime, currentTime) == 1) {
            return "Вчера";
        }

        days = ChronoUnit.DAYS.between(createdTime, currentTime);
        return days + " " + DeclensionResolver.getDeclensionForm(
                DeclensionResolver.Word.DAY, days) + " назад";
    }

}

