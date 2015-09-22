package ru.fizteh.fivt.students.w4r10ck1337.TwitterStream;

import twitter4j.*;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Date;

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
    private static String place = "nearby";
    private static String query = null;
    private static Twitter twitter = null;
    private static boolean stream = false;
    private static boolean hideRetweets = false;
    private static int limit = INF;
    private static boolean help = false;

    private static String blue(String text) {
        return text.replaceAll(
                "(@[\\w]+)",
                (char) FUCKING_27 + "[34m" + "$1" + (char) FUCKING_27 + "[0m");
    }

    private static String niceMinutes(long minutes) {
        if (minutes % FUCKING_100 >= FUCKING_11
            && minutes % FUCKING_100 <= FUCKING_19) {
            return minutes + " минут назад";
        }
        if (minutes % FUCKING_10 == 1) {
            return minutes + " минуту назад";
        }
        if (minutes % FUCKING_10 >= 2 && minutes % FUCKING_10 <= FUCKING_4) {
            return minutes + " минуты назад";
        }
        return minutes + " минут назад";
    }

    private static String niceHours(long hours) {
        if (hours % FUCKING_100 >= FUCKING_11
            && hours % FUCKING_100 <= FUCKING_19) {
            return hours + " часов назад";
        }
        if (hours % FUCKING_10 == 1) {
            return hours + " час назад";
        }
        if (hours % FUCKING_10 >= 2 && hours % FUCKING_10 <= FUCKING_4) {
            return hours + " часа назад";
        }
        return hours + " часов назад";
    }

    private static String niceDays(long days) {
        if (days % FUCKING_100 >= FUCKING_11
            && days % FUCKING_100 <= FUCKING_19) {
            return days + " дней назад";
        }
        if (days % FUCKING_10 == 1) {
            return days + " день назад";
        }
        if (days % FUCKING_10 >= 2 && days % FUCKING_10 <= FUCKING_4) {
            return days + " дня назад";
        }
        return days + " дней назад";
    }

    private static String niceTime(Date date) {
        if (System.currentTimeMillis() - date.getTime() < MINUTE * 2) {
            return "Только что";
        } else if (System.currentTimeMillis() - date.getTime() < HOUR) {
            return niceMinutes(
                    (System.currentTimeMillis() - date.getTime()) / MINUTE);
        } else if (System.currentTimeMillis() - date.getTime() < DAY) {
            return niceHours(
                    (System.currentTimeMillis() - date.getTime()) / HOUR);
        } else if (System.currentTimeMillis() - date.getTime() < DAY * 2) {
            return "Вчера";
        } else {
            return niceDays(
                    (System.currentTimeMillis() - date.getTime()) / DAY);
        }
    }

    private static void printTweet(Status status) {
        if (status.isRetweet()) {
            if (hideRetweets) {
                return;
            }
            if (!stream) {
                System.out.print("[" + niceTime(status.getCreatedAt()) + "] ");
            }
            System.out.println(
                    blue("@" + status.getUser().getScreenName()
                             + " ретвитнул "
                             + status.getText().substring(FUCKING_3)));
        } else {
            if (!stream) {
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

    private static void printHelp() {
        System.out.println(
          "Консольное приложение, выводящее на экран поток твитов.\n"
        + "Использование: java TwitterStream [параметры]\n"
        + "Параметры:\n"
        + "-q,--query <query>                     Искать по запросу <query>\n"
        + "-p,--place <location|'nearby'>         Искать по региону, если\n"
        + "                                       значение равно <nearby> или\n"
        + "                                       параметр отсутствует, по ip\n"
        + "-s,--stream                            Приложение непрерывно с\n"
        + "                                       задержкой в 1 секунду\n"
        + "                                       печатает твиты на экран\n"
        + "                                       (ESC для выхода)\n"
        + "--hideRetweets                         Не показывать ретвиты\n"
        + "-l,--limit <tweets>                    Выводить не более <tweet>\n"
        + "                                       твитов (не для --stream)\n"
        + "-h,--help                              Показать справку");
    }

    private static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-q") || args[i].equals("--query")) {
                query = args[i + 1];
            }
            if (args[i].equals("-p") || args[i].equals("--place")) {
                place = args[i + 1];
            }
            if (args[i].equals("-s") || args[i].equals("--stream")) {
                stream = true;
            }
            if (args[i].equals("--hideRetweets")) {
                hideRetweets = true;
            }
            if (args[i].equals("-l") || args[i].equals("--limit")) {
                limit = Integer.parseInt(args[i + 1]);
            }
            if (args[i].equals("-h") || args[i].equals("--help")) {
                help = true;
            }
        }
    }

    private static double[] getLocation() {
        String location;
        if (place.equals("nearby")) {
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
            geoQuery.setQuery(place);
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
        Query searchQuery = new Query(query);
        searchQuery.setCount(Math.min(Q_LIMIT, limit));
        searchQuery.setQuery(query);
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
        while (limit > 0) {
            searchQuery.setCount(Math.min(Q_LIMIT, limit));
            limit -= Q_LIMIT;
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
        if (help) {
            printHelp();
            return;
        }
        twitter = TwitterFactory.getSingleton();
        if (query != null) {
            System.out.println(
                    "Твиты по запросу " + query + " для " + place + ":");
        } else {
            System.out.println("Твиты для " + place + ":");
        }
        System.out.println(
                "----------------------------------------------"
                + "------------------------------------------");

        if (stream) {
            streamTweets();
        } else {
            printTweets();
        }
    }
}
