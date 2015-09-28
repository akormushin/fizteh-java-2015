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

import static ru.fizteh.fivt.students.andrewgark.Files.getFile;
import static ru.fizteh.fivt.students.andrewgark.GeolocationTwitterStream.getLocation;

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
        String[] keywords;
        if (jcts.getKeywords().size() > 0) {
            keywords = jcts.getKeywords().toArray(new String[jcts.getKeywords().size()]);
        } else {
            keywords = new String[1];
            keywords[0] = "";
        }
        if (jcts.isStream()) {
            if (jcts.getLimit() < Integer.MAX_VALUE) {
                System.out.println("You can't have stream with limit.");
                System.exit(-1);
            }
        }
        printTweets(String.join(" ", keywords), jcts.getLocation(), jcts.getLimit(), jcts.isHideRetweets(), jcts.isStream());
    }

    public static void printTweets(String keywords, String location, Integer limit, Boolean hideRetweets, Boolean isStream) {
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
                    if (!(hideRetweets && tweet.isRetweet())) {
                        printTweet(tweet, isStream);
                        numberTweets++;
                    }
                }
            } while ((isStream || numberTweets < needTweets) && (query = result.nextQuery()) != null);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
        if (numberTweets == 0) {
            System.out.println("Твитов по заданному запросу не найдено.");
        }
        System.exit(0);
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
            time = getTime(tweet) + " ";
        }
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
                Integer numberMinutes = Integer.valueOf(Long.toString(tweetDateTime.until(now, ChronoUnit.MINUTES)));
                return getForm(numberMinutes, new String[]{"минута", "минуты", "минут"}) + " назад";
            }
            Integer numberHours = Integer.valueOf(Long.toString(tweetDateTime.until(now, ChronoUnit.HOURS)));
            return getForm(numberHours, new String[]{"час", "часа", "часов"}) + " назад";
        } else {
            LocalDate now = LocalDate.now();
            if (now.minusDays(1).equals(tweetDate)) {
                return "Вчера";
            }
            Integer numberDays = Integer.valueOf(Long.toString(tweetDate.until(now, ChronoUnit.DAYS)));
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
        if ((n % 10 == 1) && (n % 100 != 11)) {
            return Integer.toString(n) + " "  + forms[0];
        } else if ((n % 10 >= 2) && (n %10 <= 4)  && (n % 100 < 10 || n % 100 >= 20)) {
            return Integer.toString(n) + " "  + forms[1];
        } else {
            return Integer.toString(n) + " "  + forms[2];
        }
    }
}

