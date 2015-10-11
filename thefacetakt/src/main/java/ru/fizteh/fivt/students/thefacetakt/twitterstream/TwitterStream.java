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
import ru.fizteh.fivt.students.thefacetakt.twitterstream.exceptions.TwitterStreamException;
import twitter4j.*;

import java.net.MalformedURLException;
import java.util.*;

public class TwitterStream {
    static final int MAX_NUMBER_OF_TRIES = 2;

    private static PlaceLocationResolver geoResolver;

    static {
        try {
            geoResolver = new PlaceLocationResolver();
        } catch (NoKeyException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
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
                            "--query", "query");
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

        Printer tweetsPrinter = new Printer(System.out);

        TweetsGetter tweetsGetter = new TweetsGetter(geoResolver);

        tweetsPrinter.print("Твиты по запросу "
                + jCommanderSettings.getQuery() + " для "
                + currentLocation.getName());

        if (!jCommanderSettings.isStream()) {
            try {
                tweetsPrinter.printTweets(tweetsGetter.getTweetsOnce(
                        jCommanderSettings,
                        currentLocation, TwitterFactory.getSingleton()
                ));
            } catch (TwitterStreamException e) {
                e.printStackTrace();
            }
        } else {
            tweetsGetter.getTwitterStream(jCommanderSettings, currentLocation,
                    tweetsPrinter::print,
                    new TwitterStreamFactory().getInstance());
            readUntilCtrlD();
        }
    }

}
