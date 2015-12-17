package ru.fizteh.fivt.students.nmakeenkov.twitterstream;

import ru.fizteh.fivt.students.nmakeenkov.twitterstream.library.*;
import twitter4j.*;
import com.beust.jcommander.*;
import java.util.*;
import org.json.JSONException;
import ru.fizteh.fivt.students.nmakeenkov.twitterstream.library.CommandLineParametersParser.Parameters;

public class TwitterStream {
    private static final int LITTLE_SLEEP = 200;
    private static final int BIG_SLEEP = 1000;

    private static void printStream(Parameters params) throws TwitterException,
            JSONException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = TwitterUtils.buildQuery(params);
        while (true) {
            List<Status> status = new ArrayList<>();
            while (status.size() == 0) {
                status = twitter.search(query).getTweets();
                Utils.mySleep(LITTLE_SLEEP);
            }
            System.out.println(TwitterUtils.buildTweet(status.get(0), params.isStream()));
            query.setSinceId(status.get(0).getId());
            Utils.mySleep(BIG_SLEEP);
        }
    }


    private static void printQuery(Parameters params) throws TwitterException, JSONException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = TwitterUtils.buildQuery(params);
        int tweetsLeft = params.getLimit();
        boolean any = false;
        while (tweetsLeft != 0 && query != null) {
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                if (!status.isRetweet() || !params.isHideRetweets()) {
                    any = true;
                    System.out.print(Utils.getTimeDifference(status.getCreatedAt().getTime(),
                            System.currentTimeMillis()));
                    System.out.println(TwitterUtils.buildTweet(status, params.isStream()));
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
       }

    }

    public static void main(String[] args) {
        Parameters params = new Parameters();
        try {
            params = new CommandLineParametersParser().parse(args);
            if (params.isHelp()) {
                System.exit(0);
            }
        } catch (ParameterException ex) {
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
            System.err.println("???: " + ex.getClass().toString());
        }
    }

}
