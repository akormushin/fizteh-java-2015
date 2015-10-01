package ru.fizteh.fivt.students.akormushin.moduletests.library;

import twitter4j.*;

import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

/**
 * Created by kormushin on 29.09.15.
 */
public class TwitterServiceImpl implements TwitterService {

    private final Twitter twitter;
    private final TwitterStream twitterStream;

    /**
     * All dependency classes are passed via constructor to decouple them and let us mock them.
     *
     * @param twitter
     * @param twitterStream
     */
    public TwitterServiceImpl(Twitter twitter, TwitterStream twitterStream) {
        this.twitter = twitter;
        this.twitterStream = twitterStream;
    }

    /**
     * @param query
     * @return
     * @throws TwitterException
     */
    @Override
    public List<String> getFormattedTweets(String query) throws TwitterException {
        if (query == null) {
            throw new IllegalArgumentException("Query is required");
        }

        return twitter
                .search(new Query(query)).getTweets().stream()
                .map(this::formatTweet)
                .collect(toList());
    }

    /**
     * @param query
     * @param listener
     */
    @Override
    public void listenForTweets(String query, Consumer<String> listener) {
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                listener.accept(formatTweet(status));
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        });
        twitterStream.filter(new FilterQuery().track(query));
    }

    /**
     * You shouldn't test private methods separately. It will be covered by public methods that use him.
     *
     * @param status
     * @return
     */
    private String formatTweet(Status status) {
        return "@" + status.getUser().getScreenName() + ": " + status.getText();
    }
}
