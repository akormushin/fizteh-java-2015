package ru.fizteh.fivt.students.akormushin.twitterstream;

import twitter4j.Query;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Created by kormushin on 15.09.15.
 */
public class TwitterTweets {

    public static void main(String[] args) throws TwitterException {
        TwitterFactory.getSingleton()
                .search(new Query("физтех")).getTweets().stream()
                .map(s ->
                        "@" + s.getUser().getScreenName() + ": " + s.getText())
                .forEach(System.out::println);
    }

}
