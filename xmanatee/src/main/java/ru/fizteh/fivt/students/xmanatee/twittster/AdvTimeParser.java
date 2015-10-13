package ru.fizteh.fivt.students.xmanatee.twittster;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

class AdvTimeParser {
    private String timeToReturn;
    AdvTimeParser(java.util.Date date) {

        Word4declension minuteWord = new Word4declension("минуту", "минуты", "минут");
        Word4declension hourWord = new Word4declension("час", "часа", "часов");
        Word4declension dayWord = new Word4declension("день", "дня", "дней");

        String formattedTime;
        LocalDateTime dateToFormat = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime curDate = LocalDateTime.now();
        long dMinutes = ChronoUnit.MINUTES.between(dateToFormat, curDate);
        long dHours = ChronoUnit.HOURS.between(dateToFormat, curDate);
        long days = curDate.toLocalDate().toEpochDay() - dateToFormat.toLocalDate().toEpochDay();

        if (dMinutes < 2) {
            formattedTime = "только что";
        } else if (dHours == 0) {
            formattedTime = dMinutes + " " + minuteWord.declension4Number(dMinutes) + " назад";
        } else if (days == 0) {
            formattedTime = dHours + " " + hourWord.declension4Number(dHours) + " назад";
        } else if (days == 1) {
            formattedTime = "вчера";
        } else {
            formattedTime = days + " " + dayWord.declension4Number(days) + " назад";
        }

        timeToReturn = formattedTime;
    }
    public String get() {
        return timeToReturn;
    }
}
