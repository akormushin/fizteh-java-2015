package ru.fizteh.fivt.students.sergmiller.twitterStream;

import twitter4j.*;
import twitter4j.StatusListener;
import com.beust.jcommander.JCommander;

//import java.util.ArrayList;
import java.util.List;
import java.util.Date;

//import java.util.ArrayList;
//import java.util.ArrayList;
//import java.util.List;
//import sun.jvm.hotspot.utilities.Assert;
//import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by sergmiller on 15.09.15.
 */
final class TwitterStream {
    /**
     * Javadoc comment.
     */
    private TwitterStream() {
    }

    /**
     * Get blue letter. WARNING: ONLY FOR OS X
     */
    static final String ANSI_RESET = "\u001B[0m";
    /**
     * Return white color of word.
     */
    static final String ANSI_BLUE = "\u001B[34m";

    /**
     * Stream mod.
     * @param jCommanderParsed is class with query's info
     */
    public static void printTwitterStream(
            final JCommanderParser jCommanderParsed) {
        twitter4j.TwitterStream twStream = twitter4j
                .TwitterStreamFactory.getSingleton();


        StatusListener listener = new StatusAdapter() {
            @Override
            public void onStatus(final Status status) {
                if (!jCommanderParsed.isHideRetweets() || !status.isRetweet()) {
                    printTweet(status, jCommanderParsed);
                }
            }
        };

     //   FilterQuery query = new FilterQuery(jCommanderParsed.getQuery());
        String[] queries = jCommanderParsed
                .getQuery().toArray(
                        new String[jCommanderParsed.getQuery().size()]);
        twStream.addListener(listener);
        twStream.filter(new FilterQuery().track(queries));
      //  twStream.filter(new Filter.
    }

    /**
     * Mod with print limited quantity of text.
     * @param jCommanderParsed is class with query's info
     * @throws TwitterException is kind of exception
     */
    public static void printTwitterLimited(
            final JCommanderParser jCommanderParsed) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();

        Query query = new Query(String.join(" ", jCommanderParsed.getQuery()));

        query.setCount(jCommanderParsed.getLimit());

        QueryResult request = twitter.search(query);

        List<Status> tweets = request.getTweets();

        Boolean existTweets = false;

        for (Status status: tweets) {
            if (!jCommanderParsed.isHideRetweets() || !status.isRetweet()) {
                printTweet(status, jCommanderParsed);
                existTweets = true;
            }
        }

        if (!existTweets) {
            System.out.println("\nПо запросу "
                    + String.join(" ", jCommanderParsed.getQuery())
                    + " для "
                    + jCommanderParsed.getLocation()
                    + " ничего не найдено=(\n\n"
                    + "Рекомендации:\n\n"
                    + "Убедитесь, что все слова написаны без ошибок.\n"
                    + "Попробуйте использовать "
                    + "другие ключевые слова.\n"
                    + "Попробуйте использовать "
                    + "более популярные ключевые слова."
            );
        }
    }

    /**
     * Getting time info.
     * @param date is current time
     * @return new string with special format
     */
    public static String getTime(final Date date) {
        return date.toString();
    }

    /**
     * mods and ranges for getting correct declension form.
     */
    public static final int MOD100 = 100;
    public static final int MOD10 = 10;
    public static final int LEFTBOUNDMOD100IS10 = 10;
    public static final int RIGHTBOUNDMOD100IS20 = 20;

    public static final int ONE = 1;
    public static final int FIVE = 5;

    private static String[] retweetForms = {"ретвит", "ретвитов", "ретвита"};
/*
    /**
     * Ansesstor method.
     * @param numberOfForm number in array
     * @return string
     *//*
    public static String getRetweetForm(final int numberOfForm) {
        return retweetForms[numberOfForm];
    }*/

    /**
     * Getting correct form for current word.
     * @param word is input word
     * @param numberOfForm is number of correct form for @word in array of forms
     * @return correct form of @word
     */
    public static String getForm(final String word, final int numberOfForm) {
        String form = new String();
        switch (word) {
            case "ретвит":
                form =  retweetForms[numberOfForm];
                break;
            default:
                break;
        }
        return form;
    }

    /**
     * General function for getting correct declension form for current word.
     * @param word is input word
     * @param count is quantity of object with name @word
     * @return string with correct form of @word
     */
    public static String getDeclensionForm(final String word, final int count) {
        if (count % MOD100 >= LEFTBOUNDMOD100IS10
                && count % MOD100 <= RIGHTBOUNDMOD100IS20) {
            return getForm(word, 1);
        }

        int countMod10 = count % MOD10;
        if (countMod10 == ONE) {
            return getForm(word, 0);
        }
        if (countMod10 > ONE  && countMod10 < FIVE) {
            return getForm(word, 2);
        }
        return getForm(word, 1);
    }

    /**
     * Print general info about TwitterStream.
     * @param jCommanderSettings is JC params
     */
    public static void printHelpMan(final JCommander jCommanderSettings) {
        jCommanderSettings.usage();
    }

    /**
     * Main function of TwitterStream.
     * @param args is input parameters
     * @throws TwitterException some exception
     */
    public static void main(final String[] args) throws TwitterException {
        JCommanderParser jCommanderParsed = new JCommanderParser();

        JCommander jCommanderSettings = new JCommander(jCommanderParsed, args);

      //  System.out.print(jCommanderParsed.getQuery());


        jCommanderSettings.setProgramName("TwitterStream");

        /*
        for (int i = 0; i < jCommanderParsed.getQuery().size(); ++i) {
            System.out.print(jCommanderParsed.getQuery().get(i) + " ");
        }

        System.out.println();*/

        if (jCommanderParsed.isHelp()) {
            printHelpMan(jCommanderSettings);
            return;
        }
        //Твиты по запросу <query> для <location>:
        System.out.println("Твиты по запросу "
                + String.join(" ", jCommanderParsed.getQuery())
                + " для "
                + jCommanderParsed.getLocation()
                + ":");

        if (jCommanderParsed.isStream()) {
            printTwitterStream(jCommanderParsed);
        } else {
            printTwitterLimited(jCommanderParsed);
        }
    }

    /**
     * Print current tweet.
     * @param status is info about tweet
     * @param jCommanderParsed is JC params
     */
    public static void printTweet(final Status status,
                                  final JCommanderParser jCommanderParsed) {
        if (!jCommanderParsed.isStream()) {
            System.out.print(getTime(status.getCreatedAt()) + " ");
        }
        System.out.print(
                "@"
                + ANSI_BLUE
                + status.getUser().getScreenName()
                + ANSI_RESET
                + ": "
        );
        if (!status.isRetweet()) {
            System.out.println(
                    status.getText()
                    + " ("
                    + status.getRetweetCount()
                    + " "
                    + getDeclensionForm("ретвит", status.getRetweetCount())
                    + ")"
            );
        } else {
            System.out.print("ретвитнул ");
            String[] parsedText = status.getText().split(" ");

            System.out.print(
                            "@"
                            + ANSI_BLUE
                            + parsedText[1].split("@|:")[1]
                            + ANSI_RESET
                            + ": "
            );

            for (int i = 2; i < parsedText.length; ++i) {
                System.out.print(" " + parsedText[i]);
            }

            System.out.println();
        }
    }
}




