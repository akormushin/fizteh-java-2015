package ru.fizteh.fivt.students.w4r10ck1337.twitterstream;

import com.beust.jcommander.JCommander;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.beust.jcommander.Parameter;


public class TwitterStream {
    private static final int INF = 1000000000;
    private static final int Q_LIMIT = 100;
    private static final int FUCKING_27 = 27;
    private static final int FUCKING_3 = 3;
    private static Twitter twitter = null;

    private static class Parameters {
        @Parameter
        private List<String> parameters = new ArrayList<>();

        @Parameter(
                names = {"-q", "--query"},
                description = "Искать по запросу <query>")
        private String query = "";

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

    private static void printTweet(Status status) {
        if (status.isRetweet()) {
            if (parameters.hideRetweets) {
                return;
            }
            if (!parameters.stream) {
                System.out.print(
                        "[" + NiceTime.diff(status.getCreatedAt()) + "] ");
            }
            System.out.println(
                    blue("@" + status.getUser().getScreenName()
                             + " ретвитнул "
                             + status.getText().substring(FUCKING_3)));
        } else {
            if (!parameters.stream) {
                System.out.print(
                        "[" + NiceTime.diff(status.getCreatedAt()) + "] ");
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

    private static Query createQuery() {
        Query searchQuery = new Query(parameters.query);
        searchQuery.setCount(Math.min(Q_LIMIT, parameters.limit));
        searchQuery.setQuery(parameters.query);
        double[] location = GeoApi.getLocation(parameters.place, twitter);
        while (location == null) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                System.err.println("Ошибка ожидания");
            }
            location = GeoApi.getLocation(parameters.place, twitter);
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
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    System.err.println("Ошибка ожидания");
                }
            }
            if (tweets.size() == 0) {
                System.out.println("Не найдены твиты по запросу");
                return;
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
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    System.err.println("Ошибка ожидания");
                }
            }
            try {
                Thread.sleep(
                        Math.max(TimeUnit.SECONDS.toMillis(1)
                                - System.currentTimeMillis() + prev, 0));
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
        if (parameters.query.length() > 0) {
            System.out.println(
                    "Твиты по запросу " + parameters.query
                            + " для " + parameters.place + ":");
        } else {
            System.out.println("Твиты для " + parameters.place + ":");
        }
        System.out.println(
                "----------------------------------------------"
                        + "------------------------------------------");
        twitter = TwitterFactory.getSingleton();
        if (parameters.hideRetweets) {
            parameters.query += " +exclude:retweets";
        }
        if (parameters.stream) {
            streamTweets();
        } else {
            printTweets();
        }
    }
}
