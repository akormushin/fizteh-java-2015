package ru.fizteh.fivt.students.Jettriangle.twitterstream;

/**
 * Created by rtriangle on 04.10.15.
 */
import twitter4j.*;

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