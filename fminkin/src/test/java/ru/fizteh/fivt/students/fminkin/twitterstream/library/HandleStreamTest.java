package ru.fizteh.fivt.students.fminkin.twitterstream.library;

import com.beust.jcommander.JCommander;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Created by Федор on 15.12.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class HandleStreamTest {
    private Location londonLocation = new Location(51.5073509, -0.1277583, "London");
    @Mock
    private twitter4j.TwitterStream twitterStream;
    private static List<Status> londonStatuses;
    @Mock
    private GeoLocation g = new GeoLocation();
    @BeforeClass
    public static void loadSampleData() {
        londonStatuses = twitter4j.Twitter4jTestUtils.tweetsFromJson("/tweets.json");
    }

    JCommanderConfig setUpJCommanderSettings(String... args) {
        JCommanderConfig setting = new JCommanderConfig();
        new JCommander(setting, args);
        return setting;
    }

    @Before
    public void setUp() throws Exception {
        ArgumentCaptor<StatusListener> statusCaptor
                = ArgumentCaptor.forClass(StatusListener.class);
        doNothing().when(twitterStream).addListener((StatusListener)
                statusCaptor.capture());
        doAnswer(invocation -> {
            londonStatuses.forEach(s -> statusCaptor.getValue().onStatus(s));
            return null;
        }).when(twitterStream).filter(any(FilterQuery.class));


        when(g.getLocationGoogle(anyString(), any()))
                .then(invocation -> {
                    switch (invocation.getArguments()[0].toString()) {
                        case "Лондон, Великобритания":
                            return londonLocation;
                        case "London, Great Britain":
                            return londonLocation;
                        default:
                            throw new IOException();
                    }
                });
    }

    @Test
    public void testGetTwitterStream() throws Exception {
        ArrayList<String> tweets = new ArrayList<>();
        SearchTweets search = new SearchTweets();
        search.handleStream(setUpJCommanderSettings("-q", "hello", "-p",
                "Лондон", "-s"), londonLocation, twitterStream, tweets::add);
        assertThat(tweets.size(), is(100));
        assertThat(tweets, hasItems("@BelAndNevCafe: @Juange18 hello! We don't but we usually suggest you call "
                + "us 10-15mins before you arrive so we can try to reserve you a table 02037208825"));
    }

    @Test
    public void testGetTwitterNoRTStream() throws Exception {
        ArrayList<String> tweets = new ArrayList<>();
        SearchTweets search = new SearchTweets();
        search.handleStream(setUpJCommanderSettings("-q", "hello", "-p",
                        "Лондон", "-s", "--hideRetweets"),
                londonLocation, twitterStream, tweets::add);
        assertThat(tweets.size(), is(42));
        assertThat(tweets, hasItems("@BelAndNevCafe: @Juange18 hello! We don't but we usually suggest you call "
                + "us 10-15mins before you arrive so we can try to reserve you a table 02037208825"));
        assertThat(tweets, not(hasItem("this is basically a retweet")));
    }
}
