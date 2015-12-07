package ru.fizteh.fivt.students.akormushin.moduletests.library;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.akormushin.moduletests.library.TwitterServiceImpl;
import twitter4j.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Example unit tests for tweets retrieval service with mocking
 * <p>
 * Created by kormushin on 29.09.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceImplTest {

    @Mock
    private Twitter twitter;

    @Mock
    private TwitterStream twitterStream;

    /**
     * Mockito calls <pre>new TwitterServiceImpl(twitter, twitterStream)</pre> here
     */
    @InjectMocks
    private TwitterServiceImpl twitterService;

    public static List<Status> statuses;

    @BeforeClass
    public static void loadSampleData() {
        statuses = Twitter4jTestUtils.tweetsFromJson("/search-java-response.json");
    }

    /**
     * Preparing sample data for test. Initializing mocks
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        QueryResult queryResult = mock(QueryResult.class);
        when(queryResult.getTweets()).thenReturn(statuses);

        when(twitter.search(argThat(hasProperty("query", equalTo("java")))))
                .thenReturn(queryResult);

        QueryResult emptyQueryResult = mock(QueryResult.class);
        when(emptyQueryResult.getTweets()).thenReturn(Collections.emptyList());

        when(twitter.search(argThat(hasProperty("query", not(equalTo("java"))))))
                .thenReturn(emptyQueryResult);
    }

    @Test
    public void testGetFormattedTweets() throws Exception {
        List<String> tweets = twitterService.getFormattedTweets("java");

        assertThat(tweets, hasSize(15));
        assertThat(tweets, hasItems(
                "@lxwalls: RT @Space_Station: How do astronauts take their coffee?\n#NationalCoffeeDay \nhttp://t.co/fx4lQcu0Xp http://t.co/NbOZoQDags",
                "@Ankit__Tomar: #Hiring Java Lead Developer - Click here for job details : http://t.co/0slLn3YVTW"
        ));

        verify(twitter).search(argThat(hasProperty("query", equalTo("java"))));
    }

    @Test
    public void testGetFormattedTweets_empty_result() throws Exception {
        List<String> tweets = twitterService.getFormattedTweets("c#");

        assertThat(tweets, hasSize(0));

        verify(twitter).search(argThat(hasProperty("query", equalTo("c#"))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFormattedTweets_null_query() throws Exception {
        twitterService.getFormattedTweets(null);
    }

    @Test
    public void testListenForTweets() throws Exception {
        //Use ArgumentCaptor to remember argument between different stub invocations
        ArgumentCaptor<StatusListener> statusListener = ArgumentCaptor.forClass(StatusListener.class);
        //Mocking void method
        doNothing().when(twitterStream).addListener((StatusListener) statusListener.capture());
        doAnswer(i -> {
            statuses.forEach(s -> statusListener.getValue().onStatus(s));
            return null;
        }).when(twitterStream).filter(any(FilterQuery.class));

        List<String> tweets = new ArrayList<>();

        twitterService.listenForTweets("java", tweets::add);

        assertThat(tweets, hasSize(15));
        assertThat(tweets, hasItems(
                "@lxwalls: RT @Space_Station: How do astronauts take their coffee?\n#NationalCoffeeDay \nhttp://t.co/fx4lQcu0Xp http://t.co/NbOZoQDags",
                "@Ankit__Tomar: #Hiring Java Lead Developer - Click here for job details : http://t.co/0slLn3YVTW"
        ));

        verify(twitterStream).addListener((StatusListener) any(StatusAdapter.class));
        verify(twitterStream).filter(any(FilterQuery.class));
    }
}