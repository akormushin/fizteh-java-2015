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

import java.io.PrintStream;
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

    static void printSeparator(PrintStream out) {
        for (int i = 0; i < MINUSES_COUNT; ++i) {
            out.print("-");
        }
        out.println();
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
    static void printNick(String nick, PrintStream out) {
        out.print("@" + ANSI_BLUE + nick + ": " + ANSI_RESET);
    }

    static void printTweet(Status status, PrintStream out) {
        printNick(status.getUser().getScreenName(), out);
        if (!status.isRetweet()) {
            out.print(status.getText());

            if (status.getRetweetCount() != 0) {
                out.print(" ("
                        + status.getRetweetCount() + " "
                        + Declenser.retweetDeclension(status.getRetweetCount())
                        + ")"
                );
            }
        } else {
            out.print("ретвитнул ");
            String[] splitText = status.getText().split(":");

            printNick(status.getRetweetedStatus().getUser().getScreenName(),
                    out);
            ArrayList<String> originalText = new ArrayList<>(
                    Arrays.asList(splitText)
                    .subList(1, splitText.length -  1)
            );

            out.print(String.join(":", originalText));
        }
        out.println();
    }

    static void printTweetsOnce(JCommanderSetting jCommanderSettings,
                                Location currentLocation, String queryString,
                                PrintStream out) throws TwitterException {

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
                    out.println("Не найдено ни одного твита");
                    printSeparator(out);
                }

                for (Status status : tweets) {
                    if (!status.isRetweet()
                            || !jCommanderSettings.isHideRetweets()) {

                        out.print("["
                                + TimeFormatter.formatTime(currentTime,
                                status.getCreatedAt().getTime()) + "] ");

                        printTweet(status, out);

                        printSeparator(out);
                    }
                }
                numberOfTries = MAX_NUMBER_OF_TRIES;
            } catch (TwitterException te) {
                ++numberOfTries;
                if (numberOfTries == MAX_NUMBER_OF_TRIES) {
                    throw new TwitterException(te.getMessage()
                            + "\n" + "Something went terribly wrong, "
                            + "probably - connection + error");
                }
            }
        } while (numberOfTries < MAX_NUMBER_OF_TRIES);
    }



    static void printTwitterStream(JCommanderSetting jCommanderSetting,
                                   Location currentLocation, PrintStream out) {

        twitter4j.TwitterStream twitterStream =
                new TwitterStreamFactory().getInstance();

        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {

                if (jCommanderSetting.isHideRetweets()
                        && status.isRetweet()) {
                    return;
                }

                Location tweetLocation;
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

                if (SphereDistanceResolver
                        .sphereDistance(tweetLocation.getLatitude(),
                                tweetLocation.getLongitude(),
                                currentLocation.getLatitude(),
                                currentLocation.getLongitude()
                        ) < RADIUS) {
                    printTweet(status, out);
                    printSeparator(out);
                }
            }
        });

        String[] trackArray = jCommanderSetting.getQueries().toArray(
                new String[jCommanderSetting.getQueries().size()]
        );

        twitterStream.filter(new FilterQuery().track(trackArray));
    }

    static void readUntilCtrlD() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                scanner.next();
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) throws TwitterException {

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
        printSeparator(System.out);

        if (!jCommanderSettings.isStream()) {
            printTweetsOnce(jCommanderSettings, currentLocation
                    , queryString, System.out);
        } else {
            printTwitterStream(jCommanderSettings, currentLocation, System.out);
            readUntilCtrlD();
        }
    }

}
