package ru.fizteh.fivt.students.okalitova.moduletests.library;

import twitter4j.*;
import static java.util.stream.Collectors.toList;
import java.util.*;
import ru.fizteh.fivt.students.okalitova.twitterstreamer.NoTweetsException;
import ru.fizteh.fivt.students.okalitova.twitterstreamer.InvalidQueryException;

/**
 * Created by nimloth on 01.11.15.
 */
public class Search {
    private static final int RADIUS = 1;
    private final Twitter twitter;

    public Search(Twitter twitter) {
        this.twitter = twitter;
    }

    public Query setQuery(ParametersParser param) throws Exception {
        //set query
        if (param.getQuery().equals("")) {
            throw new InvalidQueryException("Пустая строка запроса");
        }
        Query query = new Query(param.getQuery());
        //set place
        if (!param.getPlace().equals("")) {
            if (!param.getPlace().equals("nearby")) {
                PlaceParser placeParser;
                placeParser = new PlaceParser(param.getPlace());
                GeoLocation geoLocation;
                geoLocation = new GeoLocation(placeParser.getLocation().lat,
                        placeParser.getLocation().lng);
                query.setGeoCode(geoLocation,
                        placeParser.getRadius(), Query.KILOMETERS);
            } else {
                GeoLocation geoLocation;
                geoLocation = new GeoLocation(NearbyParser.getLatLng().lat,
                        NearbyParser.getLatLng().lng);
                query.setGeoCode(geoLocation,
                        RADIUS, Query.KILOMETERS);
            }
        }
        return query;
    }

    public List<String> searchResult(ParametersParser param) throws Exception {
        Query query = setQuery(param);
        QueryResult result;
        int limit = param.getLimit();
        int statusCount = 0;
        List<Status> tweets = new ArrayList<Status>();
        do {
            result = twitter.search(query);
            for (Status status : result.getTweets()) {
                tweets.add(status);
                statusCount++;
                limit--;
                if (limit == 0) {
                    break;
                }
            }
            query = result.nextQuery();
        } while (query != null && limit > 0);
        if (statusCount == 0) {
            throw new NoTweetsException("Нет подходящих твитов");
        }

        return tweets.stream().map(new StatusParser(param)::getStatus).collect(toList());
    }
}
