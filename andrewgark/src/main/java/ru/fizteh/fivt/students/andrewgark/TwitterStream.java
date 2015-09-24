package ru.fizteh.fivt.students.andrewgark;

import com.beust.jcommander.JCommander;
import twitter4j.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitterStream {
    private static final Double RADIUS = 30.0;
    private static final Integer NUMBER_TWEETS = 100;
    public static void main(String[] args) {
        JCommanderTwitterStream jcts = new JCommanderTwitterStream();
        new JCommander(jcts, args);
        if (jcts.isHelp()) {
            System.out.println(getFile("src/main/resources/01-TwitterStream.md"));
            System.exit(0);
        }
        if (jcts.isStream()) {
            if (jcts.getLimit() < Integer.MAX_VALUE) {
                System.out.println("You can't have stream with limit.");
                System.exit(-1);
            }
            System.out.println("Stream isn't ready.");
            System.exit(-1);
        } else {
            String[] keywords;
            if (jcts.getKeywords().size() > 0) {
                keywords = jcts.getKeywords().toArray(new String[jcts.getKeywords().size()]);
            } else {
                keywords = new String[1];
                keywords[0] = "";
            }
            printSomeTweets(String.join(" ", keywords), jcts.getLocation(), jcts.getLimit(), jcts.isHideRetweets());
        }
    }

    public static void printSomeTweets(String keywords, String location, Integer limit, Boolean hideRetweets) {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            Query query = new Query(keywords);
            query.setGeoCode(getLocation(location), RADIUS, Query.KILOMETERS);
            query.count(Integer.min(NUMBER_TWEETS, limit));
            QueryResult result;
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet: tweets) {
                if (!(hideRetweets && tweet.isRetweet())) {
                    printTweet(tweet, false);
                }
            }
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }

    public static void printTweet(Status tweet, boolean isStream) {
        String time;
        if (isStream)
            time = "";
        else
            time = getTime(tweet);
        String uName = tweet.getUser().getScreenName();
        String text = tweet.getText();
        Integer retweets = tweet.getRetweetCount();
        if (tweet.isRetweet()) {
            Pattern myPattern = Pattern.compile("RT @([^ ]*): (.*)");
            Matcher m = myPattern.matcher(text);
            m.find();
            String uRTName = m.group(1);
            text = m.group(2);
            System.out.println(time + " @" + uName + ": ретвитнул @" + uRTName + ": " + text);
        } else {
            System.out.println(time + " @" + uName + ": " + text + retweetsForm(retweets));
        }
    }

    public static String getTime(Status tweet) {
        ZonedDateTime dateTime = tweet.getCreatedAt().toInstant().atZone(ZoneId.systemDefault());
        LocalDate tweetDate = dateTime.toLocalDate();
        if (tweetDate.equals(LocalDate.now())) {
            LocalDateTime tweetDateTime = dateTime.toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();
            if (now.minusMinutes(2).isBefore(tweetDateTime)) {
                return "Только что";
            }
            if (now.minusHours(1).isBefore(tweetDateTime)) {
                return Long.toString(tweetDateTime.until(now, ChronoUnit.MINUTES)) + " минут назад";
            }
            return Long.toString(tweetDateTime.until(now, ChronoUnit.HOURS)) + " часов назад";
        } else {
            LocalDate now = LocalDate.now();
            if (now.minusDays(1).equals(tweetDate)) {
                return "Вчера";
            }
            return Long.toString((tweetDate.until(now, ChronoUnit.DAYS))) + " дней назад";
        }
    }

    public static String retweetsForm(Integer retweets) {
        if (retweets > 0) {
            return "";
        }
        return " (" + Integer.toString(retweets) + " ретвитов)";
    }

    public static GeoLocation getLocation(String location) {
        if (location == "nearby") {
            String xml = getUrl("https://ipcim.com/en/?p=where");
            Pattern myPattern = Pattern.compile(".*LatLng\\(([0-9.]*), ([0-9.]*)\\);.*");
            Matcher m = myPattern.matcher(xml);
            if (!m.find()) {
                System.out.println("We can't find your IP.");
                System.exit(-1);
            }
            return new GeoLocation(Double.parseDouble(m.group(2)), Double.parseDouble(m.group(1)));
        } else {
            String urlQuery = "https://geocode-maps.yandex.ru/1.x/?geocode=";
            String yandexKey = getFile("src/main/resources/YandexMapsAPI.properties");
            String xml = getUrl(urlQuery + location + "&key=" + yandexKey);
            Pattern myPattern = Pattern.compile(".*<pos>([0-9.\\-]*) ([0-9.\\-]*)<\\/pos>.*");
            Matcher m = myPattern.matcher(xml);
            if (!m.find()) {
                System.out.println("We can't find this location.");
                System.exit(-1);
            }
            return new GeoLocation(Double.parseDouble(m.group(2)), Double.parseDouble(m.group(1)));
        }
    }

    public static String getFile(String fileName) {
        BufferedReader fin = null;
        try {
            fin = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s = "";
        try {
            String str;
            while ((str = fin.readLine()) != null) {
                s += str + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String getUrl(String url) {
        URL thisUrl = null;
        try {
            thisUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        URLConnection connection = null;
        try {
            connection = thisUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.addRequestProperty("Protocol", "Http/1.1");
        connection.addRequestProperty("Connection", "keep-alive");
        connection.addRequestProperty("Keep-Alive", "1000");
        connection.addRequestProperty("User-Agent", "Web-Agent");
        try {
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = null;
        try {
            do {
                text += in.readLine() + "\n";
            } while (in.ready());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}

