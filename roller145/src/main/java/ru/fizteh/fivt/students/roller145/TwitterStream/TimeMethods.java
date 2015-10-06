package ru.fizteh.fivt.students.roller145.TwitterStream;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by riv on 27.09.15.
 */
public class TimeMethods {

    public static final int MILISEC_IN_SEC = 1000;

    public static void printTime(LocalDateTime when) {
        LocalDateTime currentTime = LocalDateTime.now();
            if (ChronoUnit.MINUTES.between(when, currentTime) < 2) {
                System.out.print("Suddenly ");
            }
            if (ChronoUnit.HOURS.between(when, currentTime)  < 1) {
                System.out.print( ChronoUnit.MINUTES.between(when, currentTime) + " minutes ago ");
            }
            if (ChronoUnit.DAYS.between(when, currentTime)  < 1 ) {
                System.out.print(ChronoUnit.HOURS.between(when, currentTime)  + " hours ago ");
            }
            else {
                if (ChronoUnit.DAYS.between(when, currentTime) < 2) {
                    System.out.print("Yesterday ");
                }
                else {
                    System.out.print(ChronoUnit.DAYS.between(when, currentTime)  + " days ago ");
                }
            }
    }

}
