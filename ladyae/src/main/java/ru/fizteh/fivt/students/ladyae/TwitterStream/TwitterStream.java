package ru.fizteh.fivt.students.ladyae.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import twitter4j.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class TwitterStream {
    private static final int INF = 1000000000;
    public static final int MILI_SEC = 1;
    public static final int SEC = 1000 * MILI_SEC;
    public static final int MIN = 60 * SEC;
    public static final int HOUR = 60 * MIN;
    public static final int DAY = 24 * MIN;
    public static final int LATITUDE_MAX = INF;
    public static final int LATITUDE_MIN = -INF;
    public static final int LONGITUDE_MAX = INF;
    public static final int LONGITUDE_MIN = -INF;
    public static final int SIZE = 10000000;
    private static final double MILES = 34.5;
    private static final double RADIUS = 20;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int TEN = 10;
    public static final int ELEVEN = 11;
    public static final int TWELVE = 12;
    public static final int NEGATIVE_TWO = -2;
    public static final String[][] FORMS = {{" минут назад", " минуту назад" , " минуты назад"} ,
            {" часов назад", " час назад" , " часа назад"} ,
            {" дней назад", " день назад" , " дня назад"} ,
            {" ретвитов", " ретвит", " ретвита"}};


    public static class JCommanderParameters {
            @Parameter
            private List<String> parameters = new ArrayList<>();

            @Parameter(names = {"-q", "-query"}, description = "Tweets on request <query>")
            private String query = "MIPT";

            @Parameter(names = {"-s", "-stream"}, description = "Continuously prints the tweets")
            private boolean stream = false;

            @Parameter(names = {"-p", "-place"}, description  = "To search for a given region")
            private String place = "nearby";

            @Parameter(names = "-hideRetweets", description = "Filters out retweets")
            private boolean hidden = false;

            @Parameter(names = {"-l", "-limit"}, description = "Displays the <limit> of tweets")
            private int limit = INF;

            @Parameter(names = {"-h", "-help"}, description = "Displays help")
            private boolean help = false;

    }

    private static JCommanderParameters parameters;

    public static String parseTime(Status oneTweet) {
        LocalDateTime tweetDate = oneTweet.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime currentDate = LocalDateTime.now();

        if (ChronoUnit.MINUTES.between(tweetDate, currentDate) < 2) {
            return goodTime(-1, 0);
        } else {
            if (ChronoUnit.HOURS.between(tweetDate, currentDate) == 0) {
                return goodTime(ChronoUnit.MINUTES.between(tweetDate, currentDate), 0);
            } else {
                if (ChronoUnit.DAYS.between(tweetDate, currentDate) == 0) {
                    return goodTime(ChronoUnit.HOURS.between(tweetDate, currentDate), ONE);
                } else {
                    if (ChronoUnit.DAYS.between(tweetDate, currentDate) == 1) {
                        return goodTime(NEGATIVE_TWO, THREE);
                    } else {
                        return goodTime(ChronoUnit.DAYS.between(tweetDate, currentDate), TWO);
                    }
                }
            }
        }
    }

    public static String goodTime(long time, int index) {
        String result = null;
        if (time == -1) {
            result = "Только что";
        } else {
            if (time == NEGATIVE_TWO) {
                result = "Вчера";
            } else {
                if (time % TEN >= FIVE || time == ELEVEN || time == TWELVE || time % TEN == 0) {
                    return time + FORMS[index][0];
                } else {
                    if (time % TEN == ONE) {
                        return time + FORMS[index][ONE];
                    } else {
                        return time + FORMS[index][TWO];
                    }
                }
            }
        }
        return result;
    }

    public static void printStream(Status status) {
        StringBuilder print = new StringBuilder();
        print.append("@" + status.getUser().getScreenName() + " : ");
        if (status.isRetweet() && parameters.hidden) {
            print.delete(0, print.length());
            return;
        }
        if (status.isRetweet() && !parameters.hidden) {
            print.append(status.getText().replace("RT", "ретвитнул"));
        } else {
            print.append(status.getText());
        }
        System.out.println(print);
        System.out.println("--------------------------------------------------------------------"
                + "--------------------");
    }

    public static void printOneTweet(Status status, Query query) {
        StringBuilder string = new StringBuilder();
        string.append("[" + parseTime(status) + "] " + "@"
                + status.getUser().getScreenName() + " : ");

        if (status.isRetweet() && parameters.hidden) {
            return;
        }
        if (status.isRetweet() && !parameters.hidden) {
            string.append(status.getText().replace("RT", "ретвитнул"));
        }
        if (!status.isRetweet() && status.getRetweetCount() > 0) {
            string.append(status.getText() + " "
                    + status.getRetweetCount() + goodTime(status.getRetweetCount(), THREE));
        } else {
            string.append(status.getText());
        }
        System.out.println(string);
        System.out.println("--------------------------------------------------------------------"
                + "--------------------");
    }

    public static void findTweets(Query query, Twitter myTwitter)throws TwitterException {
        QueryResult results = myTwitter.search().search(query);

        List<Status> goodTweets = results.getTweets();
        if (goodTweets.isEmpty()) {
            System.out.println("Tweets not found by query");
        }

        for (Status status : goodTweets) {
            printOneTweet(status, query);
        }
    }

    public static double[] locationNearBy()  {

        double[] curloc = new double[2];
        try {
            String location;
            URL url = new URL("http://ipinfo.io/geo");
            InputStream currentUrl = url.openStream();
            byte[] b = new byte[SIZE + 1];
            char[] c = new char[currentUrl.read(b) + 2];
            for (int i = 0; b[i] > 0 && i < SIZE; i++) {
                c[i] = (char) b[i];
            }
            location = String.valueOf(c).split("\"loc\": \"")[1].split("\"")[0];
            currentUrl.close();
            curloc[0] = Double.parseDouble(location.split(",")[0]);
            curloc[1] = Double.parseDouble(location.split(",")[1]);
        } catch (Exception e) {
            System.out.println("Can't define place");
        }
        return curloc;
    }

    public static double[] getSquareByName(Twitter twitter) {
        double[] squareLocation = new double[FOUR];
        try {
            GeoQuery gQuery = new GeoQuery((String) null);
            gQuery.setQuery(parameters.place);
            ResponseList<Place> searchResults = twitter.searchPlaces(gQuery);
            if (searchResults.size() == 0) {
                System.err.println("The place doesn't exist");
            }

            GeoLocation[][] tmp;
            double latitudeMax = -INF;
            double latitudeMin = INF;
            double longitudeMax = -INF;
            double longitudeMin = INF;

            for (GeoLocation i : searchResults.get(0).getBoundingBoxCoordinates()[0]) {
                latitudeMin = Math.min(latitudeMin, i.getLatitude());
                latitudeMax = Math.max(latitudeMax, i.getLatitude());
                longitudeMin = Math.min(longitudeMin, i.getLongitude());
                longitudeMax = Math.max(longitudeMax, i.getLongitude());
            }
            squareLocation[0] = latitudeMin;
            squareLocation[ONE] = longitudeMin;
            squareLocation[TWO] = latitudeMax;
            squareLocation[THREE] = longitudeMax;
        } catch (TwitterException e) {
            System.err.println("Twitter API failed");
        }
        return squareLocation;
    }

    public static void stream(Query query, Twitter twitter) throws TwitterException, IOException {
        twitter4j.TwitterStream currentStream = new TwitterStreamFactory().getInstance();

        StatusListener listener = new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                printStream(status);
            }
        };
        FilterQuery fQuery = new FilterQuery();
        System.out.println(query.getQuery());
        String[] que = {query.getQuery()};
        fQuery.track(que);
        fQuery.locations(streamLocation(twitter));
        currentStream.addListener(listener);
        currentStream.filter(fQuery);

    }

    public static double[][] streamLocation(Twitter twitter) {
        double[][] location = new double[2][2];
        if (parameters.place != "nearby") {
            location[0][0] = getSquareByName(twitter)[0];
            location[0][1] = getSquareByName(twitter)[1];
            location[1][0] = getSquareByName(twitter)[TWO];
            location[1][1] = getSquareByName(twitter)[THREE];
        } else {
            double[][] result = new double[TWO][TWO];
            location[0][0] = locationNearBy()[0] - ONE;
            location[0][1] = locationNearBy()[ONE] - ONE;
            location[1][0] = locationNearBy()[0] + ONE;
            location[1][1] = locationNearBy()[ONE] + ONE;
        }
        return location;
    }
    public static Query createcommonQuery(Twitter twitter) {

        Query currentQuery = new Query(parameters.query);

        if (parameters.place != "nearby") {
            double[] latlong = new double[]{};
            latlong = getSquareByName(twitter);
            double latitude = (latlong[ONE] + latlong[THREE]) / TWO;
            double longitude = (latlong[0] + latlong[TWO]) / TWO;
            GeoLocation location = new GeoLocation(longitude, latitude);
            double radius = Math.hypot(latlong[TWO] - latlong[0], latlong[THREE] - latlong[1]) * MILES / 2;
            currentQuery.setGeoCode(location, radius, Query.MILES);
        } else {
            GeoLocation curloc = new GeoLocation(locationNearBy()[0], locationNearBy()[1]);
            currentQuery.setGeoCode(curloc, RADIUS, Query.MILES);
        }

        if (parameters.limit != INF) {
            currentQuery.setCount(parameters.limit);
        }

        return currentQuery;
    }

    public static void main(String[] args) {

        Twitter myTwitter = new TwitterFactory().getInstance();

        parameters = new JCommanderParameters();
        JCommander jc = new JCommander();
        try {
            jc = new JCommander(parameters, args);
        } catch (ParameterException e) {
            System.err.println("Wrong arguments! Use -h for help.");
            return;
        }

        if (parameters.help) {
            jc.setProgramName("TwitterStream");
            jc.usage();
            return;
        }
        Query resultQuery = createcommonQuery(myTwitter);

        try {
            if (parameters.stream) {
                stream(resultQuery, myTwitter);
            } else {
                findTweets(resultQuery, myTwitter);
            }
        } catch (Exception e) {
            System.out.println("TWITTER FAILED");
            return;
        }
    }
}

