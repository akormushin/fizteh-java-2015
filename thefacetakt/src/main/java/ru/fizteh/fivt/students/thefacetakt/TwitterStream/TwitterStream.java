package ru.fizteh.fivt.students.thefacetakt.TwitterStream;

/**
 * Created by thefacetakt on 17.09.15.
 */

import com.beust.jcommander.*;
import twitter4j.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

class Location {
    private double latitude;
    private double longitude;
    private String name;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }


    Location(double newLatitude, double newLongitude) {
        this.latitude = newLatitude;
        this.longitude = newLongitude;
    }
}

class InvalidLocationException extends Exception {
    InvalidLocationException() {
    }

    InvalidLocationException(String message) {
        super(message);
    }
}

class QueryLimitException extends Exception {
    QueryLimitException() {
    }

    QueryLimitException(String message) {
        super(message);
    }
}

class PlaceLocationResolver {
    static final String LOCATION_DEFINITION_ERROR
            = "Problem while location definition";

    private static HashMap<String, Location> cache
            = new HashMap<String, Location>();
    private static String googleMapsKey;
    private static String yandexMapsKey;

    static void init() {
        try {
            BufferedReader mapsKeyFile = new BufferedReader(
                    new FileReader("src/main/resources/geo.properties")
            );
            googleMapsKey = mapsKeyFile.readLine();
            yandexMapsKey = mapsKeyFile.readLine();
            mapsKeyFile.close();
        } catch (IOException e) {
            System.err.println("Something went terribly wrong: no maps "
                    + "key found");
            System.exit(1);
        }
    }


    static Location resolvePlaceLocationGoogle(String nameOfLocation)
            throws InvalidLocationException, QueryLimitException {
        int numberOfTries = 0;

        do {
            URL googleMapsURL = null;
            try {
                try {
                    URI uri = new URI("https",
                            "maps.googleapis.com",
                            "/maps/api/geocode/json",
                            "address=" + nameOfLocation + "&key="
                                    + googleMapsKey,
                            null);
                    googleMapsURL = uri.toURL();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                System.err.println(e.getMessage());
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

                String status = locationInfo.getString("status");
                if (status.equals("OVER_QUERY_LIMIT")) {
                    throw new QueryLimitException("Google");
                }
                if (!status.equals("OK")) {
                    throw new InvalidLocationException("Unknown place");
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
        while (numberOfTries < TwitterStream.MAX_NUMBER_OF_TRIES);
        System.err.println(LOCATION_DEFINITION_ERROR);
        System.exit(1);
        return null;
    }

    static Location resolvePlaceLocationYandex(String nameOfLocation)
            throws InvalidLocationException {
        int numberOfTries = 0;

        do {
            URL yandexMapsURL = null;
            try {
                try {
                    URI uri = new URI("https",
                            "geocode-maps.yandex.ru",
                            "/1.x/",
                            "geocode=" + nameOfLocation
                                    + "&key=" + yandexMapsKey
                                    + "&format=json",
                            null);
                    yandexMapsURL = uri.toURL();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                System.err.println(e.getMessage());
                ++numberOfTries;
                continue;
            }

            try {
                assert yandexMapsURL != null;

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        yandexMapsURL.openStream()));

                String currentInfo;
                StringBuilder responseStrBuilder = new StringBuilder();
                while ((currentInfo = in.readLine()) != null) {
                    responseStrBuilder.append(currentInfo);
                }

                JSONObject locationInfo =
                        new JSONObject(responseStrBuilder.toString())
                                .getJSONObject("response")
                                .getJSONObject("GeoObjectCollection");

                if (Integer.parseInt(locationInfo
                        .getJSONObject("metaDataProperty")
                        .getJSONObject("GeocoderResponseMetaData")
                        .getString("found")) == 0) {
                    throw new InvalidLocationException("Unknown place");
                }

                locationInfo = locationInfo
                        .getJSONArray("featureMember")
                        .getJSONObject(0)
                        .getJSONObject("GeoObject")
                        .getJSONObject("Point");
                String[] coordinates =
                        locationInfo.getString("pos").split(" ");

                return new Location(
                        Double.parseDouble(coordinates[1]),
                        Double.parseDouble(coordinates[0]));
            } catch (IOException | JSONException e) {
                System.out.println(e.getMessage());
                ++numberOfTries;
            }
        }
        while (numberOfTries < TwitterStream.MAX_NUMBER_OF_TRIES);
        System.err.println(LOCATION_DEFINITION_ERROR);
        System.exit(1);
        return null;
    }

    static Location resolvePlaceLocation(String nameOfLocation)
            throws InvalidLocationException {
        nameOfLocation = nameOfLocation.trim();

        if (nameOfLocation.length() == 0) {
            throw new InvalidLocationException("empty address");
        }

        if (!cache.containsKey(nameOfLocation)) {

            try {
                Location result
                        = resolvePlaceLocationGoogle(nameOfLocation);
                cache.put(nameOfLocation, result);
            } catch (QueryLimitException e) {
                try {
                    Location result
                            = resolvePlaceLocationYandex(nameOfLocation);
                    cache.put(nameOfLocation, result);
                } catch (InvalidLocationException ie) {
                    cache.put(nameOfLocation, null);
                }
            } catch (InvalidLocationException e) {
                cache.put(nameOfLocation, null);
            }

        }

        if (cache.get(nameOfLocation) == null) {
            throw new InvalidLocationException("Unknown place");
        }
        return cache.get(nameOfLocation);
    }

    static Location resolveCurrentLocation() {
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

                String[] coordinates = locationInfo
                        .getString("loc").split(",");

                Location result = new Location(
                        Double.parseDouble(coordinates[0]),
                        Double.parseDouble(coordinates[1]));
                result.setName(locationInfo.getString("city"));
                return result;
            } catch (IOException | JSONException e) {
                ++numberOfTries;
            }
        }
        while (numberOfTries < TwitterStream.MAX_NUMBER_OF_TRIES);

        System.err.println(LOCATION_DEFINITION_ERROR);
        System.exit(1);
        return new Location(0, 0);
    }
}

class JCommanderSetting {

    static final String DEFAULT_LOCATION = "nearby";

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

class Declenser {
    static final int FIRST_IMPORTANT_MOD = 100;
    static final int SECOND_IMPORTANT_MOD = 10;

    static final int FIRST_CASE_LEFT_BORDER = 11;
    static final int FIRST_CASE_RIGHT_BORDER = 19;

    static final int SECOND_CASE = 1;

    static final int THIRD_CASE_LEFT_BORDER = 2;
    static final int THIRD_CASE_RIGHT_BORDER = 4;

    static String declensionScheme(long n, String[] cases) {
        n = n % FIRST_IMPORTANT_MOD;
        if (FIRST_CASE_LEFT_BORDER <= n
                && n <= FIRST_CASE_RIGHT_BORDER) {
            return cases[0];
        }

        n = n % SECOND_IMPORTANT_MOD;
        if (n == SECOND_CASE) {
            return cases[1];
        }
        if (THIRD_CASE_LEFT_BORDER <= n
                && n <= THIRD_CASE_RIGHT_BORDER) {
            return cases[2];
        }
        return cases[0];
    }

    static String retweetDeclension(long n) {
        String[] cases = {"ретвитов", "ретвит", "ретвита"};
        return declensionScheme(n, cases);
    }

    static String minutesDeclension(long n) {
        String[] cases = {"минут", "минуту", "минуты"};
        return declensionScheme(n, cases);
    }

    static String hoursDeclension(long n) {
        String[] cases = {"часов", "час", "часа"};
        return declensionScheme(n, cases);
    }

    static String daysDeclension(long n) {
        String[] cases = {"дней", "день", "дня"};
        return declensionScheme(n, cases);
    }
}


public class TwitterStream {

    static final int MINUSES_COUNT = 140;
    static final int MAX_NUMBER_OF_TRIES = 2;
    static final double RADIUS = 10;
    static final String RADIUS_UNIT = "km";



    static void printSeparator() {
        for (int i = 0; i < MINUSES_COUNT; ++i) {
            System.out.print("-");
        }
        System.out.println();
    }

    static Location resolveLocation(String passedLocation) {
        Location result = null;
        if (passedLocation == JCommanderSetting.DEFAULT_LOCATION) {
            result = PlaceLocationResolver.resolveCurrentLocation();
        } else {
            try {
                result = PlaceLocationResolver
                        .resolvePlaceLocation(passedLocation);
                result.setName(passedLocation);
            } catch (InvalidLocationException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        return result;
    }

    static class TimeFormatter {
        static final int MILLISECONDS_IN_SECOND = 1000;
        static final int MILLISECONDS_IN_MINUTE = 60 * MILLISECONDS_IN_SECOND;
        static final int MILLISECONDS_IN_HOUR = 60 * MILLISECONDS_IN_MINUTE;
        static final int MILLISECONDS_IN_DAY = 24 * MILLISECONDS_IN_HOUR;

        static String formatTime(long currentTime, long timeToFormat) {
            if (currentTime - timeToFormat < 2 * MILLISECONDS_IN_MINUTE) {
                return "Только что";
            }
            if (currentTime - timeToFormat < MILLISECONDS_IN_HOUR) {
                long n = ((currentTime - timeToFormat)
                        / MILLISECONDS_IN_MINUTE);
                return String.valueOf(n)
                        + " " + Declenser.minutesDeclension(n) + " назад";
            }

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyy");

            if (formatter.format(currentTime).equals(
                    formatter.format(timeToFormat))) {
                long n = (currentTime - timeToFormat) / MILLISECONDS_IN_HOUR;
                return String.valueOf(n) + " " + Declenser.hoursDeclension(n)
                        + " назад";
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
            long n = (currentTime - timeToFormat) / MILLISECONDS_IN_DAY;
            return String.valueOf(n) + " " + Declenser.daysDeclension(n)
                    + " назад";
        }
    }

    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_BLUE = "\u001B[34m";
    static void printNick(String nick) {

        System.out.print("@" + ANSI_BLUE + nick + ": " + ANSI_RESET);
    }

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
            //RT @nick:

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

                if (tweets.size() == 0) {
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
    static double sphereDistance(double phi1, double lambda1,
                                 double phi2, double lambda2) {

        double deltaPhi = phi2 - phi1;
        double deltaLambda = lambda2 - lambda1;

        return 2 * Math.asin(Math.sqrt(sqr(Math.sin(deltaPhi / 2))
                + Math.cos(phi1) * Math.cos(phi2)
                        * sqr(Math.sin(deltaLambda / 2))))
                * EARTH_RADIUS;
    }

    static void printTwitterStream(JCommanderSetting jCommanderSetting,
                                   Location currentLocation) {
        StatusListener listener = new StatusAdapter() {
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
                                    PlaceLocationResolver.resolvePlaceLocation(
                                            status.getUser().getLocation());
                        } catch (InvalidLocationException e) {
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
        };

        twitter4j.TwitterStream twitterStream =
                new TwitterStreamFactory().getInstance();

        twitterStream.addListener(listener);

        String[] trackArray = jCommanderSetting.getQueries().toArray(
                new String[jCommanderSetting.getQueries().size()]
        );
        long[] followArray = {};

        twitterStream.filter(new FilterQuery(0, followArray , trackArray));
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

    public static void main(String[] args) throws InterruptedException {
        PlaceLocationResolver.init();

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
