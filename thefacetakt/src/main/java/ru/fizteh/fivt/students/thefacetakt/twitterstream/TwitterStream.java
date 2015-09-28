package ru.fizteh.fivt.students.thefacetakt.twitterstream;

/**
 * Created by thefacetakt on 17.09.15.
 */

import com.beust.jcommander.*;
import ru.fizteh.fivt.students.thefacetakt.twitterstream
        .exceptions.InvalidLocationException;
import ru.fizteh.fivt.students.thefacetakt.twitterstream
        .exceptions.LocationDefinitionErrorException;
import ru.fizteh.fivt.students.thefacetakt
        .twitterstream.exceptions.NoKeyException;
import twitter4j.*;

import java.net.MalformedURLException;
import java.util.*;


public class TwitterStream {

    static final int MINUSES_COUNT = 140;
    static final int MAX_NUMBER_OF_TRIES = 2;
    static final double RADIUS = 10;
    static final String RADIUS_UNIT = "km";

    private static PlaceLocationResolver geoResolver;

    static {
        try {
            geoResolver = new PlaceLocationResolver();
        } catch (NoKeyException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    static void printSeparator() {
        for (int i = 0; i < MINUSES_COUNT; ++i) {
            System.out.print("-");
        }
        System.out.println();
    }

    static Location resolveLocation(String passedLocation)
            throws LocationDefinitionErrorException, InvalidLocationException,
            MalformedURLException {
        Location result = null;
        if (passedLocation.equals(JCommanderSetting.DEFAULT_LOCATION)) {
            result = geoResolver.resolveCurrentLocation();
        } else {
            result = geoResolver.resolvePlaceLocation(passedLocation);
        }
        return result;
    }



    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_BLUE = "\u001B[34m";
    static void printNick(String nick) {

        System.out.print("@" + ANSI_BLUE + nick + ": " + ANSI_RESET);
    }


    //RT @nick:
    static final int RT_SPACE_AT_LENGTH = 4;
    static void printTweet(Status status) {
        printNick(status.getUser().getScreenName());
        if (!status.isRetweet()) {
            System.out.print(status.getText());

            if (status.getRetweetCount() != 0) {
                System.out.print(" ("
                        + status.getRetweetCount() + " "
                        + Declenser.retweetDeclension(status.getRetweetCount())
                        + ")"
                );
            }
        } else {
            System.out.print("ретвитнул ");
            String[] splittedText = status.getText().split(":");

            String originalNick = splittedText[0].substring(RT_SPACE_AT_LENGTH);
            printNick(originalNick);
            ArrayList<String> originalText = new ArrayList<String>(
                    Arrays.asList(splittedText)
                    .subList(1, splittedText.length -  1)
            );

            System.out.print(String.join(":", originalText));
        }
        System.out.println();
    }

    static void printTweetsOnce(JCommanderSetting jCommanderSettings,
                              Location currentLocation, String queryString) {

        int numberOfTries = 0;

        do {
            try {
                Twitter twitter = TwitterFactory.getSingleton();

                Query query = new Query(queryString).geoCode(
                        new GeoLocation(
                                currentLocation.getLatitude(),
                                currentLocation.getLongitude()), RADIUS,
                        RADIUS_UNIT);

                query.setCount(jCommanderSettings.getLimit());

                QueryResult result = twitter.search(query);


                long currentTime = System.currentTimeMillis();

                List<Status> tweets = result.getTweets();
                Collections.reverse(tweets);

                if (tweets.isEmpty()) {
                    System.out.println("Не найдено ни одного твита");
                    printSeparator();
                }

                for (Status status : tweets) {
                    if (!status.isRetweet()
                            || !jCommanderSettings.isHideRetweets()) {

                        System.out.print("["
                                + TimeFormatter.formatTime(currentTime,
                                status.getCreatedAt().getTime()) + "] ");

                        printTweet(status);

                        printSeparator();
                    }
                }
                numberOfTries = MAX_NUMBER_OF_TRIES;
            } catch (TwitterException te) {
                ++numberOfTries;
                if (numberOfTries == MAX_NUMBER_OF_TRIES) {
                    System.out.println(te.getMessage());
                    System.err.println("Something went terribly wrong, "
                            + "probably - connection + error");
                    System.exit(1);
                }
            }
        } while (numberOfTries < MAX_NUMBER_OF_TRIES);
    }

    static double sqr(double x) {
        return x * x;
    }

    //https://en.wikipedia.org/wiki/Great-circle_distance#Formulas
    static final double EARTH_RADIUS = 6371;
    static final double RADIANS_IN_DEGREE = Math.PI / 180;

    static double toRadians(double angle) {
        return angle * RADIANS_IN_DEGREE;
    }

    static double sphereDistance(double phi1, double lambda1,
                                 double phi2, double lambda2) {
        phi1 = toRadians(phi1);
        phi2 = toRadians(phi2);
        lambda1 = toRadians(lambda1);
        lambda2 = toRadians(lambda2);

        double deltaPhi = phi2 - phi1;
        double deltaLambda = lambda2 - lambda1;

        return 2 * Math.asin(Math.sqrt(sqr(Math.sin(deltaPhi / 2))
                + Math.cos(phi1) * Math.cos(phi2)
                        * sqr(Math.sin(deltaLambda / 2))))
                * EARTH_RADIUS;
    }

    static void printTwitterStream(JCommanderSetting jCommanderSetting,
                                   Location currentLocation) {

        twitter4j.TwitterStream twitterStream =
                new TwitterStreamFactory().getInstance();

        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {

                if (jCommanderSetting.isHideRetweets()
                        && status.isRetweet()) {
                    return;
                }

                Location tweetLocation = null;
                if (status.getGeoLocation() != null) {
                    tweetLocation = new Location(
                            status.getGeoLocation().getLatitude(),
                            status.getGeoLocation().getLongitude());
                } else {
                    if (status.getUser().getLocation() != null) {
                        try {
                            tweetLocation =
                                    geoResolver.resolvePlaceLocation(
                                            status.getUser().getLocation());
                        } catch (InvalidLocationException
                                | LocationDefinitionErrorException
                                | MalformedURLException e) {
                            return;
                        }
                    } else {
                        return;
                    }
                }

                assert tweetLocation != null;

                if (sphereDistance(tweetLocation.getLatitude(),
                        tweetLocation.getLongitude(),
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude()
                        ) < RADIUS) {
                    printTweet(status);
                    printSeparator();
                }
            }
        });

        String[] trackArray = jCommanderSetting.getQueries().toArray(
                new String[jCommanderSetting.getQueries().size()]
        );

        twitterStream.filter(new FilterQuery().track(trackArray));
    }

    static void readUntilCtrlD() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int checkstyleWantAtLeastOneStatement = 0;
            // waiting for end of input
        }
        scanner.close();
        System.exit(0);
    }

    public static void main(String[] args) {

        JCommanderSetting jCommanderSettings = new JCommanderSetting();

        try {
            JCommander jCommander = new JCommander(jCommanderSettings, args);
            jCommander.setProgramName("TwitterStream");
            if (jCommanderSettings.isHelp()) {
                throw new ParameterException("");
            }
        } catch (ParameterException pe) {
            JCommander jCommander =
                    new JCommander(jCommanderSettings,
                            new String[] {"--query", "query"});
            jCommander.setProgramName("TwitterStream");
            jCommander.usage();
            return;
        }

        Location currentLocation = null;
        try {
            currentLocation = resolveLocation(
                    jCommanderSettings.getLocation()
            );
        } catch (LocationDefinitionErrorException
                | InvalidLocationException
                | MalformedURLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        String queryString = String.join(" ", jCommanderSettings.getQueries());
        System.out.println("Твиты по запросу "
                + queryString + " для "
                + currentLocation.getName());
        printSeparator();

        if (!jCommanderSettings.isStream()) {
            printTweetsOnce(jCommanderSettings, currentLocation, queryString);
        } else {
            printTwitterStream(jCommanderSettings, currentLocation);
            readUntilCtrlD();
        }
    }

}
