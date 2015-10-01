package ru.fizteh.fivt.students.roller145.TwitterStream;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by riv on 27.09.15.
 */
public class TimeMethods {
    public static final int SEC_IN_MIN = 60;
    public static final int MIN_IN_HOUR = 60;
    public static final int HOURS_IN_DAY = 24;
    public static final int MILISEC_IN_SEC = 1000;
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void printTime(Date when) {
        long currentTime = System.currentTimeMillis();
            long todaySeconds = LocalTime.now().toSecondOfDay();
            long seconds = (long) (currentTime - when.getTime()) / (MILISEC_IN_SEC);

            int minutes = (int) (currentTime - when.getTime()) / (MILISEC_IN_SEC * SEC_IN_MIN);

            int hours = (int) (currentTime - when.getTime())
                    / (MILISEC_IN_SEC * SEC_IN_MIN * MIN_IN_HOUR);

            int days = (int) (currentTime - when.getTime())
                    / (MILISEC_IN_SEC * SEC_IN_MIN * MIN_IN_HOUR * HOURS_IN_DAY);
            if (seconds < 2* SEC_IN_MIN) {
                System.out.print("Suddenly ");
            }
            if (minutes < MIN_IN_HOUR) {
                System.out.print( minutes + " minutes ago ");
            }
            if (todaySeconds - seconds > 0) {
                System.out.print(hours + " hours ago ");
            }
            else {
                if (seconds - todaySeconds < SEC_IN_MIN * MIN_IN_HOUR * HOURS_IN_DAY) {
                    System.out.print("Yesterday ");
                }
                else {
                    System.out.print(days + " days ago");
                }
            }
    }

}
