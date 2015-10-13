package ru.fizteh.fivt.students.nmakeenkov.twitterstream;

import twitter4j.*;
import com.beust.jcommander.*;
import java.util.*;
import org.json.JSONException;

public class TwitterStream {
    private static final String NEARBY = "nearby";
    private static final int LITTLE_SLEEP = 200;
    private static final int BIG_SLEEP = 1000;

    private static Query createQuery(CommandLineParameters params) throws JSONException {
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

    private static void printTweet(Status status, boolean hideRT) {
        if (status.isRetweet()) {
            System.out.println("@" + status.getUser().getName() + " ретвитнул @"
                    + status.getRetweetedStatus().getUser().getName() + " : "
                    + status.getRetweetedStatus().getText());
        } else {
            System.out.print("@" + status.getUser().getName() + ": "
                    + status.getText());
            if (!hideRT) {
                System.out.print(" (" + status.getRetweetCount()
                        + " ретвитов)");
            }
            System.out.println();
        }
    }

    private static void printStream(CommandLineParameters params) throws TwitterException,
            JSONException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = createQuery(params);
        while (true) {
            List<Status> status = new ArrayList<>();
            while (status.size() == 0) {
                status = twitter.search(query).getTweets();
                try {
                    Thread.sleep(LITTLE_SLEEP);
                } catch (Exception e) {
                    System.err.println("Странная ошибка, но работать продолжим");
                }
            }
            printTweet(status.get(0), params.isStream());
            query.setSinceId(status.get(0).getId());
            try {
                Thread.sleep(BIG_SLEEP);
            } catch (Exception e) {
                System.err.println("Странная ошибка, но работать продолжим");
            }
        }
    }

    private static void printQuery(CommandLineParameters params) throws TwitterException, JSONException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = createQuery(params);
        int tweetsLeft = params.getLimit();
        boolean any = false;
        while (tweetsLeft != 0 && query != null) {
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                if (!status.isRetweet() || !params.isHideRetweets()) {
                    any = true;
                    Utils.printTime(status.getCreatedAt().getTime());
                    printTweet(status, params.isStream());
                    if (tweetsLeft != -1) {
                        tweetsLeft--;
                    }
                    if (tweetsLeft == 0) {
                        break;
                    }
                }
            }
            query = result.nextQuery();
        }

        if (!any) {
            System.out.println("Не найдено ни одного твита по вашему запросу");
            return;
        }

    }

    public static void main(String[] args) {
        CommandLineParameters params = new CommandLineParameters();
        JCommander comm;

        try {
            comm = new JCommander(params, args);
            if (params.isHelp()) {
                comm.usage();
                System.exit(0);
            }
        } catch (Exception ex) {
            System.err.println("Error with parameters: " + ex.getMessage());
            System.exit(1);
        }

        try {
            if (params.isStream()) {
                printStream(params);
            } else {
                printQuery(params);
            }
        } catch (TwitterException ex) {
            System.err.println("Error with twitter: " + ex.getMessage());
            System.exit(1);
        } catch (JSONException ex) {
            System.err.println("Error with JSON: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("???: " + ex.getClass());
        }
    }

}
