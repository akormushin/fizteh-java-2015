package ru.fizteh.fivt.students.fminkin.twitterstream.library;
import com.beust.jcommander.JCommander;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;




/**
 * Created by thefacetakt on 11.10.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchTest {
    private Location londonLocation = new Location(51.5073509, -0.1277583, "London");

    @Mock
    private Twitter twitter;
    @Mock
    private twitter4j.TwitterStream twitterStream;

    private static List<Status> londonStatuses;
    @Mock
    private GeoLocation g = new GeoLocation();
    @BeforeClass
    public static void loadSampleData() {
        londonStatuses = Twitter4jTestUtils.tweetsFromJson("/tweets.json");
    }


    JCommanderConfig setUpJCommanderSettings(String... args) {
        JCommanderConfig setting = new JCommanderConfig();
        new JCommander(setting, args);
        return setting;
    }

    @Before
    public void setUp() throws Exception {
        when(g.getLocationGoogle("London", new JSonReader()))
                .thenReturn(londonLocation);
        QueryResult q = mock(QueryResult.class);
        when(q.getTweets()).thenReturn(londonStatuses);

        QueryResult emptyQueryResult = mock(QueryResult.class);
        when(emptyQueryResult.getTweets()).thenReturn(new ArrayList<>());

        when(twitter.search(argThat(hasProperty("query", equalTo("[hello]")))))
                .thenReturn(q);
        when(twitter.search(argThat(hasProperty("query", not(equalTo("[hello]"))))))
                .thenReturn(emptyQueryResult);
    }

    @Test
    public void testGetTweetsOnce() throws Exception {
        SearchTweets search = new SearchTweets();
        List<Status> tw = search.search(setUpJCommanderSettings("-q", "hello", "-p", "London"),
                londonLocation, twitter);
        List<String> tweets = tw.
                stream().map(Status::getText).collect(Collectors.toList());
        assertThat(tweets.size(), is(100));
        assertThat(tweets, hasItems("@Juange18 hello! We don't but we usually suggest you call "
                + "us 10-15mins before you arrive so we can try to reserve you a table 02037208825"));
    }

    @Test
    public void testGetTweetsOnceWithoutRT() throws Exception {
        SearchTweets search = new SearchTweets();
        List<String> tweets = search.search(setUpJCommanderSettings("-q", "hello", "-p", "London",
                "--hideRetweets"), londonLocation, twitter)
                .stream().map(Status::getText).collect(Collectors.toList());
        assertThat(tweets.size(), is(42));
        assertThat(tweets, hasItems("@Juange18 hello! We don't but we usually suggest you call "
                + "us 10-15mins before you arrive so we can try to reserve you a table 02037208825"));
        assertThat(tweets, not(hasItem("this is basically a retweet")));

    }

    @Test
    public void testGetTweetsOnceEmptyResults() throws Exception {
        SearchTweets search = new SearchTweets();
        List<Status> tweets = search.search(setUpJCommanderSettings("-q", "ну такой строки я "
                        + "думаю точно нет"),
                londonLocation, twitter);
        assertThat(tweets.size(), is(0));
    }
}
