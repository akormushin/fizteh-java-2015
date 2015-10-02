package ru.fizteh.fivt.students.sergmiller.twitterStream;
//import  ru.fizteh.fivt.students.sergmiller.twitterStream.GeolocationResolver;

import com.beust.jcommander.ParameterException;
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
import java.time.LocalTime;
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
        twitter4j.TwitterStream twStream = twitter4j
                .TwitterStreamFactory.getSingleton();

        //   Location curLocation = getCurLocation(twStream, jCommanderParsed);

        StatusListener listener = new StatusAdapter() {
            @Override
            public void onStatus(final Status status) {
                if (jCommanderParsed.isHideRetweets() && status.isRetweet()) {
                    return;
                }
                printTweet(status, jCommanderParsed);
            }
        };

        //   FilterQuery query = new FilterQuery(jCommanderParsed.getQuery());
        //  GeoQuery curGeoQuery = new GeoQuery(jCommanderParsed.getLocation());
        //  ResponseList<Place> correctPlaces
        // = twStream.searchPlaces(curGeoQuery);
        ArrayList<GeoQuery> points = new ArrayList<>();
        String[] queries = jCommanderParsed
                .getQuery().toArray(
                        new String[jCommanderParsed.getQuery().size()]);
        twStream.addListener(listener);
        twStream.filter(new FilterQuery().track(queries));
        //  twStream.filter(new Filter.
    }

    static final double EARTH_RADIUS = 6371;
    static final String RADIUS_UNIT = "km";
    static final String NEARBY = "nearby";


    public static double getSphereDist(double latitude1, double longitude1,
                                       double latitude2, double longitude2) {
        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);
        longitude1 = Math.toRadians(longitude1);
        longitude2 = Math.toRadians(longitude2);
        return EARTH_RADIUS
                * Math.acos(Math.sin(latitude1)
                * Math.sin(latitude2)
                + Math.cos(latitude1)
                * Math.cos(latitude2)
                * Math.cos(longitude1 - longitude2));
    }

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

                String joinedQuery = "*";
                if (!jCommanderParsed.getQuery().isEmpty()) {
                    joinedQuery = String.join(" ", jCommanderParsed.getQuery());
                }
                Query query = new Query(joinedQuery);
                String curLocationRequest = "";
                try {
                    if (jCommanderParsed.getLocation() != "") {
                        if (jCommanderParsed.getLocation().equals(NEARBY)) {
                            curLocationRequest = GeoLocationResolver.getNameOfCurrentLocation();
                        } else {
                            curLocationRequest = jCommanderParsed.getLocation();
                        }
                        Pair<GeoLocation, Double> geoParams = GeoLocationResolver
                                .getGeoLocation(curLocationRequest);
                        query.geoCode(geoParams.getKey(), geoParams.getValue(), RADIUS_UNIT);
                        System.out.println("location is " + curLocationRequest
                                + ", latitude :"
                                + geoParams.getKey().getLatitude()
                                + " longitude :"
                                + geoParams.getKey().getLongitude()
                                + ", radius(km): "
                                + geoParams.getValue());
                    }
                } catch (IOException | GettingMyLocationException e) {
                    e.getMessage();
                    System.out.println("Не могу определить регион=(\n" + "Поиск по World:");
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
                    System.out.println("\nПо запросу "
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
                    System.out.println(twExp.getMessage());
                    System.err.println("Что-то пошло не так=(\n"
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
    public static String getTime(final long createdTime) {
        long currentTime = System.currentTimeMillis();
        long todaySeconds = LocalTime.now().toSecondOfDay();
        long seconds = (long) (currentTime - createdTime)
                / (MILISECONDS_IN_SECONDS);

        int minutes = (int) (currentTime - createdTime)
                / (MILISECONDS_IN_SECONDS * SECONDS_IN_MINUTE);

        int hours = (int) (currentTime - createdTime)
                / (MILISECONDS_IN_SECONDS * SECONDS_IN_MINUTE * MINUTES_IN_HOUR);

        int days = (int) (currentTime - createdTime)
                / (MILISECONDS_IN_SECONDS * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY);
        if (seconds < TWO * SECONDS_IN_MINUTE) {
            return "Только что";
        }

        if (minutes < MINUTES_IN_HOUR) {
            return minutes + " " + getDeclensionForm("минута", minutes) + " назад";
        }

        if (todaySeconds - seconds > 0) {
            return hours + " " + getDeclensionForm("час", hours) + " назад";
        } else {
            if (seconds - todaySeconds
                    < SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY) {
                return "Вчера";
            } else {
                return days + " " + getDeclensionForm("день", days) + " назад";
            }
        }
    }

    private static String[] retweetForms = {"ретвит", "ретвитов", "ретвита"};
    private static String[] minuteForms = {"минута", "минут", "минуты"};
    private static String[] hourForms = {"час", "часов", "часа"};
    private static String[] dayForms = {"день", "дней", "дня"};
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
     *
     * @param word         is input word
     * @param numberOfForm is number of correct form for @word in array of forms
     * @return correct form of @word
     */
    public static String getForm(final String word, final int numberOfForm) {
        String form = new String();
        switch (word) {
            case "ретвит":
                form = retweetForms[numberOfForm];
                break;
            case "минута":
                form = minuteForms[numberOfForm];
                break;
            case "час":
                form = hourForms[numberOfForm];
                break;
            case "день":
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
    public static String getDeclensionForm(final String word, final int count) {
        if (count % MOD100 >= LEFT_BOUND_MOD_100
                && count % MOD100 <= RIGHT_BOUND_MOD_100) {
            return getForm(word, 1);
        }

        int countMod10 = count % MOD10;
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
        while (scan.hasNext()) {
            int oneStatement = 0;
        }
        scan.close();
        //System.exit(0);
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
            if (jCommanderParsed.isHelp() || jCommanderParsed.getQuery().size() == 0) {
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

        System.out.println("Твиты по запросу "
                        + String.join(", ", jCommanderParsed.getQuery())
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
            System.out.print(getTime(status.getCreatedAt().getTime()) + " ");
        }
        System.out.print(
                highlightUserName(status.getUser().getScreenName()));
        if (!status.isRetweet()) {
            System.out.print(
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
                    highlightUserName(parsedText[1].split("@|:")[1]));

            for (int i = 2; i < parsedText.length; ++i) {
                System.out.print(" " + parsedText[i]);
            }
        }
        System.out.println(tweetsSeparator());
    }
}




