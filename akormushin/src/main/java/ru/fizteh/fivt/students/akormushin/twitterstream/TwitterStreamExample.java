package ru.fizteh.fivt.students.akormushin.twitterstream;

import twitter4j.*;

/**
 * Created by kormushin on 15.09.15.
 */
public class TwitterStreamExample {

    public static void main(String[] args) throws TwitterException {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                System.out.println(
                        status.getUser().getName() + " : " + status.getText());
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        });
        twitterStream.filter(new FilterQuery()
                .track("java")
                .language("en"));
    }

}
