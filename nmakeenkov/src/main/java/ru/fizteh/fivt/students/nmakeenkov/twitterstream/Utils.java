package ru.fizteh.fivt.students.nmakeenkov.twitterstream;

public class Utils {
    private static final long JUST_NOW = 2 * 60; // 2 minutes
    private static final long MILLIS_PER_SECOND = 1000;
    private static final long SECS_PER_MIN = 60;
    private static final long MINS_PER_HOUR = 60;
    private static final long HOURS_PER_DAY = 60;
    private static final int MY_10 = 10;
    private static final int MY_11 = 11;
    private static final int MY_5 = 5;
    private static final int MY_15 = 15;

    /*
      0 - minutes
      1 - hours
      2 - days
    */
    private static final String[][] TIME_POSTFIX =
            {{"минуту", "минуты", "минут"},
                    {"час", "часа", "часов"},
                    {"день", "дня", "дней"}};
    private static String timeToString(long time, int type) {
        String ans = (new Long(time)).toString() + " ";
        if (time % MY_10 == 1 && time != MY_11) {
            ans += TIME_POSTFIX[type][0];
        } else {
            if (time % MY_10 > 1 && time % MY_10 < MY_5
                    && (time < MY_5 || time > MY_15)) {
                ans += TIME_POSTFIX[type][1];
            } else {
                ans += TIME_POSTFIX[type][2];
            }
        }
        return ans + " ";
    }

    public static void printTime(long time) {
        long dt = System.currentTimeMillis() - time;
        dt /= MILLIS_PER_SECOND;
        System.out.print("[");
        if (dt <= JUST_NOW) {
            System.out.print("только что");
        } else {
            dt /= SECS_PER_MIN;
            if (dt < MINS_PER_HOUR) {
                System.out.print(timeToString(dt, 0) + "назад");
            } else {
                dt /= MINS_PER_HOUR;
                long todayStarted = System.currentTimeMillis()
                        / MILLIS_PER_SECOND;
                todayStarted /= SECS_PER_MIN * MINS_PER_HOUR;
                todayStarted %= HOURS_PER_DAY;
                if (dt < todayStarted) {
                    System.out.print(timeToString(dt, 1) + "назад");
                } else {
                    if (dt < todayStarted + HOURS_PER_DAY) {
                        System.out.print("вчера");
                    } else {
                        System.out.print(timeToString(dt / HOURS_PER_DAY, 2)
                                + "назад");
                    }
                }
            }
        }
        System.out.print("]");
    }

    private static final double EARTH_RADIUS = 6371.0;
    public static double getDistance(double lat1, double lng1,
                                     double lat2, double lng2) {
        return EARTH_RADIUS * Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng1 - lng2));
    }
}
