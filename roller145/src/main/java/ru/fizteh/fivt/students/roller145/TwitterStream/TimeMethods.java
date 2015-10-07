package ru.fizteh.fivt.students.roller145.TwitterStream;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.fizteh.fivt.students.roller145.TwitterStream.DislenctionForms.DislForm;

/**
 * Created by riv on 27.09.15.
 */
public class TimeMethods {

    public static final int MILISEC_IN_SEC = 1000;

    public static void printTime(LocalDateTime when) {
        LocalDateTime currentTime = LocalDateTime.now();
        long minute = ChronoUnit.MINUTES.between(when, currentTime);
        long hour = ChronoUnit.HOURS.between(when, currentTime);
        long day = ChronoUnit.DAYS.between(when, currentTime);

            if (minute< 2) {
                System.out.print("Только что ");
            }
            else if (hour  < 1) {
                System.out.print( minute+ DislForm(minute, DislenctionForms.ETime.MINUTE) +" назад ");
            }
            else if (day  < 1 ) {
                System.out.print(hour  + DislForm(hour, DislenctionForms.ETime.HOUR)+ " назад ");
            }
            else {
                if (day < 2) {
                    System.out.print("Вчера ");
                }
                else {
                    System.out.print(day  + DislForm(day, DislenctionForms.ETime.DAY)+ " назад ");
                }
            }
    }

}
