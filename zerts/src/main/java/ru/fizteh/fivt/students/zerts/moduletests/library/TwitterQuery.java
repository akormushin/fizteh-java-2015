package ru.fizteh.fivt.students.zerts.moduletests.library;

import ru.fizteh.fivt.students.zerts.TwitterStream.Printer;
import ru.fizteh.fivt.students.zerts.TwitterStream.TwitterReader;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.GeoExeption;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.SearchTweetExeption;
import twitter4j.*;

import java.io.IOException;
import java.util.List;

public class TwitterQuery {

    public static void query(ArgsParser argsPars) throws IOException, GeoExeption, SearchTweetExeption,
            InterruptedException, JSONException {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            GeoLocation queryLocation = GeoParser.getCoordinates(argsPars.getPlace());
            if (queryLocation == null) {
                throw new GeoExeption("Bad query");
            }
            Query query = new Query(argsPars.getQuery().toString()).geoCode(queryLocation,
                    TwitterReader.getLocateRadius(), "km");
            QueryResult result;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                Printer.print("Tweets with " + argsPars.getQuery());
                if (argsPars.getPlace() != null) {
                    Printer.print(" near ");
                    if (argsPars.getPlace().equals("nearby")) {
                        Printer.print(GeoParser.getMyPlace());
                    } else {
                        Printer.print(argsPars.getPlace());
                    }
                }
                Printer.printLine();
                if (tweets.isEmpty()) {
                    Printer.print("Sorry, no tweets found :(\n");
                    System.exit(0);
                }
                for (Status tweet : tweets) {
                    TweetPrinter.printTweet(tweet, argsPars, false);
                    if (argsPars.getNumberOfTweets() == TweetPrinter.getPrintedTweets()) {
                        return;
                    }
                }
                query = result.nextQuery();
            } while (query != null);
            System.exit(0);
        } catch (TwitterException te) {
            throw new SearchTweetExeption();
        }
    }
}
