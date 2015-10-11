package ru.fizteh.fivt.students.Duha666.TwitterStream;

import twitter4j.*;

import java.util.List;

public class TweetPrinter {
    public static void showTweets(JCommanderSettings jcs) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(jcs.getQuery().toString());
        query.setCount(jcs.getLimitNumber());
        QueryResult queryResult = twitter.search(query);
        List<Status> tweets = queryResult.getTweets();
        for (Status tweet: tweets) {
            if (!jcs.isHideRetweets() || !tweet.isRetweet()) {
                System.out.print("[" + (System.currentTimeMillis() - tweet.getCreatedAt().getTime()) + "]");

            }
        }
    }
}
