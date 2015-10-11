package ru.fizteh.fivt.students.thefacetakt.twitterstream;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.exceptions.InvalidLocationException;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.exceptions.LocationDefinitionErrorException;
import twitter4j.*;
import twitter4j.Twitter4jTestUtils;


import java.net.MalformedURLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by thefacetakt on 11.10.15.
 */
public class TweetsGetterTest {
    private Location MoscowLocation = new Location(55.7500, 37.6167, "Moscow");

    @Mock
    private Twitter twitter;

    @Mock
    private twitter4j.TwitterStream twitterStream;

    @Mock
    private PlaceLocationResolver geoResolver;

    @InjectMocks
    private TweetsGetter tweetsGetter;

    private List<Status> aMoscowStatuses;

    @BeforeClass
    private void loadSampleData() {

    }

    @Before
    public void setUp() throws InvalidLocationException,
            LocationDefinitionErrorException, MalformedURLException,
            TwitterException {
        when(geoResolver.resolvePlaceLocation("Moscow"))
                .thenReturn(MoscowLocation);

        QueryResult aQueryResult = mock(QueryResult.class);

        when(aQueryResult.getTweets()).thenReturn();

        when(twitter.search(argThat(hasProperty("query", equalTo("a")))))
                .thenReturn(aQueryResult);
    }



    @Test
    public void testGetTweetsOnce() throws Exception {

    }

    @Test
    public void testGetTwitterStream() throws Exception {

    }
}