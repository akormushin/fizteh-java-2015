package ru.fizteh.fivt.students.okalitova.twitterstreamer;

import com.beust.jcommander.JCommander;
import twitter4j.*;
import java.util.Date;
import java.util.List;
import static java.lang.Thread.sleep;


public class TwitterStreamer {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE =  "\u001b[35m";
    public static final long MILSEC_IN_MIN = 1000 * 60;
    public static final long MILSEC_IN_HOUR = 1000 * 60 * 60;
    public static final long MILSEC_IN_DAY = 1000 * 60 * 60 * 24;
    public static final short MINUTS_ID = 0;
    public static final short HOURS_ID = 1;
    public static final short DAYS_ID = 2;
    public static final int FIVE = 5;
    public static final int ELEVEN = 11;
    public static final int TWELVE = 12;
    public static final int MOD10 = 10;
    public static final int PAUSE = 1000;
    public static final int RADIUS = 1;

    public static String timeUnits(long time, short unit) {
        if (time % MOD10 == 1 && time != ELEVEN) {
            if (unit == MINUTS_ID) {
                return "минутку";
            } else {
                if (unit == HOURS_ID) {
                    return "часик";
                } else {
                    return "денек";
                }
            }
        } else {
            if (time % MOD10 > 1 && time % MOD10 < FIVE
                    && time != TWELVE) {
                if (unit == MINUTS_ID) {
                    return "минутки";
                } else {
                    if (unit == HOURS_ID) {
                        return "часика";
                    } else {
                        return "денька";
                    }
                }
            } else {
                if (unit == MINUTS_ID) {
                    return "минуток";
                } else {
                    if (unit == HOURS_ID) {
                        return "часиков";
                    } else {
                        return "деньков";
                    }
                }
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
                System.out.print(minBetween + " " + timeUnits(minBetween, MINUTS_ID)
                        + " назад");
            } else {
                if (today(time, currentTime)) {
                    System.out.print(hoursBetween + " " + timeUnits(hoursBetween, HOURS_ID)
                            + " назад");
                } else {
                    if (yesterday(time, currentTime)) {
                        System.out.print("вчера");
                    } else {
                        System.out.print(daysBetween + " " + timeUnits(daysBetween, DAYS_ID)
                                + " назад");
                    }
                }
            }
        }
        System.out.print("]");
    }

    public static Query setQuery(Parameters param) throws Exception {
        //set query
        Query query = new Query(param.getQuery());
        //set place
        if (!param.getPlace().equals("")) {
            if (!param.getPlace().equals("nearby")) {
                GoogleFindPlace googleFindPlace;
                googleFindPlace = new GoogleFindPlace(param.getPlace());
                GeoLocation geoLocation;
                geoLocation = new GeoLocation(googleFindPlace.getLocation().lat,
                        googleFindPlace.getLocation().lng);
                query.setGeoCode(geoLocation,
                        googleFindPlace.getRadius(), Query.KILOMETERS);
            } else {
                GeoLocation geoLocation;
                geoLocation = new GeoLocation(Nearby.getLatLng().lat,
                        Nearby.getLatLng().lng);
                query.setGeoCode(geoLocation,
                        RADIUS, Query.KILOMETERS);
            }
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
        if (statusCount == 0) {
            System.out.println("Подходящих твитов нет");
        }
    }

    public static FilterQuery setFilter(Parameters param) throws Exception {
        //set query filter
        String[] trackArray = new String[1];
        trackArray[0] = param.getQuery();
        long[] followArray = new long[0];
        FilterQuery filter = new FilterQuery(0, followArray, trackArray);
        //set place filter
        if (!param.getPlace().equals("")) {
            if (!param.getPlace().equals("nearby")) {
                GoogleFindPlace googleFindPlace;
                googleFindPlace = new GoogleFindPlace(param.getPlace());
                double[][] bounds = {{googleFindPlace.getBounds().southwest.lng,
                        googleFindPlace.getBounds().southwest.lat},
                        {googleFindPlace.getBounds().northeast.lng,
                                googleFindPlace.getBounds().northeast.lat}};
                filter.locations(bounds);
            } else {
                double[][] bounds = {{Nearby.getBounds().southwest.lng,
                        Nearby.getBounds().southwest.lat},
                        {Nearby.getBounds().northeast.lng,
                                Nearby.getBounds().northeast.lat}};
                filter.locations(bounds);
            }
        }
        return filter;
    }
    public static void stream(Parameters param,
                              StatusListener listener) throws Exception {
        TwitterStream twitterStream;
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);
        if (param.getQuery() == "" && param.getPlace() == "") {
            twitterStream.sample();
        } else {
            FilterQuery filter = setFilter(param);
            twitterStream.filter(filter);
        }
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
        JCommander cmd = new JCommander(param, args);
        if (param.isHelp()) {
            cmd.usage();
            System.exit(0);
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
                + "~CUTE twitterstreamer CUTE~"
                + ANSI_RESET);
    }

    public static void main(String[] args) throws Exception {

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
                    try {
                        sleep(PAUSE);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
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
            try {
                stream(param, listener);
            } catch (TwitterException e) {
                System.out.println(e.getMessage());
                stream(param, listener);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }
        } else {
            try {
                search(param);
            } catch (TwitterException e) {
                System.out.println(e.getMessage());
                search(param);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }
        }
    }
}
