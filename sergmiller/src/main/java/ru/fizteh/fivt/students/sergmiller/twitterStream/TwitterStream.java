package ru.fizteh.fivt.students.sergmiller.twitterStream;

import com.beust.jcommander.ParameterException;
import org.json.JSONException;
import ru.fizteh.fivt.students.sergmiller.twitterStream.exceptions.GettingMyLocationException;
import twitter4j.*;
import twitter4j.StatusListener;
import com.beust.jcommander.JCommander;

import java.util.*;
import java.io.IOException;
import java.util.List;

import javafx.util.Pair;


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
     * mods and ranges for getting correct declension form.
     */
    public static final int MILISECONDS_IN_SECONDS = 1000;

    public static final int MAX_QUANTITY_OF_TRIES = 2;


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
                    } else { /*
                        if (status.getUser().getLocation() != null) {
                            try {
                                GeoLocation curTweetLocation = GeoLocationResolver.getGeoLocation(
                                        status.getUser().getLocation()).getKey();
                                curTweetLatitude = curTweetLocation.getLatitude();
                                curTweetLongitude = curTweetLocation.getLongitude();
                            } catch (IOException | org.json.JSONException e) {
                                return;
                            }
                        } else {*/
                        return;
                        //}
                    }

                    if (GeoLocationResolver.getSphereDist(
                            locationLatitude,
                            locationLongitude,
                            curTweetLatitude,
                            curTweetLongitude) > locationRadius) {
                        return;
                    }
                }

                TweetPrinter.printTweet(status, jCommanderParsed);
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
        if (jCommanderParsed.getQuery().size() != 0) {
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
                        /*System.out.println("Location is " + curLocationRequest
                                + ", latitude :"
                                + geoParams.getKey().getLatitude()
                                + " longitude :"
                                + geoParams.getKey().getLongitude()
                                + ", radius(km): "
                                + geoParams.getValue()
                                + TweetPrinter.tweetsSeparator());*/
                    }
                } catch (IOException | JSONException | GettingMyLocationException e) {
                    e.getMessage();
                    System.err.println("Не могу определить регион=(\n" + "Поиск по World:");
                    curLocationRequest = "World";
                }

                query.setCount(jCommanderParsed.getLimit());

                QueryResult request;
                int quantityOfPrintedTweets = 0;
                Boolean flagLimit = false;
                List<Status> allTweets = new ArrayList<>();

                do {
                    request = twitter.search(query);

                    List<Status> tweets = request.getTweets();

                    for (Status status : tweets) {
                        if (!jCommanderParsed.isHideRetweets()
                                || !status.isRetweet()) {
                            allTweets.add(status);
                            ++quantityOfPrintedTweets;
                            if (quantityOfPrintedTweets == jCommanderParsed.getLimit()) {
                                flagLimit = true;
                                break;
                            }
                        }
                    }
                    query = request.nextQuery();
                } while (query != null && !flagLimit);

                if (allTweets.isEmpty()) {
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
                } else {
                    Collections.reverse(allTweets);

                    for (Status status : allTweets) {
                        TweetPrinter.printTweet(status, jCommanderParsed);
                    }

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
                        + TweetPrinter.tweetsSeparator()
        );


        if (jCommanderParsed.isStream()) {
            printTwitterStream(jCommanderParsed);
            exitWithCtrlD();
        } else {
            printTwitterLimited(jCommanderParsed);
        }
        //System.out.println(LocalTime.now() + " " + LocalTime.now().toSecondOfDay());
    }
}




