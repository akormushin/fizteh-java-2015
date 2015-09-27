package ru.fizteh.fivt.students.okalitova.TwitterStreamer;

import com.beust.jcommander.JCommander;
import twitter4j.*;
import java.util.Date;
import java.util.List;


public class TwitterStreamer {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE =  "\u001b[35m";
    public static final long MILSEC_IN_MIN = 1000 * 60;
    public static final long MILSEC_IN_HOUR = 1000 * 60 * 60;
    public static final long MILSEC_IN_DAY = 1000 * 60 * 60 * 24;
    public static final int FIVE = 5;
    public static final int ELEVEN = 11;
    public static final int TWELVE = 12;
    public static final int MOD10 = 10;

    public static String minuts(long minuts) {
        if (minuts % MOD10 == 1 && minuts != ELEVEN) {
            return "минутку";
        } else {
            if (minuts % MOD10 > 1 && minuts % MOD10 < FIVE
                    && minuts != TWELVE) {
                return "минутки";
            } else {
                return "минуток";
            }
        }
    }
    public static String hours(long hours) {
        if (hours % MOD10 == 1 && hours != ELEVEN) {
            return "часик";
        } else {
            if (hours % MOD10 > 1 && hours % MOD10 < FIVE
                    && hours != TWELVE) {
                return "часика";
            } else {
                return "часиков";
            }
        }
    }
    public static String days(long days) {
        if (days % MOD10 == 1 && days != ELEVEN) {
            return "денек";
        } else {
            if (days % MOD10 > 1 && days % MOD10 < FIVE
                    && days != TWELVE) {
                return "денька";
            } else {
                return "деньков";
            }
        }
    }
    public static boolean today(long time, long currentTime) {
        long pastDays = currentTime / MILSEC_IN_DAY;
        long todayTime = currentTime - pastDays * MILSEC_IN_DAY;
        return todayTime >= currentTime - time;
    }
    public static boolean yesterday(long time, long currentTime) {
        long pastDays = currentTime / MILSEC_IN_DAY;
        long todayTime = currentTime - (pastDays - 1) * MILSEC_IN_DAY;
        return todayTime >= currentTime - time;
    }

    public static void printTime(Status status) {
        Date date = new Date();
        long currentTime = date.getTime();
        long time = status.getCreatedAt().getTime();
        long minBetween = (currentTime - time) / MILSEC_IN_MIN;
        long hoursBetween = (currentTime - time) / MILSEC_IN_HOUR;
        long daysBetween = (currentTime - time) / MILSEC_IN_DAY;
        System.out.print("[");
        if (minBetween <= 2) {
            System.out.print("только что");
        } else {
            if (hoursBetween < 1) {
                System.out.print(minBetween + " " + minuts(minBetween)
                        + " назад");
            } else {
                if (today(time, currentTime)) {
                    System.out.print(hoursBetween + " " + hours(hoursBetween)
                            + " назад");
                } else {
                    if (yesterday(time, currentTime)) {
                        System.out.print("вчера");
                    } else {
                        System.out.print(daysBetween + " " + days(daysBetween)
                                + " назад");
                    }
                }
            }
        }
        System.out.print("]");
    }

    public static Query setQuery(Parameters param) {
        //set query
        Query query = new Query(param.getQuery());
        //set place
        if (param.getPlace() != "") {
            GoogleFindPlace googleFindPlace;
            try {
                googleFindPlace = new GoogleFindPlace(param.getPlace());
                GeoLocation geoLocation;
                geoLocation = new GeoLocation(googleFindPlace.getLocation().lat,
                        googleFindPlace.getLocation().lng);
                query.setGeoCode(geoLocation,
                        googleFindPlace.getRadius(), Query.KILOMETERS);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }
        }
        return query;
    }
    public static void search(Parameters param) {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = setQuery(param);
        QueryResult result;
        int limit = param.getLimit();
        int statusCount = 0;
        try {
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status status : tweets) {
                    printTime(status);
                    printStatus(status, param.isHideRetwitts());
                    statusCount++;
                    limit--;
                    if (limit == 0) {
                        break;
                    }
                }
                query = result.nextQuery();
            } while (query != null && limit > 0);
        } catch (TwitterException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        if (statusCount == 0) {
            System.out.println("Подходящих твитов нет");
        }
    }

    public static FilterQuery setFilter(Parameters param) {
        //set query filter
        String[] trackArray = new String[1];
        trackArray[0] = param.getQuery();
        long[] followArray = new long[0];
        FilterQuery filter = new FilterQuery(0, followArray, trackArray);
        //set place filter
        if (param.getPlace() != "") {
            try {
                GoogleFindPlace googleFindPlace;
                googleFindPlace = new GoogleFindPlace(param.getPlace());
                double[][] bounds = {{googleFindPlace.getBounds().southwest.lng,
                        googleFindPlace.getBounds().southwest.lat},
                        {googleFindPlace.getBounds().northeast.lng,
                                googleFindPlace.getBounds().northeast.lat}};
                filter.locations(bounds);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }
        }
        return filter;
    }
    public static void stream(Parameters param,
                              StatusListener listener) {
        TwitterStream twitterStream;
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);
        FilterQuery filter = setFilter(param);
        twitterStream.filter(filter);
    }

    public static void printStatus(Status status, boolean hideRetwitts) {
        if (status.isRetweet()) {
            if (!hideRetwitts) {
                System.out.println(ANSI_PURPLE
                        + "@" + status.getUser().getScreenName()
                        + ANSI_RESET + ": ретвитнул " + ANSI_PURPLE
                        + "@"
                        + status.getRetweetedStatus().getUser().getScreenName()
                        + ANSI_RESET + ": "
                        + status.getRetweetedStatus().getText());
            }
        } else {
            System.out.print(ANSI_PURPLE
                    + "@" + status.getUser().getScreenName()
                    + ANSI_RESET + ": " + status.getText());
            if (status.getRetweetCount() != 0) {
                System.out.print("(" + status.getRetweetCount() + " ретвитов)");
            }
            System.out.println();
        }

    }

    public static Parameters getParameters(String[] args) {
        Parameters param = new Parameters();
        try {
            JCommander cmd = new JCommander(param, args);
            if (param.isHelp()) {
                cmd.usage();
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        System.out.println("query: " + param.getQuery());
        System.out.println("place: " + param.getPlace());
        System.out.println("stream: " + param.isStream());
        System.out.println("retwitts: " + param.isHideRetwitts());
        System.out.println("limit: " + param.getLimit());
        return param;
    }

    public static void printHello() {
        System.out.println(ANSI_PURPLE
                + "~CUTE TwitterStreamer CUTE~"
                + ANSI_RESET);
    }

    public static void main(String[] args) {

        printHello();

        Parameters param = getParameters(args);

        if (param.isStream()) {
            StatusListener listener = new StatusListener() {
                @Override
                public void onException(Exception e) {
                }

                @Override
                public void onStatus(Status status) {
                    printStatus(status, param.isHideRetwitts());
                }

                @Override
                public void onDeletionNotice(StatusDeletionNotice
                                                     statusDeletionNotice) {
                }

                @Override
                public void onTrackLimitationNotice(int i) {
                }

                @Override
                public void onScrubGeo(long l, long l1) {
                }

                @Override
                public void onStallWarning(StallWarning stallWarning) {
                }
            };
            stream(param, listener);
        } else {
            search(param);
        }
    }
}
