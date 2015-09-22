package ru.fizteh.fivt.students.xmanatee.Twittster;


import com.beust.jcommander.JCommander;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import com.google.maps.model.LatLng;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import com.beust.jcommander.Parameter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


class Word4declension {                                        //секунда или секунду
    private static final int FIRST_BOUND = 2; //секунды
    private static final int SECOND_BOUND = 5; //секунд
    private static final int LEFT_BOUND = 11;
    private static final int RIGHT_BOUND = 20;
    private static final int GREAT_MODULO = 100;
    private static final int TINY_MODULO = 10;

    private String[] cases;

    Word4declension(String... inCases) {
        cases = inCases;
    }
    public String getCase(int caseNumber) {
        return cases[caseNumber];
    }

    public String declension4Number(long number) {
        number = number % GREAT_MODULO;
        if (LEFT_BOUND < number && number < RIGHT_BOUND) {
            return cases[2];
        } else {
            number = number % TINY_MODULO;
            if (number < FIRST_BOUND) {
                return cases[0];
            } else if (number < SECOND_BOUND) {
                return cases[1];
            } else {
                return cases[2];
            }
        }
    }
}

public class Twittster {
    public static void main(String[] args) {
        System.out.println(ANSI_YELLOW + "YOU'RE RUNNING TWITTSTER" + ANSI_RESET);
        Parameters parameters = parseArgs(args);

        if (parameters.help) {
            runHelp();
            System.exit(0);
        }
        if (parameters.ifstream) {
            runStreamer(parameters);
        } else {
            runSearch(parameters);
        }
    }

    private static void runHelp() {
        JCommander cmd = new JCommander(new Parameters());
        cmd.usage();
    }

    public static void runSearch(Parameters parameters) {
        ConfigurationBuilder cb = getOAuthConfigurationBuilder();
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        Query query = composeQuery(parameters);
        QueryResult result = null;

        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
            System.exit(1);
        }

        List<Status> tweets = result.getTweets();
        if (tweets.size() == 0) {
            System.out.println("No results");
        } else {
            for (Status tweet : tweets) {
                displayTweet(tweet);
            }
        }
    }

    public static void runStreamer(Parameters parameters) {
        ConfigurationBuilder cb = getOAuthConfigurationBuilder();
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                displayTweet(status);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
//                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
//                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
//                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
//                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
        //twitterStream.sample();
        twitterStream.filter(parameters.query);
    }

    static final double RADIUS_SCALING = 2;
    public static Query composeQuery(Parameters parameters) {
        Query query = new Query();
        query.setCount(parameters.limit);
        if (parameters.noRetweets) {
            parameters.query += " +exclude:retweets";
        }
        if (!parameters.place.isEmpty()) {
            GoogleFindPlace gfm = null;
            try {
                gfm = new GoogleFindPlace(parameters.place);
            } catch (Exception e) {
                System.out.println("Problems finding location encountered...");
                e.printStackTrace();
            }
            assert gfm != null;
            GeoLocation gl = new GeoLocation(gfm.getLocation().lat, gfm.getLocation().lng);
            query.setGeoCode(gl, gfm.getRadius() * RADIUS_SCALING, Query.Unit.km);
        }
        query.setQuery(parameters.query);
        return query;
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final Integer RT_PREFIX_LENGTH = 4;

    public static void displayTweet(Status tweet) {
        String formattedTweet = "";
        String tweetText = tweet.getText();
        formattedTweet += "[" + timeParser(tweet.getCreatedAt()) + "] ";
        formattedTweet += ANSI_BLUE + "@" + tweet.getUser().getScreenName() + ANSI_RESET + ": ";
        if (tweet.isRetweet()) {
            formattedTweet += "ретвитнул ";
            tweetText = tweetText.substring(RT_PREFIX_LENGTH);
        }
        formattedTweet += tweetText;

        Integer retweeted = tweet.getRetweetCount();
        if (retweeted > 0) {
            Word4declension retweetWord = new Word4declension("ретвит", "ретвита", "ретвитов");
            formattedTweet += ANSI_GREEN + " (" + retweeted + " "
                    + retweetWord.declension4Number(retweeted) + ")" + ANSI_RESET;
        }
        formattedTweet += ANSI_YELLOW + "." + ANSI_RESET;
        System.out.println(formattedTweet);
    }

    public static String timeParser(java.util.Date date) {

        Word4declension secondsw = new Word4declension("секунда", "секунды", "секунд");
        Word4declension minuteWord = new Word4declension("минута", "минуты", "минут");
        Word4declension hourWord = new Word4declension("час", "часа", "часов");
        Word4declension dayWord = new Word4declension("день", "дня", "дней");

        String formattedTime = new String();
        long currentTime = System.currentTimeMillis();
        long timeToFormat = date.getTime();
        long milliseconds = currentTime - timeToFormat;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);

        if (minutes < 2) {
            formattedTime = "только что";
        } else if (hours == 0) {
            formattedTime = minutes + " " + minuteWord.declension4Number(minutes) + " назад";
        } else if (days == 0) {
            formattedTime = hours + " " + hourWord.declension4Number(hours) + " назад";
        } else if (days == 1) {
            formattedTime = "вчера";
        } else {
            formattedTime = days + " " + dayWord.declension4Number(days) + " назад";
        }

        return formattedTime;
    }

    public static class Parameters {

        @Parameter(names = { "-q", "--query" }, description = "Query or keywords for streaming", required = true)
        private String query = "";

        @Parameter(names = { "-p", "--place" }, description = "Location or 'nearby'")
        private String place = "";

        @Parameter(names = { "-s", "--stream"}, description = "Streaming mode")
        private Boolean ifstream = false;

        @Parameter(names = { "-l", "--limit"}, description = "Number of tweets to show(only for no streaming mode)")
        private Integer limit = Integer.MAX_VALUE;

        @Parameter(names = "--hideRetweets", description = "Hiding Retweets")
        private boolean noRetweets = false;

        @Parameter(names = { "-h", "--help"}, description = "Help mode", help = true)
        private boolean help = false;
    }

    private static Parameters parseArgs(String[] args) {
        Parameters params = new Parameters();
        new JCommander(params, args);
        return params;
    }

    /*
    public static OAuth2Token getOAuth2Token(String key, String sec) {

        OAuth2Token token = null;
        ConfigurationBuilder cb;

        cb = new ConfigurationBuilder();
        cb.setApplicationOnlyAuthEnabled(true);

        cb.setOAuthConsumerKey(key).setOAuthConsumerSecret(sec);

        try {
            token = new TwitterFactory(cb.build()).getInstance().getOAuth2Token();
        } catch (Exception e) {
            System.out.println("Could not get OAuth2 token");
            e.printStackTrace();
            System.exit(0);
        }

        return token;
    }

    public static ConfigurationBuilder getOAuthConfigurationBuilder() {

        String consumerKey = "";
        String consumerSecret = "";
        OAuth2Token token;
        token = getOAuth2Token(consumerKey, consumerSecret);
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setApplicationOnlyAuthEnabled(true);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuth2TokenType(token.getTokenType());
        cb.setOAuth2AccessToken(token.getAccessToken());

        return cb;
    }
*/
    public static ConfigurationBuilder getOAuthConfigurationBuilder() {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("mykeys.properties");
            prop.load(input);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        String consumerKey = prop.getProperty("consumerKey");
        String consumerSecret = prop.getProperty("consumerSecret");
        String accessToken = prop.getProperty("accessToken");
        String accessTokenSecret = prop.getProperty("accessTokenSecret");

        cb.setDebugEnabled(false);
        cb.setPrettyDebugEnabled(false);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessTokenSecret);

        return cb;
    }
}

class GoogleFindPlace {
    private static final double R = 6371;
    private GeocodingResult[] result;
    private double radius;
    GoogleFindPlace(String place) throws Exception {
        GeoApiContext context = new GeoApiContext()
                .setApiKey("AIzaSyD0YkaGrWTTtyuXMSYr7_iZ1oPDO4p37Ac");
        result = GeocodingApi.geocode(context, place).await();
        radius = calculateRadius();
    }

    public LatLng getLocation() {
        return result[0].geometry.location;
    }
    public double getRadius() {
        return radius;
    }
    private double calculateRadius() {
        // Heavy formulas for distance from Wiki
        double phi1 = Math.toRadians(result[0].geometry.bounds.northeast.lat);
        double phi2 = Math.toRadians(result[0].geometry.bounds.southwest.lat);
        double dPhi = phi1 - phi2;
        double lambda1 = Math.toRadians(result[0].geometry.bounds.northeast.lng);
        double lambda2 = Math.toRadians(result[0].geometry.bounds.southwest.lng);
        double dLambda = lambda1 - lambda2;

        double a = Math.sin(dPhi / 2) * Math.sin(dPhi / 2)
                + Math.cos(phi1) * Math.cos(phi2)
                * Math.sin(dLambda / 2) * Math.sin(dLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance / 2;
    }
}
