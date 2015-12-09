package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import com.beust.jcommander.JCommander;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.*;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.exceptions.TwitterStreamException;
import twitter4j.*;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by thefacetakt on 11.10.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class TweetsGetterTest_once {
    private ru.fizteh.fivt.students.thefacetakt.twitterstream.library.Location MoscowLocation = new ru.fizteh.fivt.students.thefacetakt.twitterstream.library.Location(55.7500, 37.6167, "Moscow");

    @Mock
    private Twitter twitter;

    @Mock
    private twitter4j.TwitterStream twitterStream;

    @Mock
    private PlaceLocationResolver geoResolver;

    private static List<Status> aMoscowStatuses;

    @BeforeClass
    public static void loadSampleData() {
        aMoscowStatuses = Twitter4jTestUtils
                .tweetsFromJson("/aMoscowTweets.json");
    }


    JCommanderSetting setUpJCommanderSettings(String... args) {
        JCommanderSetting setting = new JCommanderSetting();
        new JCommander(setting, args);
        return setting;
    }

    @Before
    public void setUp() throws Exception {
        when(geoResolver.resolvePlaceLocation("Moscow"))
                .thenReturn(MoscowLocation);

        QueryResult aQueryResult = mock(QueryResult.class);
        when(aQueryResult.getTweets()).thenReturn(aMoscowStatuses);

        QueryResult emptyQueryResult = mock(QueryResult.class);
        when(emptyQueryResult.getTweets()).thenReturn(new ArrayList<>());

        when(twitter.search(argThat(hasProperty("query", equalTo("a")))))
                .thenReturn(aQueryResult);
        when(twitter.search(argThat(hasProperty("query", not(equalTo("a"))))))
                .thenReturn(emptyQueryResult);
    }



    @Test
    public void testGetTweetsOnce() throws Exception {
        TweetsGetter tweetsGetter = new TweetsGetter(geoResolver);
        List<String> tweets =
                tweetsGetter.getTweetsOnce(
                        setUpJCommanderSettings("-q", "a", "-p", "Moscow"),
                        MoscowLocation, twitter);
        assertThat(tweets.size(), is(100));
        assertThat(tweets, hasItems("@MashaRaif: Я уверена, он(а) делает что-то"
                        + " для вас, потому что это выгодно ему(ей)",
                "@nyoquira82: ретвитнул @plushev: А вот "
                        + "такой полувнутрячок, для коллег и постоянных слушателей. "
                        + "Улыбчивый блондин рядом со мной - "
                        + "это… https://t.co/f74eHz82i5"));

    }

    @Test
    public void testGetTweetsOnceWithoutRT() throws Exception {
        TweetsGetter tweetsGetter = new TweetsGetter(geoResolver);
        List<String> tweets =
                tweetsGetter.getTweetsOnce(
                        setUpJCommanderSettings("-q", "a", "-p", "Moscow",
                                "--hideRetweets"),
                        MoscowLocation, twitter);
        assertThat(tweets.size(), is(91));
        assertThat(tweets, hasItems("@MashaRaif: Я уверена, он(а) делает что-то"
                + " для вас, потому что это выгодно ему(ей)"));
        assertThat(tweets, not(hasItem("@nyoquira82: ретвитнул @plushev: А вот "
                + "такой полувнутрячок, для коллег и постоянных слушателей. "
                + "Улыбчивый блондин рядом со мной - "
                + "это… https://t.co/f74eHz82i5")));
    }

    @Test
    public void testGetTweetsOnceEmptyResults() throws Exception {
        TweetsGetter tweetsGetter = new TweetsGetter(geoResolver);
        List<String> tweets =
                tweetsGetter.getTweetsOnce(setUpJCommanderSettings("-q", "b"),
                        MoscowLocation, twitter);
        assertThat(tweets.size(), is(0));
    }

    @Test(expected = TwitterStreamException.class)
    public void testGetTweetsOnceEmptyQuery() throws Exception {
        TweetsGetter tweetsGetter = new TweetsGetter(geoResolver);
        tweetsGetter.getTweetsOnce(setUpJCommanderSettings("-q", ""),
                        MoscowLocation, twitter);
    }
}