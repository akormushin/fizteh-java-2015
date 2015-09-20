package ru.fizteh.fivt.students.thefacetakt.TwitterStream;

/**
 * Created by thefacetakt on 17.09.15.
 */

import com.beust.jcommander.*;
import twitter4j.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class TwitterStream {

    static final int MINUSES_COUNT = 140;
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_BLUE = "\u001B[34m";
    static final String LOCATION_DEFINITION_ERROR
            = "Problem while location definition";

    static final String DEFAULT_LOCATION = "nearby";

    static final double radius = 5;
    static final String radiusUnit = "km";

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
        private boolean hideRetweers = false;

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

        public boolean isHideRetweers() {
            return hideRetweers;
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
        final int MAX_NUMBER_OF_TRIES = 20;
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
        final int MAX_NUMBER_OF_TRIES = 20;
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

    public static void main(String[] args) {
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

        Location currentLocation = resolveLocation(jCommanderSettings.getLocation());

        try {
            Twitter twitter = TwitterFactory.getSingleton();

            String queryString = String.join(" ",
                    jCommanderSettings.getQueries());
            Query query = new Query(queryString).geoCode(new GeoLocation(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude()), radius, radiusUnit);

            System.out.println("Твиты по запросу "
                    + queryString + " для " + currentLocation.getName());

            QueryResult result = twitter.search(query);
            printSeparator();
            for (Status status : result.getTweets()) {

                if (status.isRetweet()) {
                    System.out.print("RETWEET");
                }
                System.out.print("[" + status.getCreatedAt() + "]"
                        + "@" + ANSI_BLUE  + status.getUser().getScreenName()
                        + ": " + ANSI_RESET + status.getText());
                if (status.getRetweetCount() != 0) {
                    System.out.print(" ("
                            + status.getRetweetCount() + " "
                            + retweetDeclension(status.getRetweetCount())
                            + ")");
                }
                System.out.println();
                printSeparator();
            }

        } catch (TwitterException te) {
            //te.printStackTrace();
            System.err.println("Failed to get tweets: "
                    + te.getMessage() + " " + te.getStatusCode());
            System.exit(-1);
        }
    }

}
