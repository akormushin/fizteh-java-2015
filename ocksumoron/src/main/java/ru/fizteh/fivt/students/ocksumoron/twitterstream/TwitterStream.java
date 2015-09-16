package main.java.ru.fizteh.fivt.students.twitterstream;

import twitter4j.*;

/**
 * Created by ocksumoron on 16.09.15.
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
