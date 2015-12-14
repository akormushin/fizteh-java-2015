package ru.fizteh.fivt.students.oshch.moduletests;

import com.beust.jcommander.JCommander;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.oshch.moduletests.library.Parameters;
import ru.fizteh.fivt.students.oshch.moduletests.library.SearchTweets;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.Twitter4jTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchTweetsTest {
    @Mock
    private Twitter twitter;

    @InjectMocks
    private SearchTweets search;

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
        Parameters param = new Parameters();
        String[] args = {"-q", "mipt", "-s", "-l", "100", "-p", ""};
        JCommander cmd = new JCommander(param, args);

        List<Status> tweets = new ArrayList<>();
        search.search(param, tweets::add);
        assertThat(tweets, hasSize(13));

        verify(twitter).search(argThat(hasProperty("query", equalTo("mipt"))));
    }

    @Test
    public void limitSearchRusultTest() throws Exception {
        Parameters param = new Parameters();
        String[] args = {"-q", "mipt", "-s", "-l", "5", "-p", ""};
        JCommander cmd = new JCommander(param, args);

        List<Status> tweets = new ArrayList<>();
        search.search(param, tweets::add);

        assertThat(tweets, hasSize(5));
        verify(twitter).search(argThat(hasProperty("query", equalTo("mipt"))));
    }

    @Test
    public void empltySearchRusultTest() throws Exception {
        Parameters param = new Parameters();
        String[] args = {"-q", "hse", "-s", "-l", "100", "-p", ""};
        JCommander cmd = new JCommander(param, args);

        List<Status> tweets = new ArrayList<>();
        search.search(param, tweets::add);
        assertThat(tweets, hasSize(0));
    }
}