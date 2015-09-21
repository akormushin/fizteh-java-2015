package ru.fizteh.fivt.students.zakharovas.TwitterStream;

import java.util.Date;

public class StringFormater {
    public static String retweetFormat(int numberOfretweets) {
        if (numberOfretweets >= 11 && numberOfretweets <= 19) {
            return "ретвитов";
        }
        switch (numberOfretweets % 10) {
            case 0:
                return "ретвитов";
            case 1:
                return "ретвит";
            case 2:
            case 3:
            case 4:
                return "ретвита";
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return "ретвитов";
            default:
                return null;
        }
    }

    public static String dateFormater(Date date) {
        return date.toString();
    }

}
