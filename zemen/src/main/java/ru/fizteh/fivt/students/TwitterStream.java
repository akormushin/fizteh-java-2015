package ru.fizteh.fivt.students;

import twitter4j.*;

import java.util.List;

/**
 * Twitter streaming.
 */
public class TwitterStream {
    public static void main(String[] args) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        List<Status> statusList = twitter.getHomeTimeline();
        for (Status status: statusList) {
            System.out.println(status.getText());
        }
    }
}
