package ru.fizteh.fivt.students.zemen;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import twitter4j.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Twitter streaming.
 */
public class TwitterStream {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static final int FIVE = 5;
    public static final int TEN = 10;

    private static String getRussianForm(String[] form, long count) {
        if (count / TEN == 1) {
            return form[0];
        }
        if (count % TEN == 1) {
            return form[1];
        }
        if (count % TEN > 0 && count % TEN < FIVE) {
            return form[2];
        }
        return form[0];
    }

    private static String formatTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String res = "[";
        Calendar current = Calendar.getInstance();

        long offset = calendar.getTimeZone().getOffset(
                TimeUnit.MILLISECONDS.toSeconds(calendar.getTimeInMillis()));
        long day = TimeUnit.MILLISECONDS.toDays(calendar.getTimeInMillis()
                + offset);
        long now = TimeUnit.MILLISECONDS.toDays(current.getTimeInMillis()
                + offset);
        assert day <= now;
        if (day + 1 == now) {
            res += "вчера";
        } else if (day < now) {
            long diff = now - day;
            String[] form = new String[]{"дней", "день", "дня"};
            res += diff + " " + getRussianForm(form, diff) + " назад";
        } else {
            long diff = current.getTimeInMillis() - calendar.getTimeInMillis();
            assert diff >= 0;
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            if (hours > 0) {
                String[] form = new String[]{"часов", "час", "часа"};
                res += hours + " " + getRussianForm(form, hours) + " назад";
            } else if (minutes > 2) {
                String[] form = new String[]{"минут", "минуту", "минуты"};
                res += minutes + " " + getRussianForm(form, minutes) + " назад";
            } else {
                res += "только что";
            }
        }
        res += "]";
        return res;
    }

    private static String formatName(String name) {
        return "@" + ANSI_BLUE + name + ANSI_RESET;
    }

    static String formatAll(Status tweet, Arguments arguments) {
        boolean isRetweet = tweet.isRetweet();
        String formattedTweet = "";
        if (!arguments.isStream()) {
            formattedTweet += formatTime(tweet.getCreatedAt()) + " ";
        }
        formattedTweet += formatName(tweet.getUser().getScreenName()) + ": ";
        if (isRetweet) {
            tweet = tweet.getRetweetedStatus();
            formattedTweet += "ретвитнул "
                    + formatName(tweet.getUser().getScreenName()) + ": ";
        }
        formattedTweet += tweet.getText();
        if (!isRetweet) {
            formattedTweet += " (" + tweet.getRetweetCount() + " ретвитов)";
        }
        return formattedTweet;
    }

    private static void streamTweets(Arguments arguments) {
        StatusListener statusListener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                if (arguments.isHideRetweets() && status.isRetweet()) {
                    return;
                }
                System.out.println(formatAll(status, arguments));
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

            @Override
            public void onException(Exception e) {

            }
        };
        twitter4j.TwitterStream twitterStream =
                new TwitterStreamFactory().getInstance();
        twitterStream.addListener(statusListener);
        String[] track = new String[arguments.getKeywords().size()];
        arguments.getKeywords().toArray(track);
        twitterStream.filter(new FilterQuery(0, new long[0], track));
    }

    public static void main(String[] argv) {
        Arguments arguments = new Arguments();
        JCommander jCommander;
        try {
            jCommander = new JCommander(arguments, argv);
        } catch (ParameterException ex) {
            jCommander = new JCommander(arguments);
            jCommander.usage();
            return;
        }
        if (arguments.isHelp()) {
            jCommander.usage();
            return;
        }
        if (arguments.isStream()) {
            streamTweets(arguments);
            return;
        }
        Twitter twitter = TwitterFactory.getSingleton();
        String location = arguments.getLocation();
        Query query = new Query(String.join(" ", arguments.getKeywords()));
        if (arguments.getLimit() > 0) {
            query.setCount(arguments.getLimit());
        }
        QueryResult response = null;
        try {
            response = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Твиты по запросу " + query.getQuery()
                + " для " + location);
        for (Status tweet : response.getTweets()) {
            if (tweet.isRetweet() && arguments.isHideRetweets()) {
                continue;
            }
            System.out.println(formatAll(tweet, arguments));
        }
    }
}

class Arguments {
    @Parameter(names = {"--query", "-q"}, required = true, variableArity = true,
            description = "Ключевые слова для запроса")
    private List<String> query = null;

    @Parameter(names = {"--place", "-p"},
            description = "Искать по заданному региону. "
                    + "Если значение равно nearby "
                    + "или параметр отсутствует - искать по ip")
    private String place = "nearby";

    @Parameter(names = {"--stream", "-s"},
            description = "Равномерно и непрерывно с задержкой в 1 секунду "
                    + "печать твиты на экран")
    private boolean stream = false;

    @Parameter(names = "--hideRetweets", description = "Скрывать ретвиты")
    private boolean hideRetweets = false;

    @Parameter(names = {"--limit", "-l"},
            description = "выводить только ограниченное число твитов. "
                    + "Не применимо для --stream режима")
    private int limit = -1;

    @Parameter(names = {"--help", "-h"}, help = true,
            description = "Show this help")
    private boolean help = false;

    public boolean isHelp() {
        return help;
    }

    public List<String> getKeywords() {
        return query;
    }

    public boolean isHideRetweets() {
        return hideRetweets;
    }

    public int getLimit() {
        return limit;
    }

    public String getLocation() {
        return place;
    }

    public boolean isStream() {
        return stream;
    }
}
