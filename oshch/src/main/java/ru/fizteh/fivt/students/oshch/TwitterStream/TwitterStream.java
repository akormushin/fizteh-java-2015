package ru.fizteh.fivt.students.oshch.TwitterStream;

        import com.beust.jcommander.JCommander;
        import twitter4j.*;
        import java.util.Date;
        import java.util.List;
        import com.google.maps.GeoApiContext;
        import com.google.maps.GeocodingApi;
        import com.google.maps.model.Bounds;
        import com.google.maps.model.GeocodingResult;
        import com.google.maps.model.LatLng;
        import com.beust.jcommander.Parameter;


public class TwitterStream {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GR =  "\u001b[32m";

    public static final long MILSEC_IN_MIN = 1000 * 60;
    public static final long MILSEC_IN_HOUR = 1000 * 60 * 60;
    public static final long MILSEC_IN_DAY = 1000 * 60 * 60 * 24;
    public static final int FIVE = 5;
    public static final int ELEVEN = 11;
    public static final int TWELVE = 12;
    public static final int MOD10 = 10;



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


    public static String minuts(long minuts) {
        if (minuts % MOD10 == 1 && minuts != ELEVEN) {
            return "минуту";
        } else {
            if (minuts % MOD10 > 1 && minuts % MOD10 < FIVE
                    && minuts != TWELVE) {
                return "минуты";
            } else {
                return "минут";
            }
        }
    }
    public static String hours(long hours) {
        if (hours % MOD10 == 1 && hours != ELEVEN) {
            return "час";
        } else {
            if (hours % MOD10 > 1 && hours % MOD10 < FIVE
                    && hours != TWELVE) {
                return "часа";
            } else {
                return "часов";
            }
        }
    }
    public static String days(long days) {
        if (days % MOD10 == 1 && days != ELEVEN) {
            return "день";
        } else {
            if (days % MOD10 > 1 && days % MOD10 < FIVE
                    && days != TWELVE) {
                return "дня";
            } else {
                return "дней";
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

        Query query = new Query(param.getQuery());

        if (param.getPlace() != "") {
            PlaceApi googleFindPlace;
            try {
                googleFindPlace = new PlaceApi(param.getPlace());
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
                    printStatus(status, param.isHideRt());
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

        String[] trackArray = new String[1];
        trackArray[0] = param.getQuery();
        long[] followArray = new long[0];
        FilterQuery filter = new FilterQuery(0, followArray, trackArray);

        if (param.getPlace() != "") {
            try {
                PlaceApi googleFindPlace;
                googleFindPlace = new PlaceApi(param.getPlace());
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

        return param;
    }


    public static void main(String[] args) {
        Parameters param = getParameters(args);

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
                            Thread.sleep(1000);
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
            stream(param, listener);
        } else {
            search(param);
        }
    }
}