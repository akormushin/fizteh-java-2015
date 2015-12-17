package ru.fizteh.fivt.students.nmakeenkov.twitterstream.library;

import twitter4j.*;
import org.json.JSONException;

import java.io.IOException;

public class TwitterUtils {
    private static final String NEARBY = "nearby";

    public static Query buildQuery(final CommandLineParametersParser.Parameters params) throws JSONException {
        Query query = new Query(params.getQuery());
        if (params.getPlace().length() > 0) {
            double[] place = null;
            if (params.getPlace().equals(NEARBY)) {
                while (place == null) {
                    try {
                        place = MyMaps.getMyCoords();
                    } catch (IOException ex) { }
                }
            } else {
                place = MyMaps.getCoordsByPlace(params.getPlace());
            }
            query.geoCode(new GeoLocation(place[0], place[1]), place[2], "km");
        }
        return query;
    }

    public static String buildTweet(Status status, boolean hideRT) {
        StringBuilder ans = new StringBuilder("@");
        ans.append(status.getUser().getName());
        if (status.isRetweet()) {
            ans.append(" ретвитнул @").append(status.getRetweetedStatus().getUser().getName()).
                    append(": ").append(status.getRetweetedStatus().getText());
        } else {
            ans.append(": ").append(status.getText());
            if (!hideRT) {
                ans.append(" (").append(status.getRetweetCount()).append(" ретвитов)");
            }
        }
        return ans.toString();
    }
}
