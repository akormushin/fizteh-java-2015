package ru.fizteh.fivt.students.oshch.twitterstream;

import com.beust.jcommander.JCommander;
import twitter4j.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.Bounds;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.beust.jcommander.Parameter;



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





    public static class Parameters {
        public static final int HUNDRED = 100;

        @Parameter(names = {"-q", "--query"},
                description = "поисковой запрос")
        private String query = "nearby";
        @Parameter(names = {"-p", "--place"},
                description = "место, рядом с которым искать")
        private String place = "";
        @Parameter(names = {"-s", "--stream"},
                description = "стрим твитов")
        private boolean stream = false;
        @Parameter(names = {"--hideRetweets"},
                description = "прятать ретвиты")
        private boolean hideRt = false;
        @Parameter(names = {"-h", "--help"},
                description = "показать помощь")
        private boolean help = false;
        @Parameter(names = {"-l", "--limit"},
                description = "максимальное количество твиттов (не в стриме)")
        private Integer limit = HUNDRED;

        public final String getQuery() {
            return query;
        }

        public boolean isStream() {
            return stream;
        }

        public boolean isHelp() {
            return help;
        }

        public final String getPlace() {
            return place;
        }

        public final Integer getLimit() {
            return limit;
        }

        public final boolean isHideRt() {
            return hideRt;
        }
    }

    public static class PlaceApi {
        private static final double R = 6371;
        private GeocodingResult[] result;
        private double radius;
        PlaceApi(String place) {
            GeoApiContext context = new GeoApiContext()
                    .setApiKey("AIzaSyCAhkvmjepUzQUh9pA7g0K4QoQY2ncBno8");
            try {
                result = GeocodingApi.geocode(context, place).await();
                radius = calculateRadius();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }
        }

        private double calculateRadius() {
            double phi1 = Math.toRadians(result[0].geometry.bounds.northeast.lat);
            double phi2 = Math.toRadians(result[0].geometry.bounds.southwest.lat);
            double dPhi = phi1 - phi2;
            double lambda1;
            lambda1 = Math.toRadians(result[0].geometry.bounds.northeast.lng);
            double lambda2;
            lambda2 = Math.toRadians(result[0].geometry.bounds.southwest.lng);
            double dLambda = lambda1 - lambda2;

            double a = Math.sin(dPhi / 2) * Math.sin(dPhi / 2)
                    + Math.cos(phi1) * Math.cos(phi2)
                    * Math.sin(dLambda / 2) * Math.sin(dLambda / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c;
            return distance / 2;
        }

        public final LatLng getLocation() {
            return result[0].geometry.location;
        }
        public final double getRadius() {
            return radius;
        }
        public final Bounds getBounds() {
            return result[0].geometry.bounds;
        }
    }

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

    public static Query setQuery(Parameters param) throws Exception {

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
                if (status.isRetweet() && param.isHideRt())
                    continue;
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

    public static FilterQuery setFilter(Parameters param) throws Exception{

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
    public static void stream(Parameters param,
                              StatusListener listener) throws Exception{
        twitter4j.TwitterStream twitterStream;
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);
        FilterQuery filter = setFilter(param);
        twitterStream.filter(filter);
    }

    public static void printStatus(Status status, boolean hideRetwitts) {
        if (status.isRetweet()) {
            if (!hideRetwitts) {
                System.out.println(ANSI_GR
                        + "@" + status.getUser().getScreenName()
                        + ANSI_RESET + ": ретвитнул " + ANSI_GR
                        + "@"
                        + status.getRetweetedStatus().getUser().getScreenName()
                        + ANSI_RESET + ": "
                        + status.getRetweetedStatus().getText());
            }
        } else {
            System.out.print(ANSI_GR
                    + "@" + status.getUser().getScreenName()
                    + ANSI_RESET + ": " + status.getText());
            if (status.getRetweetCount() != 0) {
                System.out.print("(" + status.getRetweetCount() + " ретвитов)");
            }
            System.out.println();
        }

    }

    public static Parameters getParameters(String[] args) throws Exception{
        Parameters param = new Parameters();
        JCommander cmd = new JCommander(param, args);
        if (param.isHelp()) {
            cmd.usage();
            System.exit(0);
        }
        return param;
    }


    public static void main(String[] args) throws Exception{
        Parameters param = getParameters(args);

        /*System.out.println(TimeUnit.DAYS.toMillis(1));
        System.out.println(MILSEC_IN_DAY);*/

        if (param.isStream()) {
            StatusListener listener = new StatusListener() {
                @Override
                public void onException(Exception e) {
                }

                @Override
                public void onStatus(Status status) {
                    printStatus(status, param.isHideRt());
                    if (!status.isRetweet() || !param.isHideRt())
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        }
                        catch (Exception e) {
                            System.err.println("Sleep error");
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
                }
                catch (TwitterException e) {
                    System.err.println("twitter error");
                }
                catch (Exception e) {
                    System.err.println("connection error");
                }


        } else {
            try {
                search(param);
            }
            catch (TwitterException e) {
                System.err.println("twitter error");
            }
            catch (Exception e) {
                System.err.println("connection error");
            }

        }
    }
}