package ru.fizteh.fivt.students.thefacetakt.TwitterStream;

/**
 * Created by thefacetakt on 17.09.15.
 */

import com.beust.jcommander.*;
import twitter4j.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class TwitterStream {

    static final int MINUSES_COUNT = 140;
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_BLUE = "\u001B[34m";
    static final String LOCATION_DEFINITION_ERROR
            = "Problem while location definition";

    static final String DEFAULT_LOCATION = "nearby";
    static final int MAX_NUMBER_OF_TRIES = 20;
    static final double RADIUS = 10;
    static final String RADIUS_UNIT = "km";

    private static String googleMapsKey;

    static class JCommanderSetting {

        @Parameter(names = {"--query", "-q"},
                description = "query or keywords for stream",
                variableArity = true)
        private List<String> queries = new ArrayList<>();

        @Parameter(names = {"--place", "-p"}, description = "location")
        private String location = DEFAULT_LOCATION;

        @Parameter(names = {"--stream", "-s"},
                description = "is stream enabled")
        private boolean stream = false;

        @Parameter(names = {"--hideRetweets"}, description = "hide retweets")
        private boolean hideRetweets = false;

        @Parameter(names = {"--limit", "-l"}, description = "limit of tweets")
        private Integer limit = Integer.MAX_VALUE;

        @Parameter(names = {"--help", "-h"},
                description = "show help", help = true)
        private boolean help;

        public List<String> getQueries() {
            return queries;
        }
        public String getLocation() {
            return location;
        }

        public boolean isStream() {
            return stream;
        }

        public boolean isHideRetweets() {
            return hideRetweets;
        }

        public Integer getLimit() {
            return limit;
        }

        public boolean isHelp() {
            return help;
        }
    }

    static class Location {
        private double latitude;
        private double longitude;
        private String name;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        Location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    static void printSeparator() {
        for (int i = 0; i < MINUSES_COUNT; ++i) {
            System.out.print("-");
        }
        System.out.println();
    }

    static void init() {
        try {
            BufferedReader googleMapsKeyFile = new BufferedReader(
                    new FileReader("thefacetakt/src/googlegeo.properties")
            );
            googleMapsKey = googleMapsKeyFile.readLine();
            googleMapsKeyFile.close();
        } catch (IOException e) {
            System.err.println("Something went terribly wrong: no google maps "
                    + "key found");
            System.exit(1);
        }
    }

    static String retweetDeclension(int n) {
        n = n % 100;
        if (11 <= n && n <= 19) {
            return "ретвитов";
        }
        n = n % 10;
        if (n == 1) {
            return "ретвит";
        }
        if (2 <= n && n <= 4) {
            return "ретвита";
        }

        return "ретвитов";
    }

    static Location getCurrentLocation() {
        int numberOfTries = 0;

        do {
            URL whatIsMyCity;
            try {
                whatIsMyCity = new URL("http://ipinfo.io/json");
            } catch (MalformedURLException e) {
                ++numberOfTries;
                continue;
            }

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        whatIsMyCity.openStream()));

                String currentInfo;
                StringBuilder responseStrBuilder = new StringBuilder();
                while ((currentInfo = in.readLine()) != null) {
                    responseStrBuilder.append(currentInfo);
                }

                JSONObject locationInfo =
                        new JSONObject(responseStrBuilder.toString());

                String[] coordiantes = locationInfo
                                .getString("loc").split(",");

                Location result = new Location(
                        Double.parseDouble(coordiantes[0]),
                        Double.parseDouble(coordiantes[1]));
                result.setName(locationInfo.getString("city"));
                return result;
            } catch (IOException | JSONException e) {
                ++numberOfTries;
                continue;
            }
        }
        while (numberOfTries < MAX_NUMBER_OF_TRIES);
        System.err.println(LOCATION_DEFINITION_ERROR);
        System.exit(1);
        return new Location(0, 0);
    }

    static Location resolvePlaceLocation(String nameOfLocation) {
        int numberOfTries = 0;

        do {
            URL googleMapsURL;
            try {
                googleMapsURL = new URL("https://maps.googleapis.com/"
                        + "maps/api/geocode/json?address="
                        + nameOfLocation + "&key=" + googleMapsKey);
            } catch (MalformedURLException e) {
                System.out.println(e.getMessage());
                ++numberOfTries;
                continue;
            }

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        googleMapsURL.openStream()));

                String currentInfo;
                StringBuilder responseStrBuilder = new StringBuilder();
                while ((currentInfo = in.readLine()) != null) {
                    responseStrBuilder.append(currentInfo);
                }

                JSONObject locationInfo =
                        new JSONObject(responseStrBuilder.toString());

                if (!locationInfo.getString("status").equals("OK")) {
                    System.err.println("Unknown place");
                    System.exit(1);
                }

                locationInfo = locationInfo
                                .getJSONArray("results")
                                .getJSONObject(0)
                                .getJSONObject("geometry")
                                .getJSONObject("location");

                return new Location(
                        Double.parseDouble(locationInfo.getString("lat")),
                        Double.parseDouble(locationInfo.getString("lng")));
            } catch (IOException | JSONException e) {
                System.out.println(e.getMessage());
                ++numberOfTries;
                continue;
            }
        }
        while (numberOfTries < MAX_NUMBER_OF_TRIES);
        System.err.println(LOCATION_DEFINITION_ERROR);
        System.exit(1);
        return new Location(0, 0);
    }

    static Location resolveLocation(String passedLocation) {
        Location result;
        if (passedLocation == DEFAULT_LOCATION) {
            result = getCurrentLocation();
        } else {
            result = resolvePlaceLocation(passedLocation);
            result.setName(passedLocation);
        }
        return result;
    }

    static String formatTime(long currentTime, long timeToFormat) {

        final int MILLISECONDS_IN_SECOND = 1000;
        final int MILLISECONDS_IN_MINUTE = 60 * MILLISECONDS_IN_SECOND;
        final int MILLISECONDS_IN_HOUR = 60 * MILLISECONDS_IN_MINUTE;
        final int MILLISECONDS_IN_DAY = 24 * MILLISECONDS_IN_HOUR;

        if (currentTime - timeToFormat < 2 * MILLISECONDS_IN_MINUTE) {
            return "Только что";
        }
        if (currentTime - timeToFormat < MILLISECONDS_IN_HOUR) {
            return String.valueOf(
                    ((currentTime - timeToFormat) / MILLISECONDS_IN_MINUTE))
                    + " минут назад";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyy");

        if (formatter.format(currentTime).equals(
                formatter.format(timeToFormat))) {
            return String.valueOf(
                    (currentTime - timeToFormat) / MILLISECONDS_IN_HOUR
            ) + " часов назад";
        }

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(currentTime);
        Calendar givenCalendar = Calendar.getInstance();
        givenCalendar.setTimeInMillis(timeToFormat);

        currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
        if (formatter.format(currentCalendar.getTimeInMillis()).equals(
                formatter.format(givenCalendar.getTimeInMillis()))) {
            return "вчера";
        }
        return String.valueOf(
                (currentTime - timeToFormat) / MILLISECONDS_IN_DAY
        ) + " дней назад";
    }

    public static void main(String[] args) throws InterruptedException {
        init();

        JCommanderSetting jCommanderSettings = new JCommanderSetting();

        try {
            JCommander jCommander = new JCommander(jCommanderSettings, args);
            jCommander.setProgramName("TwitterStream");
            if (jCommanderSettings.isHelp()) {
                jCommander.usage();
                return;
            }
        } catch (ParameterException pe) {
            JCommander jCommander =
                    new JCommander(jCommanderSettings, new String[0]);
            jCommander.setProgramName("TwitterStream");
            jCommander.usage();
            return;
        }

        Location currentLocation = resolveLocation(
                jCommanderSettings.getLocation()
        );

        long currentMaxId = 0;

        String queryString = String.join(" ",
                jCommanderSettings.getQueries());

        System.out.println("Твиты по запросу "
                + queryString + " для "
                + currentLocation.getName());

        printSeparator();

        do {
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

                    query.setSinceId(currentMaxId);



                    QueryResult result = twitter.search(query);


                    long currentTime = System.currentTimeMillis();

                    List<Status> tweets = result.getTweets();
                    Collections.reverse(tweets);

                    if (tweets.size() == 0
                            && !jCommanderSettings.isStream()) {
                        System.out.println("Не найдено ни одного твита");
                        printSeparator();
                    }

                    for (Status status : tweets) {
                        currentMaxId = Long.max(currentMaxId, status.getId());
                        if (!status.isRetweet()
                                || !jCommanderSettings.isHideRetweets()) {

                            System.out.print("[" + formatTime(currentTime,
                                    status.getCreatedAt().getTime()) + "]");

                            System.out.print(" @" + ANSI_BLUE
                                    + status.getUser().getScreenName()
                                    + ": " + ANSI_RESET);

                            System.out.print(status.getText());

                            if (status.getRetweetCount() != 0) {
                                System.out.print(" ("
                                        + status.getRetweetCount() + " "
                                        + retweetDeclension(
                                        status.getRetweetCount()
                                )
                                        + ")");
                            }

                            System.out.println();
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

            Thread.sleep(1000);

        } while (jCommanderSettings.isStream());
    }

}
