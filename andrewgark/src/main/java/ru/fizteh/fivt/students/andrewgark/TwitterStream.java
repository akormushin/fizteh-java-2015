package ru.fizteh.fivt.students.andrewgark;

import com.beust.jcommander.JCommander;
import twitter4j.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.fizteh.fivt.students.andrewgark.GeolocationSearch.getCoordinatesByIp;
import static ru.fizteh.fivt.students.andrewgark.GeolocationSearch.getCoordinatesByQuery;

public class TwitterStream {
    private static final Double RADIUS = 5.0;
    private static final Integer NUMBER_TWEETS = 100;
    private static final Integer ONE = 1;
    private static final Integer TWO = 2;
    private static final Integer FOUR = 4;
    private static final Integer TEN = 10;
    private static final Integer ELEVEN = 11;
    private static final Integer TWENTY = 20;
    private static final Integer HUNDRED = 100;
    private static final String SEPARATOR =
            "----------------------------------------------------------------------------------------";

    public static void main(String[] args) {
        JCommanderTwitterStream jcts = new JCommanderTwitterStream();
        new JCommander(jcts, args);
        if (jcts.isHelp()) {
            JCommander jc = new JCommander(jcts, args);
            jc.usage();
            return;
        }
        String[] keywords;
        if (jcts.getKeywords().size() > 0) {
            keywords = jcts.getKeywords().toArray(new String[jcts.getKeywords().size()]);
        } else {
            keywords = new String[1];
            keywords[0] = "";
        }
        String stringKeywords = String.join(" ", keywords);
        if (jcts.isStream()) {
            if (jcts.getLimit() < Integer.MAX_VALUE) {
                System.err.println("You can't have stream with limit.");
                System.exit(-1);
            }
            streamTweets(stringKeywords, jcts.getLocation(), jcts.isHideRetweets());
        } else {
            printTweets(stringKeywords, jcts.getLocation(), jcts.getLimit(), jcts.isHideRetweets());
        }
        return;
    }

    public static void streamTweets(String keywords, String location, Boolean hideRetweets) {
        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusAdapter() {
            public void onStatus(Status status) {
                if (!(status.isRetweet() && hideRetweets)) {
                    printTweet(status, true);
                }
            }
        });
        Double[] coordinates = getCoordinatesByQuery(location);
        double[][] doubleCoordinates = {{coordinates[1] - RADIUS, coordinates[0] - RADIUS},
                {coordinates[1] + RADIUS, coordinates[0] + RADIUS}};
        //System.out.println(coordinates[0].toString() + " " + coordinates[1].toString());
        String[] arrayKeywords = {keywords};
        FilterQuery fq = new FilterQuery();
        fq.locations(doubleCoordinates);
        fq.track(arrayKeywords);
        twitterStream.filter(fq);
    }

    public static void printTweets(String keywords, String location, Integer limit, Boolean hideRetweets) {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(keywords);
        query.setGeoCode(getLocation(location), RADIUS, Query.KILOMETERS);
        Integer needTweets;
        Integer numberTweets = 0;
        if (limit == Integer.MAX_VALUE) {
            needTweets = NUMBER_TWEETS;
        } else {
            needTweets = limit;
        }
        QueryResult result;
        List<Status> tweets;

        try {
            do {
                result = twitter.search(query);
                tweets = result.getTweets();
                for (Status tweet : tweets) {
                    if (!(hideRetweets && tweet.isRetweet()) && (numberTweets < needTweets)) {
                        printTweet(tweet, false);
                        numberTweets++;
                    }
                }
                query = result.nextQuery();
            } while ((numberTweets < needTweets) && (query != null));
        } catch (TwitterException te) {
            te.printStackTrace();
            System.err.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
        if (numberTweets == 0) {
            System.out.println("Твитов по заданному запросу не найдено.");
        } else {
            System.out.println(SEPARATOR);
        }
        return;
    }

    public static GeoLocation getLocation(String location) {
        Double[] coordinates;
        if (location == "nearby") {
            coordinates = getCoordinatesByIp();
        } else {
            coordinates = getCoordinatesByQuery(location);
        }
        return new GeoLocation(coordinates[0], coordinates[1]);
    }

    public static void printTweet(Status tweet, boolean isStream) {
        String time;
        if (isStream) {
            time = "";
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        } else {
            time = getTimeForm(tweet) + " ";
        }
        System.out.println(SEPARATOR);
        String uName = tweet.getUser().getScreenName();
        String text = tweet.getText();
        Integer retweets = tweet.getRetweetCount();
        if (tweet.isRetweet()) {
            Pattern myPattern = Pattern.compile("RT @([^ ]*): (.*)");
            Matcher m = myPattern.matcher(text);
            m.find();
            String uRTName = m.group(1);
            text = m.group(2);
            System.out.println(time + "@" + uName + ": ретвитнул @" + uRTName + ": " + text);
        } else {
            System.out.println(time + "@" + uName + ": " + text + retweetsForm(retweets));
        }
    }

    public static String getTimeForm(Status tweet) {
        ZonedDateTime dateTime = tweet.getCreatedAt().toInstant().atZone(ZoneId.systemDefault());
        LocalDateTime tweetLocalDateTime = dateTime.toLocalDateTime();
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        return "[" + getTimeBetweenForm(tweetLocalDateTime, nowLocalDateTime) + "]";
    }

    public static String getTimeBetweenForm(LocalDateTime tweetLDT, LocalDateTime nowLDT) {
        LocalDate tweetLD = tweetLDT.toLocalDate();
        LocalDate nowLD = nowLDT.toLocalDate();

        if (tweetLD.equals(nowLD)) {
            if (nowLDT.minusMinutes(2).isBefore(tweetLDT)) {
                return "Только что";
            }
            if (nowLDT.minusHours(1).isBefore(tweetLDT)) {
                Integer numberMinutes = Integer.valueOf(Long.toString(tweetLDT.until(nowLDT, ChronoUnit.MINUTES)));
                return getForm(numberMinutes, new String[]{"минута", "минуты", "минут"}) + " назад";
            }
            Integer numberHours = Integer.valueOf(Long.toString(tweetLDT.until(nowLDT, ChronoUnit.HOURS)));
            return getForm(numberHours, new String[]{"час", "часа", "часов"}) + " назад";
        } else {
            if (nowLD.minusDays(1).equals(tweetLD)) {
                return "Вчера";
            }
            Integer numberDays = Integer.valueOf(Long.toString(tweetLD.until(nowLD, ChronoUnit.DAYS)));
            return getForm(numberDays, new String[]{"день", "дня", "дней"}) + " назад";
        }
    }

    public static String retweetsForm(Integer retweets) {
        if (retweets == 0) {
            return "";
        }
        return " (" + getForm(retweets, new String[]{"ретвит", "ретвита", "ретвитов"}) + ")";
    }

    public static String getForm(Integer n, String[] forms) {
        if ((n % TEN == ONE) && (n % HUNDRED != ELEVEN)) {
            return Integer.toString(n) + " "  + forms[0];
        } else if ((n % TEN >= TWO) && (n % TEN <= FOUR)  && (n % HUNDRED < TEN || n % HUNDRED >= TWENTY)) {
            return Integer.toString(n) + " "  + forms[1];
        } else {
            return Integer.toString(n) + " "  + forms[2];
        }
    }
}

