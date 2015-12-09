package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.exceptions.InvalidLocationException;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.exceptions.LocationDefinitionErrorException;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.exceptions.TwitterStreamException;
import twitter4j.*;

import java.net.MalformedURLException;
import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

/**
 * Created by thefacetakt on 11.10.15.
 */
public class TweetsGetter {
    static final double RADIUS = 10;
    static final String RADIUS_UNIT = "km";

    private PlaceLocationResolver geoResolver;

    public TweetsGetter(PlaceLocationResolver resolver) {
        this.geoResolver = resolver;
    }

    public List<String> getTweetsOnce(JCommanderSetting jCommanderSettings,
                                      Location currentLocation,
                                      Twitter twitter)
            throws TwitterStreamException {
        String queryString = jCommanderSettings.getQuery();
        if (queryString.equals("")) {
            throw new TwitterStreamException("Empty query");
        }

        Query query = new Query(jCommanderSettings.getQuery()).geoCode(
                new GeoLocation(
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude()), RADIUS,
                RADIUS_UNIT);

        query.setCount(jCommanderSettings.getLimit());

        for (int numberOfTry = 0; numberOfTry
                < PlaceLocationResolver.MAX_NUMBER_OF_TRIES;
             ++numberOfTry) {
            try {

                return twitter.search(query).getTweets()
                        .stream()
                        .filter(e -> !e.isRetweet()
                                || !jCommanderSettings.isHideRetweets())
                        .map(Formatter::formatTweet)
                        .collect(toList());
            } catch (TwitterException e) {
                if (numberOfTry + 1
                        == PlaceLocationResolver.MAX_NUMBER_OF_TRIES) {
                    throw new TwitterStreamException("Error while search", e);
                }
            }
        }
        throw new TwitterStreamException("Unexpected end of "
                + "TweetsOnce function");
    }

    public void getTwitterStream(JCommanderSetting jCommanderSetting,
                                 Location currentLocation,
                                 Consumer<String> tweetFunction,
                                 twitter4j.TwitterStream twitterStream) {
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
                    tweetFunction.accept(Formatter.formatTweet(status));
                }
            }
        });

        twitterStream.filter(new FilterQuery().track(jCommanderSetting
                                .getQueries().toArray(
                                        new String[jCommanderSetting
                                                .getQueries().size()]
                                )
                )
        );
    }
}
