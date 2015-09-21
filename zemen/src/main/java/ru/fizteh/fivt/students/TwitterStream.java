package ru.fizteh.fivt.students;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import twitter4j.*;

import java.util.List;

/**
 * Twitter streaming.
 */
public class TwitterStream {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static void main(String[] argv) throws TwitterException {
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
        Twitter twitter = TwitterFactory.getSingleton();
        String location = arguments.getLocation();
        Query query = new Query(String.join(" ", arguments.getKeywords()));
        if (arguments.getLimit() > 0) {
            query.setCount(arguments.getLimit());
        }
        QueryResult response = twitter.search(query);
        System.out.println("Твиты по запросу " + query.getQuery()
                + " для " + location);
        for (Status tweet : response.getTweets()) {
            if (tweet.isRetweet() && arguments.isHideRetweets()) {
                continue;
            }
            String formattedTweet;
            if (tweet.isRetweet()) {
                Status original = tweet.getRetweetedStatus();

                formattedTweet = "[" + tweet.getCreatedAt() + "]"
                        + " @" + ANSI_BLUE + tweet.getUser().getScreenName()
                        + ANSI_RESET + ": ретвитнул"
                        + " @" + ANSI_BLUE + original.getUser().getScreenName()
                        + ANSI_RESET + ": "
                        + original.getText();
            } else {
                formattedTweet = "[" + tweet.getCreatedAt() + "]"
                        + " @" + ANSI_BLUE + tweet.getUser().getScreenName()
                        + ANSI_RESET + ": "
                        + tweet.getText()
                        + " (" + tweet.getRetweetCount() + " ретвитов)";
            }
            System.out.println(formattedTweet);
        }
    }
}

class Arguments {
    @Parameter(names = {"--query", "-q"}, required = true, variableArity = true,
            description = "Ключевые слова для запроса")
    private List<String> query;

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
}
