package ru.fizteh.fivt.students.sergmiller.twitterStream;
//import  ru.fizteh.fivt.students.sergmiller.twitterStream.GeolocationResolver;

import com.beust.jcommander.ParameterException;
//import org.json.*;
import org.json.JSONException;
import ru.fizteh.fivt.students.sergmiller.twitterStream.exceptions.GettingMyLocationException;
import twitter4j.*;
import twitter4j.StatusListener;
import com.beust.jcommander.JCommander;

//import java.util.ArrayList;
//import java.time.LocalDate;
//import java.time.Instant;
//import java.time.Instant;
//import java.time.Period;
//import java.net.MalformedURLException;
import java.time.LocalDateTime;
//import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.io.IOException;
//import java.time.LocalDateTime;
//import java.lang.Math;

import javafx.util.Pair;

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
     * Get blue letter.
     */
    static final String ANSI_RESET = "\u001B[0m";
    /**
     * Return white color of word.
     */
    static final String ANSI_BLUE = "\u001B[34m";

    /**
     * mods and ranges for getting correct declension form.
     */
    public static final int MOD100 = 100;
    public static final int MOD10 = 10;
    public static final int LEFT_BOUND_MOD_100 = 10;
    public static final int RIGHT_BOUND_MOD_100 = 20;

    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int HOURS_IN_DAY = 24;
    public static final int MILISECONDS_IN_SECONDS = 1000;

    public static final int FIVE = 5;
    public static final int MAX_QUANTITY_OF_TRIES = 2;
    public static final int SEPARATOR_LENGTH = 80;
/*
    public static  Location getCurLocation(twitter4j.TwitterStream twStream,
     JCommanderParser jCommanderParsed) {
        Place curPlace =
    }*/

    /**
     * Stream mod.
     *
     * @param jCommanderParsed is class with query's info
     */
    public static void printTwitterStream(
            final JCommanderParser jCommanderParsed) {
        String curLocationRequest = "";
        Pair<GeoLocation, Double> geoParams = new Pair(new GeoLocation(0, 0), new Double(0));
        try {
            if (!jCommanderParsed.getLocation().equals("")) {
                if (jCommanderParsed.getLocation().equals(NEARBY)) {
                    curLocationRequest = GeoLocationResolver.getNameOfCurrentLocation();
                } else {
                    curLocationRequest = jCommanderParsed.getLocation();
                }
                geoParams = GeoLocationResolver
                        .getGeoLocation(curLocationRequest);
            }
        } catch (IOException | JSONException | GettingMyLocationException e) {
            e.getMessage();
            System.err.println("Не могу определить регион=(\n" + "Поиск по World:");
            curLocationRequest = "World";
        }


        twitter4j.TwitterStream twStream = twitter4j
                .TwitterStreamFactory.getSingleton();

        //   Location curLocation = getCurLocation(twStream, jCommanderParsed);
        //  Query query = new

        final double locationLatitude = geoParams.getKey().getLatitude();
        final double locationLongitude = geoParams.getKey().getLongitude();
        final double locationRadius = geoParams.getValue();
        StatusListener listener = new StatusAdapter() {
            @Override
            public void onStatus(final Status status) {
                if (jCommanderParsed.isHideRetweets() && status.isRetweet()) {
                    return;
                }

                if (!jCommanderParsed.getLocation().equals("")) {
                    final double curTweetLatitude;
                    final double curTweetLongitude;
                    if (status.getGeoLocation() != null) {
                        curTweetLatitude = status.getGeoLocation().getLatitude();
                        curTweetLongitude = status.getGeoLocation().getLongitude();

                    } else {
                        if (status.getUser().getLocation() != null) {
                            try {
                                GeoLocation curTweetLocation = GeoLocationResolver.getGeoLocation(
                                        status.getUser().getLocation()).getKey();
                                curTweetLatitude = curTweetLocation.getLatitude();
                                curTweetLongitude = curTweetLocation.getLongitude();
                            } catch (IOException | org.json.JSONException e) {
                                return;
                            }
                        } else {
                            return;
                        }
                    }

                    if (GeoLocationResolver.getSphereDist(
                            locationLatitude,
                            locationLongitude,
                            curTweetLatitude,
                            curTweetLongitude) > locationRadius) {
                        return;
                    }
                }

                printTweet(status, jCommanderParsed);
                try {
                    Thread.sleep(MILISECONDS_IN_SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        String[] queries = jCommanderParsed
                .getQuery().toArray(
                        new String[jCommanderParsed.getQuery().size()]);
        twStream.addListener(listener);
        if (!jCommanderParsed.getQuery().equals("")) {
            twStream.filter(new FilterQuery().track(queries));
        } else {
            twStream.sample();
        }
    }

    static final String RADIUS_UNIT = "km";
    static final String NEARBY = "nearby";

    /**
     * Mod with print limited quantity of text.
     *
     * @param jCommanderParsed is class with query's info
     * @throws TwitterException is kind of exception
     */
    public static void printTwitterLimited(
            final JCommanderParser jCommanderParsed) {
        int currentQuantityOfTries = 0;
        while (currentQuantityOfTries < MAX_QUANTITY_OF_TRIES) {
            try {
                Twitter twitter = TwitterFactory.getSingleton();

                String joinedQuery = "";
                if (!jCommanderParsed.getQuery().isEmpty()) {
                    joinedQuery = String.join(" ", jCommanderParsed.getQuery());
                }
                Query query = new Query(joinedQuery);
                String curLocationRequest = "";
                try {
                    if (!jCommanderParsed.getLocation().equals("")) {
                        if (jCommanderParsed.getLocation().equals(NEARBY)) {
                            curLocationRequest = GeoLocationResolver.getNameOfCurrentLocation();
                        } else {
                            curLocationRequest = jCommanderParsed.getLocation();
                        }
                        Pair<GeoLocation, Double> geoParams = GeoLocationResolver
                                .getGeoLocation(curLocationRequest);
                        query.geoCode(geoParams.getKey(), geoParams.getValue(), RADIUS_UNIT);
                        System.out.println("Location is " + curLocationRequest
                                + ", latitude :"
                                + geoParams.getKey().getLatitude()
                                + " longitude :"
                                + geoParams.getKey().getLongitude()
                                + ", radius(km): "
                                + geoParams.getValue()
                                + "\n"
                                + tweetsSeparator());
                    }
                } catch (IOException | JSONException | GettingMyLocationException e) {
                    e.getMessage();
                    System.err.println("Не могу определить регион=(\n" + "Поиск по World:");
                    curLocationRequest = "World";
                }

                query.setCount(jCommanderParsed.getLimit());


                QueryResult request = twitter.search(query);

                List<Status> tweets = request.getTweets();

                Boolean existTweets = false;

                Collections.reverse(tweets);

                for (Status status : tweets) {
                    if (!jCommanderParsed.isHideRetweets()
                            || !status.isRetweet()) {
                        printTweet(status, jCommanderParsed);
                        existTweets = true;
                    }
                }

                if (!existTweets) {
                    System.err.println("\nПо запросу "
                                    + String.join(", "
                                    , jCommanderParsed.getQuery())
                                    + " для "
                                    + curLocationRequest
                                    + " ничего не найдено=(\n\n"
                                    + "Рекомендации:\n\n"
                                    + "Убедитесь, что все слова"
                                    + " написаны без ошибок.\n"
                                    + "Попробуйте использовать "
                                    + "другие ключевые слова.\n"
                                    + "Попробуйте использовать "
                                    + "более популярные ключевые слова."
                    );
                }
                currentQuantityOfTries = MAX_QUANTITY_OF_TRIES;
            } catch (TwitterException twExp) {
                ++currentQuantityOfTries;
                if (currentQuantityOfTries == MAX_QUANTITY_OF_TRIES) {
                    System.err.println(twExp.getMessage()
                            + "\nЧто-то пошло не так=(\n"
                            + "Проверьте наличие соединия.");
                    return;
                }
            }
        }
    }

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
            return minutes + " " + getDeclensionForm(Word.MINUTE, minutes) + " назад";
        }

        if (ChronoUnit.DAYS.between(createdTime, currentTime) < 1) {
            hours = ChronoUnit.HOURS.between(createdTime, currentTime);
            return hours + " " + getDeclensionForm(Word.HOUR, hours) + " назад";
        }

        if (ChronoUnit.DAYS.between(createdTime, currentTime) == 1) {
            return "Вчера";
        }

        days = ChronoUnit.DAYS.between(createdTime, currentTime);
        return days + getDeclensionForm(Word.DAY, days) + "назад";
    }

    private static String[] retweetForms = {"ретвит", "ретвитов", "ретвита"};
    private static String[] minuteForms = {"минута", "минут", "минуты"};
    private static String[] hourForms = {"час", "часов", "часа"};
    private static String[] dayForms = {"день", "дней", "дня"};

    public enum Word { RETWEET, MINUTE, HOUR, DAY }

    /**
     * Getting correct form for current word.
     *
     * @param word         is input word
     * @param numberOfForm is number of correct form for @word in array of forms
     * @return correct form of @word
     */
    public static String getForm(Word word, final int numberOfForm) {
        String form = new String();
        switch (word) {
            case RETWEET:
                form = retweetForms[numberOfForm];
                break;
            case MINUTE:
                form = minuteForms[numberOfForm];
                break;
            case HOUR:
                form = hourForms[numberOfForm];
                break;
            case DAY:
                form = dayForms[numberOfForm];
                break;
            default:
                break;
        }
        return form;
    }

    /**
     * General function for getting correct declension form for current word.
     *
     * @param word  is input word
     * @param count is quantity of object with name @word
     * @return string with correct form of @word
     */
    public static String getDeclensionForm(Word word, final long count) {
        if (count % MOD100 >= LEFT_BOUND_MOD_100
                && count % MOD100 <= RIGHT_BOUND_MOD_100) {
            return getForm(word, 1);
        }

        long countMod10 = count % MOD10;
        if (countMod10 == ONE) {
            return getForm(word, 0);
        }
        if (countMod10 > ONE && countMod10 < FIVE) {
            return getForm(word, 2);
        }
        return getForm(word, 1);
    }

    /**
     * Print general info about TwitterStream.
     *
     * @param jCommanderSettings is JC params
     */
    public static void printHelpMan(final JCommander jCommanderSettings) {
        jCommanderSettings.usage();
    }

    static void exitWithCtrlD() {
        Scanner scan = new Scanner(System.in);
        try {
            while (scan.hasNext()) {
                int someNeverUsedStatement = 0;
            }
        } finally {
            scan.close();
            System.exit(0);
        }
    }

    /**
     * Main function of TwitterStream.
     *
     * @param args is input parameters
     * @throws TwitterException some exception
     */
    public static void main(final String[] args) {
        JCommanderParser jCommanderParsed = new JCommanderParser();

        try {
            JCommander jCommanderSettings = new JCommander(jCommanderParsed, args);
            if (jCommanderParsed.isHelp()
                    || (!jCommanderParsed.isStream()
                    && jCommanderParsed.getQuery().size() == 0)) {
                throw new ParameterException("");
            }
        } catch (ParameterException pe) {
            JCommander jCommanderHelper = new JCommander(jCommanderParsed, new String[]{"-q", ""});
            jCommanderHelper.setProgramName("TwitterStream");
            printHelpMan(jCommanderHelper);
            return;
        }

        String printLocation = "World";
        if (jCommanderParsed.getLocation() != "") {
            printLocation = jCommanderParsed.getLocation();
        }

        String printedQuery = String.join(", ", jCommanderParsed.getQuery());
        if (printedQuery.equals("")) {
            printedQuery = "-";
        }
        System.out.println("Твиты по запросу "
                        + printedQuery
                        + " для " + printLocation
                        + tweetsSeparator()
        );


        if (jCommanderParsed.isStream()) {
            printTwitterStream(jCommanderParsed);
            exitWithCtrlD();
        } else {
            printTwitterLimited(jCommanderParsed);
        }
        //System.out.println(LocalTime.now() + " " + LocalTime.now().toSecondOfDay());
    }

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
            System.out.print("[" + getTime(status.getCreatedAt().toInstant()
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
                            + getDeclensionForm(Word.RETWEET, status.getRetweetCount())
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




