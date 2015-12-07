package ru.fizteh.fivt.students.oshch.twitterstream;

import com.beust.jcommander.JCommander;
import twitter4j.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TwitterStream {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GR =  "\u001b[32m";

    public static final long MILSEC_IN_MIN = TimeUnit.MINUTES.toMillis(1);
    public static final long MILSEC_IN_HOUR = TimeUnit.HOURS.toMillis(1);
    public static final long MILSEC_IN_DAY = TimeUnit.DAYS.toMillis(1);
    public static final int FIVE = 5;
    public static final int ELEVEN = 11;
    public static final int TWELVE = 12;
    public static final int MOD10 = 10;
    public static final int MOD100 = 100;
    public static final String[] MINUTES = {"минуту", "минуты", "минут"};
    public static final String[] HOURS = {"час", "часа", "часов"};
    public static final String[] DAYS = {"день", "дня", "дней"};

    public static String myTime(long time, String[] timeType) {
        if (time % MOD10 == 1 && time % MOD100 != ELEVEN) {
            return timeType[0];
        } else {
            if (time % MOD10 > 1 && time % MOD10 < FIVE
                    && time % MOD100 != TWELVE) {
                return timeType[1];
            } else {
                return timeType[2];
            }
        }
    }

    public static boolean today(long time, long currentTime) {
        long pastDays = currentTime / MILSEC_IN_DAY;
        long todayTime = currentTime - pastDays * MILSEC_IN_DAY;
        return todayTime >= currentTime - time;
    }

    public static boolean yesterday(long time, long currentTime) {
        long currDays = currentTime / MILSEC_IN_DAY;
        long tweetDays = time / MILSEC_IN_DAY;
        return (currDays - tweetDays) == 1;
    }

    public static void printTime(Status status) {
        Date date = new Date();
        long currentTime = date.getTime();
        long time = status.getCreatedAt().getTime();
        long minBetween = (currentTime - time) / MILSEC_IN_MIN;
        long hoursBetween = (currentTime - time) / MILSEC_IN_HOUR;
        long daysBetween = currentTime / MILSEC_IN_DAY - time / MILSEC_IN_DAY;
        System.out.print("[");
        if (minBetween <= 2) {
            System.out.print("только что");
        } else {
            if (hoursBetween < 1) {
                System.out.print(minBetween + " " + myTime(minBetween, MINUTES)
                        + " назад");
            } else {
                if (today(time, currentTime)) {
                    System.out.print(hoursBetween + " " + myTime(hoursBetween, HOURS)
                            + " назад");
                } else {
                    if (yesterday(time, currentTime)) {
                        System.out.print("вчера");
                    } else {
                        System.out.print(daysBetween + " " + myTime(daysBetween, DAYS)
                                + " назад");
                    }
                }
            }
        }
        System.out.print("]");
    }

    public static Query setQuery(Parameters param) throws IOException {

        Query query = new Query(param.getQuery());

        if (!param.getPlace().isEmpty()) {
            PlaceApi googleFindPlace;
            googleFindPlace = new PlaceApi(param.getPlace());
            GeoLocation geoLocation;
            geoLocation = new GeoLocation(googleFindPlace.getLocation().lat,
                    googleFindPlace.getLocation().lng);
            query.setGeoCode(geoLocation,
                    googleFindPlace.getRadius(), Query.KILOMETERS);
        }
        return query;
    }

    public static void search(Parameters param) throws Exception {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = setQuery(param);
        QueryResult result;
        int limit = param.getLimit();
        int statusCount = 0;

        do {
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status status : tweets) {
                if (status.isRetweet() && param.isHideRt()) {
                    continue;
                }
                printTime(status);
                printStatus(status, param.isHideRt());
                statusCount++;
                limit--;
                if (limit == 0) {
                    break;
                }
            }
            query = result.nextQuery();
        } while (query != null && limit > 0);

        if (statusCount == 0) {
            System.out.println("Подходящих твитов нет");
        }
    }

    public static FilterQuery setFilter(Parameters param) throws Exception {

        String[] trackArray = new String[1];
        trackArray[0] = param.getQuery();
        long[] followArray = new long[0];
        FilterQuery filter = new FilterQuery(0, followArray, trackArray);

        if (!param.getPlace().isEmpty()) {

            PlaceApi googleFindPlace;
            googleFindPlace = new PlaceApi(param.getPlace());
            double[][] bounds = {{googleFindPlace.getBounds().southwest.lng,
                    googleFindPlace.getBounds().southwest.lat},
                    {googleFindPlace.getBounds().northeast.lng,
                            googleFindPlace.getBounds().northeast.lat}};
            filter.locations(bounds);
        }
        return filter;
    }
    public static void stream(Parameters param, StatusListener listener) throws Exception {
        twitter4j.TwitterStream twitterStream;
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);
        FilterQuery filter = setFilter(param);
        twitterStream.filter(filter);
    }

    public static void printStatus(Status status, boolean hideRetweets) {
        if (status.isRetweet()) {
            if (!hideRetweets) {
                System.out.println(ANSI_GR
                        + "@" + status.getUser().getScreenName()
                        + ANSI_RESET + ": ретвитнул " + ANSI_GR
                        + "@"
                        + status.getRetweetedStatus().getUser().getScreenName()
                        + ANSI_RESET + ": "
                        + status.getRetweetedStatus().getText());
                System.out.println("----------------------------------------"
                        +          "----------------------------------------");
            }
        } else {
            System.out.print(ANSI_GR
                    + "@" + status.getUser().getScreenName()
                    + ANSI_RESET + ": " + status.getText());
            if (status.getRetweetCount() != 0) {
                System.out.print("(" + status.getRetweetCount() + " ретвитов)");
            }
            System.out.println("\n----------------------------------------"
                    + "----------------------------------------");
        }

    }

    public static Parameters getParameters(String[] args) throws Exception {
        Parameters param = new Parameters();
        JCommander cmd = new JCommander(param, args);
        if (param.isHelp()) {
            cmd.usage();
        }
        return param;
    }


    public static void main(String[] args) throws Exception {
        Parameters param = getParameters(args);
        if (param.isHelp()) {
            System.exit(0);
        }
        if (param.isStream()) {
            StatusAdapter listener = new StatusAdapter() {
                @Override
                public void onException(Exception e) {
                    System.err.println("Stream error");
                }

                @Override
                public void onStatus(Status status) {
                    printStatus(status, param.isHideRt());
                    if (!status.isRetweet() || !param.isHideRt()) {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (Exception e) {
                            System.err.println("Sleep error");
                        }
                    }
                }
            };
            try {
                stream(param, listener);
            } catch (TwitterException e) {
                System.err.println("twitter error");
            } catch (IOException e) {
                System.err.println("cant find api key");
            } catch (Exception e) {
                System.err.println("connection error");
            }
        } else {
            try {
                search(param);
            } catch (TwitterException e) {
                System.err.println("twitter error");
            } catch (Exception e) {
                System.err.println("connection error");
            }

        }
    }
}
