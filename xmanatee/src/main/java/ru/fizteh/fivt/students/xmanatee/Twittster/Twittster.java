package ru.fizteh.fivt.students.xmanatee.Twittster;


import com.beust.jcommander.JCommander;
import twitter4j.*;
//import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;
import com.beust.jcommander.Parameter;

import java.util.List;

import static java.lang.System.exit;

public class Twittster {
    public static void main(String[] args) {
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
            exit(1);
        }

        List<Status> tweets = result.getTweets();
        for (Status tweet : tweets) {
            displayTweet(tweet);
        }
    }

    public static void runStreamer(Parameters parameters) {
        ConfigurationBuilder cb = getOAuthConfigurationBuilder();
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
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

    public static Query composeQuery(Parameters parameters) {
        Query query = new Query();
        query.setCount(parameters.limit);
        if (parameters.noRetweets) {
            parameters.query += " +exclude:retweets";
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

    public static void displayTweet(Status tweet) {
        System.out.print("[" + tweet.getCreatedAt() + "] ");
        System.out.print(ANSI_BLUE + "@" + tweet.getUser().getScreenName() + ANSI_RESET
                + ": " + tweet.getText());
        Integer retweeted = tweet.getRetweetCount();
        if (retweeted > 0) {
            System.out.print(ANSI_GREEN + " (" + retweeted + " ретвитов)" + ANSI_RESET);
        }
        System.out.println(ANSI_YELLOW + "." + ANSI_RESET);
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

        String consumerKey = "Oe8eSwYAHhHIYV9Qrd5vxg1bt";
        String consumerSecret = "SfzUoQ7xijRjl3i25AjAF5GKhjWQCZNmGSvk4j5LVcSMTsRf7Q";
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
        String consumerKey = "lw3SBY42kUCsirCzkqJmPzoxi";
        String consumerSecret = "r9LbRQzxEIsd2DF58okAnf0IQ0JCu2KkPr5gTtldJFVqsJVhZr";
        String accessToken = "206791653-7OnLR50ao89oSa7ITWC2mZjygOWjTVU6KRlhDGnv";
        String accessSecret = "gqIiXPLtblfTiqt0YXyKVpWqrGqN3Q1PbJeoQCsgEHrRU";
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessSecret);

        return cb;
    }
}
