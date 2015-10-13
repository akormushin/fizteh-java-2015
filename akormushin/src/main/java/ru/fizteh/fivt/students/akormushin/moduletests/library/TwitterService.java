package ru.fizteh.fivt.students.akormushin.moduletests.library;

import twitter4j.TwitterException;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by kormushin on 30.09.15.
 */
public interface TwitterService {
    List<String> getFormattedTweets(String query) throws TwitterException;

    void listenForTweets(String query, Consumer<String> listener);
}
