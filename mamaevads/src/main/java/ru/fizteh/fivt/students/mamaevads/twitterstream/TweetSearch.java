package ru.fizteh.fivt.students.mamaevads.twitterstream;
import twitter4j.*;
import java.util.List;

public class TweetSearch {
    public static void noStreamSearch(Arguments arguments) throws TweetSearchException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(arguments.getQuery());
        int lim = arguments.getLimit();
        if (arguments.getPlace() != "") {
            double[] location = GeoFeatures.getLocation(arguments.getPlace(), twitter);
            query.geoCode(new GeoLocation(location[0], location[1]), location[2], "km");
        }
        QueryResult queryresult;
        boolean any = false;
        try {
            while (true) {
                queryresult = twitter.search(query);
                List<Status> tweets = queryresult.getTweets();
                for (Status status : tweets) {
                    if (!arguments.isHideRetweets() || !status.isRetweet()) {
                        try {
                            System.out.print(MakeMassage.info(status) + "\n");
                            any = true;
                            lim--;
                            if (lim == 0) {
                                return;
                            }
                        } catch (LostInformationException ex) {
                            System.out.print("Message part was lost.");
                        }
                    }
                }
                query = queryresult.nextQuery();
                if (query == null) {
                    if (!any) {
                        System.out.print("No tweets found.");
                    }
                    return;
                }
            }
        } catch (TwitterException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void twitterStream(Arguments arguments)throws TweetSearchException {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        StatusListener listener = new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (!arguments.isHideRetweets() || !status.isRetweet()) {
                    try {
                        System.out.print(MakeMassage.info(status) + "\n");
                    } catch (LostInformationException ex) {
                        System.err.print("Message part was lost.");
                    }
                }
            }
        };
        String[] query = {arguments.getQuery()};
        double[][]location = GeoFeatures.getFilter(arguments.getPlace());
        FilterQuery filter = new FilterQuery();
        filter.track(query);
        if (arguments.getPlace() != "") {
            filter.locations(location);
        }
        twitterStream.addListener(listener);
        twitterStream.filter(filter);
    }

}
