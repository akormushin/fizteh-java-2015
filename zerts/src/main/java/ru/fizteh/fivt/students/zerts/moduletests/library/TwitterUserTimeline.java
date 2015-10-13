package ru.fizteh.fivt.students.zerts.moduletests.library;

import ru.fizteh.fivt.students.zerts.TwitterStream.Printer;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.GetTimelineExeption;
import twitter4j.*;

import java.util.List;

public class TwitterUserTimeline {
    public static void userStream(ArgsParser argsPars) throws GetTimelineExeption {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            int currPage = 1;
            User user = twitter.verifyCredentials();
            Printer.print("\nShowing @" + user.getScreenName() + "'s home timeline.\n\n");
            Printer.printLine();
            List<Status> tweets;
            do {
                Paging p = new Paging(currPage);
                tweets = twitter.getHomeTimeline(p);
                for (Status tweet : tweets) {
                    TweetPrinter.printTweet(tweet, argsPars, true);
                    if (argsPars.getNumberOfTweets() == TweetPrinter.getPrintedTweets()) {
                        return;
                    }
                }
                currPage++;
            } while (!tweets.isEmpty());
        } catch (TwitterException te) {
            throw new GetTimelineExeption("Can't read the timeline");
        }
    }
}
