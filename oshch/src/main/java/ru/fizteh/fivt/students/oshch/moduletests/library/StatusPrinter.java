package ru.fizteh.fivt.students.oshch.moduletests.library;

import twitter4j.Status;

import static ru.fizteh.fivt.students.oshch.moduletests.library.TimeFormatter.printTime;

public class StatusPrinter {
    private static boolean isStream = false;

    public static void setStream(boolean stream) {
        isStream = stream;
    }

    public static void printStatus(Status status) {
        if (!isStream) {
            System.out.print(printTime(status));
        }
        System.out.print("@" + status.getUser().getScreenName() + ": ");
        if (status.isRetweet()) {
            System.out.println(" ретвитнул " + "@"
                    + status.getRetweetedStatus().getUser().getScreenName() + ": "
                    + status.getRetweetedStatus().getText());
        } else {
            System.out.print(status.getText());
            if (status.getRetweetCount() != 0) {
                System.out.print("(" + status.getRetweetCount() + " ретвитов)");
            }
            System.out.print("\n");
        }
        System.out.println("----------------------------------------"
                +          "----------------------------------------");
    }
}
