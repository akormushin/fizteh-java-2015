package ru.fizteh.fivt.students.sergmiller.twitterStream;

import twitter4j.Status;

import java.time.ZoneId;

/**
 * Created by sergmiller on 06.10.15.
 */
public class TweetPrinter {
    public static final int SEPARATOR_LENGTH = 80;
    /**
     * Get blue letter.
     */
    static final String ANSI_RESET = "\u001B[0m";
    /**
     * Return white color of word.
     */
    static final String ANSI_BLUE = "\u001B[34m";

    public static String highlightUserName(final String userName) {
        return "@" + ANSI_BLUE + userName + ANSI_RESET + ": ";
    }

    public static String tweetsSeparator() {
        String separator = "\n";
        for (int i = 0; i < SEPARATOR_LENGTH; ++i) {
            separator += "-";
        }
        return separator;
    }

    /**
     * Print current tweet.
     *
     * @param status           is info about tweet
     * @param jCommanderParsed is JC params
     */
    public static void printTweet(final Status status,
                                  final JCommanderParser jCommanderParsed) {
        if (!jCommanderParsed.isStream()) {
            System.out.print("[" + TimeResolver.getTime(status.getCreatedAt().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime()) + "] ");
        }
        System.out.print(
                highlightUserName(status.getUser().getScreenName()));
        if (!status.isRetweet()) {
            System.out.print(
                    status.getText()
                            + " ("
                            + status.getRetweetCount()
                            + " "
                            + DeclensionResolver.getDeclensionForm(
                            DeclensionResolver.Word.RETWEET, status.getRetweetCount())
                            + ")"
            );
        } else {
            System.out.print("ретвитнул ");
            String[] parsedText = status.getText().split(" ");

            System.out.print(
                    highlightUserName(parsedText[1].split("@|:")[1]));

            for (int i = 2; i < parsedText.length; ++i) {
                System.out.print(" " + parsedText[i]);
            }
        }
        System.out.println(tweetsSeparator());
    }
}
