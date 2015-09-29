package ru.fizteh.fivt.students.nmakeenkov.twitterstream;

import twitter4j.*;
import com.beust.jcommander.*;
import java.util.*;

public class TwitterStream {
    private static final String NEARBY = "nearby";

    private static Query createQuery(CommandLineParameters params) {
        Query query = new Query(params.getQuery());
        if (params.getPlace().length() > 0) {
            double[] place;
            if (params.getPlace().equals(NEARBY)) {
                place = MyMaps.getMyCoords();
            } else {
                place = MyMaps.getCoordsByPlace(params.getPlace());
            }
            query.geoCode(new GeoLocation(place[0], place[1]), place[2], "km");
        }
        return query;
    }

    private static void printTweet(Status status) {
        if (status.isRetweet()) {
            System.out.println("@" + status.getUser().getName() + " ретвитнул @"
                    + status.getRetweetedStatus().getUser().getName() + " : "
                    + status.getRetweetedStatus().getText());
        } else {
            System.out.println("@" + status.getUser().getName() + ": "
                    + status.getText() + " (" + status.getRetweetCount()
                    + " ретвитов)");
        }
    }

    private static void printStream(CommandLineParameters params) {
        Twitter twitter = (new TwitterFactory()).getInstance();
        Query query = createQuery(params);
        while (true) {
            List<Status> status = null;
            while (status.size() == 0) {
                try {
                    status = twitter.search(query).getTweets();
                } catch (TwitterException ex) {
                    System.err.print(ex.getErrorMessage());
                    System.exit(1);
                }
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    System.err.println("Странная ошибка");
                }
            }
            printTweet(status.get(0));
            query.setSinceId(status.get(0).getId());
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.err.println("Странная ошибка");
            }
        }
    }

    private static void printQuery(CommandLineParameters params) {
        Twitter twitter = (new TwitterFactory()).getInstance();
        Query query = createQuery(params);
        int tweetsLeft = params.getLimit();
        boolean any = false;
        try {
            while (tweetsLeft != 0 && query != null) {
                QueryResult result = twitter.search(query);
                for (Status status : result.getTweets()) {
                    if (!status.isRetweet() || !params.isHideRetweets()) {
                        any = true;
                        Utils.printTime(status.getCreatedAt().getTime());
                        printTweet(status);
                        if (tweetsLeft != -1)
                            tweetsLeft--;
                        if (tweetsLeft == 0)
                            break;
                    }
                }
                query = result.nextQuery();
            }
        } catch (TwitterException ex) {
            System.err.print(ex.getErrorMessage());
            System.exit(1);
        }

        if (!any) {
            System.out.println("Не найдено ни одного твита по вашему запросу");
            System.exit(1);
        }

    }

    public static void main(String[] args) {
        CommandLineParameters params = new CommandLineParameters();
        JCommander comm;

        try {
            comm = new JCommander(params, args);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        if (params.isHelp()) {
            // TODO
            System.exit(0);
        }

        if (params.isStream()) {
            printStream(params);
        } else {
            printQuery(params);
        }
    }

}
