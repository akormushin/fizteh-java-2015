package ru.fizteh.fivt.students.akormushin.twitterstream;

import twitter4j.*;

/**
 * Created by kormushin on 15.09.15.
 */
public class TwitterStream {

    public static void main(String[] args) throws TwitterException {
        TwitterFactory.getSingleton()
                .search(new Query("физтех")).getTweets().stream()
                .map(s ->
                        "@" + s.getUser().getScreenName() + ": " + s.getText())
                .forEach(System.out::println);
    }

}
