package ru.fizteh.fivt.students.w4r10ck1337.twitterstream;

import com.beust.jcommander.JCommander;
import twitter4j.*;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.beust.jcommander.Parameter;


public class TwitterStream {


    private static final double DEFAULT_RADIUS = 15;
    private static final double MILES = 34.5;
    private static final int SECOND = 1000;
    private static final int MINUTE = SECOND * 60;
    private static final int HOUR = MINUTE * 60;
    private static final int DAY = HOUR * 24;
    private static final int INF = 1000000000;
    private static final int BUF_SIZE = 1000;
    private static final int Q_LIMIT = 100;
    private static final int FUCKING_27 = 27;
    private static final int FUCKING_100 = 100;
    private static final int FUCKING_19 = 19;
    private static final int FUCKING_4 = 4;
    private static final int FUCKING_3 = 3;
    private static final int FUCKING_11 = 11;
    private static final int FUCKING_10 = 10;
    private static Twitter twitter = null;

    private static class Parameters {
        @Parameter
        private List<String> parameters = new ArrayList<>();

        @Parameter(
                names = {"-q", "--query"},
                description = "Искать по запросу <query>")
        private String query = null;

        @Parameter(
                names = {"-p", "--place"},
                description = "Искать по региону, если значение равно 'nearby'"
                        + " или отсутствует, искать по ip")
        private String place = "nearby";

        @Parameter(
                names = {"-s", "--stream"},
                description = "Приложение непрерывно с задержкой в 1 секунду"
                        + " печатает твиты на экран")
        private boolean stream = false;

        @Parameter(
                names = "--hideRetweets",
                description = "Не показывать ретвиты")
        private boolean hideRetweets = false;

        @Parameter(
                names = {"-l", "--limit"},
                description = "Выводить не более <tweet> твитов (не в стриме)")
        private int limit = INF;


        @Parameter(
                names = {"-h", "--help"},
                description = "Показать помощь",
                help = true)
        private boolean help = false;
    }
    private static Parameters parameters;


    private static String blue(String text) {
        return text.replaceAll(
                "(@[\\w]+)",
                (char) FUCKING_27 + "[34m" + "$1" + (char) FUCKING_27 + "[0m");
    }

    private static String timeToString(long time, String[] variants) {
        if (time % FUCKING_100 >= FUCKING_11
                && time % FUCKING_100 <= FUCKING_19) {
            return time + " " + variants[0] + " назад";
        }
        if (time % FUCKING_10 == 1) {
            return time + " " + variants[1] + " назад";
        }
        if (time % FUCKING_10 >= 2 && time % FUCKING_10 <= FUCKING_4) {
            return time + " " + variants[2] + " назад";
        }
        return time + " " + variants[0] + " назад";
    }

    private static String niceTime(Date date) {
        if (System.currentTimeMillis() - date.getTime() < MINUTE * 2) {
            return "Только что";
        } else if (System.currentTimeMillis() - date.getTime() < HOUR) {
            return timeToString(
                    (System.currentTimeMillis() - date.getTime()) / MINUTE,
                    new String[]{"минут", "минуту", "минуты"});
        } else if (System.currentTimeMillis() - date.getTime() < DAY) {
            return timeToString(
                    (System.currentTimeMillis() - date.getTime()) / HOUR,
                    new String[]{"часов", "час", "часа"});
        } else if (System.currentTimeMillis() - date.getTime() < DAY * 2) {
            return "Вчера";
        } else {
            return timeToString(
                    (System.currentTimeMillis() - date.getTime()) / DAY,
                    new String[]{"дней", "день", "дня"});
        }
    }

    private static void printTweet(Status status) {
        if (status.isRetweet()) {
            if (parameters.hideRetweets) {
                return;
            }
            if (!parameters.stream) {
                System.out.print("[" + niceTime(status.getCreatedAt()) + "] ");
            }
            System.out.println(
                    blue("@" + status.getUser().getScreenName()
                             + " ретвитнул "
                             + status.getText().substring(FUCKING_3)));
        } else {
            if (!parameters.stream) {
                System.out.print("[" + niceTime(status.getCreatedAt()) + "] ");
            }
            System.out.print(
                    blue("@" + status.getUser().getScreenName() + ": "
                             + status.getText()));
            if (status.isRetweeted()) {
                System.out.print(
                        " (" + status.getRetweetCount() + " ретвитов)");
            }
            System.out.println();
        }
        System.out.println(
                "----------------------------------------------"
                + "------------------------------------------");
    }

    private static void parseArgs(String[] args) {
        parameters = new Parameters();
        JCommander jc = new JCommander(parameters, args);
        jc.setProgramName("TwitterStream");
        if (parameters.help) {
            jc.usage();
            return;
        }
    }

    private static double[] getLocation() {
        String location;
        if (parameters.place.equals("nearby")) {
            try {
                URL url = new URL("http://ipinfo.io/geo");
                InputStream is = url.openStream();
                byte[] b = new byte[BUF_SIZE + 1];
                char[] c = new char[is.read(b) + 2];
                for (int i = 0; b[i] > 0 && i < BUF_SIZE; i++) {
                    c[i] = (char) b[i];
                }
                location = String.valueOf(c).
                        split("\"loc\": \"")[1].split("\"")[0];
            } catch (Exception e) {
                System.err.println(
                        "Не получается определить местоположение,"
                        + " попробуйте использовать --place");
                return null;
            }
            return new double[]{
                    Double.parseDouble(location.split(",")[0]),
                    Double.parseDouble(location.split(",")[1]),
                    DEFAULT_RADIUS
            };
        }
        try {
            GeoQuery geoQuery = new GeoQuery((String) null);
            geoQuery.setQuery(parameters.place);
            List<Place> places = twitter.searchPlaces(geoQuery);
            if (places.size() == 0) {
                System.err.println("Нет такого места");
                System.exit(1);
            }
            double minlat = INF, maxlat = -INF, minlong = INF, maxlong = -INF;
            for (GeoLocation g : places.get(0).getBoundingBoxCoordinates()[0]) {
                minlat = Math.min(minlat, g.getLatitude());
                maxlat = Math.max(maxlat, g.getLatitude());
                minlong = Math.min(minlong, g.getLongitude());
                maxlong = Math.max(maxlong, g.getLongitude());
            }
            return new double[]{
                    (minlat + maxlat) / 2,
                    (minlong + maxlong) / 2,
                    Math.hypot(maxlat - minlat, maxlong - minlong) * MILES / 2
            };
        } catch (Exception e) {
            System.err.println("Нет соединения с твиттером");
        }
        return null;
    }

    private static Query createQuery() {
        Query searchQuery = new Query(parameters.query);
        searchQuery.setCount(Math.min(Q_LIMIT, parameters.limit));
        searchQuery.setQuery(parameters.query);
        double[] location = getLocation();
        while (location == null) {
            try {
                Thread.sleep(SECOND);
            } catch (Exception e) {
                System.err.println("Ошибка ожидания");
            }
            location = getLocation();
        }
        searchQuery.geoCode(
                new GeoLocation(location[0], location[1]), location[2], "mi");
        return searchQuery;
    }

    private static void printTweets() {
        Query searchQuery = createQuery();
        while (parameters.limit > 0) {
            searchQuery.setCount(Math.min(Q_LIMIT, parameters.limit));
            parameters.limit -= Q_LIMIT;
            List<Status> tweets = null;
            boolean success = false;
            while (!success) {
                try {
                    tweets = twitter.search(searchQuery).getTweets();
                    success = true;
                } catch (Exception e) {
                    System.err.println("Ошибка соединения");
                }
                try {
                    Thread.sleep(SECOND);
                } catch (Exception e) {
                    System.err.println("Ошибка ожидания");
                }
            }
            if (tweets.size() == 0) {
                System.out.println("Не найдены твиты по запросу");
                System.exit(0);
            }
            tweets.forEach(t -> printTweet(t));
            searchQuery.setMaxId(tweets.get(tweets.size() - 1).getId() - 1);
        }
    }

    private static void streamTweets() {
        Query searchQuery = createQuery();
        searchQuery.setCount(1);
        long prev = System.currentTimeMillis();
        List<Status> tweets = null;
        while (true) {
            boolean success = false;
            while (!success) {
                try {
                    tweets = twitter.search(searchQuery).getTweets();
                    success = tweets.size() > 0;
                } catch (Exception e) {
                    System.err.println("Ошибка соединения");
                }
                try {
                    Thread.sleep(SECOND);
                } catch (Exception e) {
                    System.err.println("Ошибка ожидания");
                }
            }
            try {
                Thread.sleep(System.currentTimeMillis() - prev);
            } catch (Exception e) {
                System.err.println("Ошибка ожидания");
            }
            printTweet(tweets.get(0));
            searchQuery.setSinceId(tweets.get(0).getId());
            prev = System.currentTimeMillis();
        }

    }

    public static void main(String[] args) {
        parseArgs(args);
        if (parameters.help) {
            return;
        }
        twitter = TwitterFactory.getSingleton();
        if (parameters.query != null) {
            System.out.println(
                    "Твиты по запросу " + parameters.query
                            + " для " + parameters.place + ":");
        } else {
            System.out.println("Твиты для " + parameters.place + ":");
        }
        System.out.println(
                "----------------------------------------------"
                        + "------------------------------------------");

        if (parameters.stream) {
            streamTweets();
        } else {
            printTweets();
        }
    }
}
