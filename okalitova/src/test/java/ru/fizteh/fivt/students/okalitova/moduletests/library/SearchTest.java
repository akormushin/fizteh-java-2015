package ru.fizteh.fivt.students.okalitova.moduletests.library;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.okalitova.twitterstreamer.InvalidQueryException;
import ru.fizteh.fivt.students.okalitova.twitterstreamer.NoTweetsException;
import twitter4j.*;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by nimloth on 06.11.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class SearchTest {

    @Mock
    private Twitter twitter;

    @InjectMocks
    private Search search;

    public static List<Status> statuses;

    @BeforeClass
    public static void loadSampleData() {
        statuses = Twitter4jTestUtils.tweetsFromJson("/statuses.json");
    }

    @Before
    public void setUp() throws Exception {
        QueryResult queryResult = mock(QueryResult.class);

        when(queryResult.getTweets()).thenReturn(statuses);
        when(twitter.search(argThat(hasProperty("query", equalTo("mipt")))))
                .thenReturn(queryResult);
        when(queryResult.nextQuery()).thenReturn(null);

        QueryResult emptyQueryResult = mock(QueryResult.class);
        when(emptyQueryResult.getTweets()).thenReturn(Collections.emptyList());

        when(twitter.search(argThat(hasProperty("query", not(equalTo("mipt"))))))
                .thenReturn(emptyQueryResult);
        when(queryResult.nextQuery()).thenReturn(null);
    }

    @Test
    public void simpleSearchRusultTest() throws Exception {
        final String ansiReset = "\u001B[0m";
        final String ansiPurple =  "\u001b[35m";
        ParametersParser param = new ParametersParser();
        param.setQuery("mipt");
        param.setStream(true);
        param.setLimit(100);
        param.setHideRetwitts(false);
        param.setHelp(false);
        param.setPlace("");
        List<String> tweets = search.searchResult(param);
        assertThat(tweets, hasSize(13));
        assertThat(tweets, hasItems(
                ansiPurple
                        + "@QuintetLive"
                        + ansiReset
                        + ": #QuintetLaunch : welddon launched the MIPT DGAP (Human Solo).",
                ansiPurple
                        + "@mipt_blues"
                        + ansiReset
                        + ": А в перерывах дельта-блюз включают, и того лучше"
        ));
        verify(twitter).search(argThat(hasProperty("query", equalTo("mipt"))));
    }

    @Test
    public void limitSearchRusultTest() throws Exception {
        ParametersParser param = new ParametersParser();
        param.setQuery("mipt");
        param.setStream(true);
        param.setLimit(5);
        param.setHideRetwitts(false);
        param.setHelp(false);
        param.setPlace("");
        List<String> tweets = search.searchResult(param);
        assertThat(tweets, hasSize(5));
        verify(twitter).search(argThat(hasProperty("query", equalTo("mipt"))));
    }

    @Test(expected = NoTweetsException.class)
    public void empltySearchRusultTest() throws Exception {
        ParametersParser param = new ParametersParser();
        param.setQuery("hse");
        param.setStream(true);
        param.setLimit(100);
        param.setHideRetwitts(false);
        param.setHelp(false);
        param.setPlace("");
        search.searchResult(param);
    }

    @Test
    public void simpleSetQueryTest() throws Exception {
        ParametersParser param = new ParametersParser();
        param.setQuery("msu");
        param.setStream(true);
        param.setLimit(56);
        param.setHideRetwitts(true);
        param.setHelp(false);
        param.setPlace("");
        Query query = search.setQuery(param);
        assertEquals(query.getQuery(), "msu");
    }

    @Test(expected = InvalidQueryException.class)
    public void emptySetQueryTest() throws Exception {
        ParametersParser param = new ParametersParser();
        param.setQuery("");
        param.setStream(true);
        param.setLimit(56);
        param.setHideRetwitts(true);
        param.setHelp(false);
        param.setPlace("");
        search.setQuery(param);
    }
}
