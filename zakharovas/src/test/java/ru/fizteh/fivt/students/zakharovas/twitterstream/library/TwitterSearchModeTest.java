package ru.fizteh.fivt.students.zakharovas.twitterstream.library;

import com.beust.jcommander.JCommander;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.zakharovas.twitterstream.CommandLineArgs;
import ru.fizteh.fivt.students.zakharovas.twitterstream.library.exceptions.EmptyResultException;
import twitter4j.*;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by alexander on 10.11.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class TwitterSearchModeTest {

    @Mock Twitter twitter;

    @Test(expected = TwitterException.class)
    public void testTwitterExceptionThrowing() throws Exception {

        CommandLineArgs emptyArguments = new CommandLineArgs();
        new JCommander(emptyArguments, ArgumentSeparator.separateArguments(new String[]{"-q a"}));
        TwitterException exception = mock(TwitterException.class);
        when(exception.isCausedByNetworkIssue()).thenReturn(false);
        when(twitter.search(any(Query.class))).thenThrow(exception);

        GeoLocator emptyGeoLocator = mock(GeoLocator.class);
        GeoLocation location = new GeoLocation(0, 0);
        when(emptyGeoLocator.getLocationForSearch()).thenReturn(location);
        when(emptyGeoLocator.getRadius()).thenReturn(0d);

        TwitterSearchMode searchMode = new TwitterSearchMode(twitter, emptyArguments, emptyGeoLocator);
        searchMode.search();
    }

    @Test(expected = EmptyResultException.class)
    public void testOfEmptyResult() throws Exception {
        CommandLineArgs emptyArguments = new CommandLineArgs();
        new JCommander(emptyArguments, ArgumentSeparator.separateArguments(new String[]{"-q a"}));
        QueryResult result = mock(QueryResult.class);
        when(result.getTweets()).thenReturn(new ArrayList<Status>());
        when(twitter.search(any(Query.class))).thenReturn(result);
        GeoLocator emptyGeoLocator = mock(GeoLocator.class);
        GeoLocation location = new GeoLocation(0, 0);
        when(emptyGeoLocator.getLocationForSearch()).thenReturn(location);
        when(emptyGeoLocator.getRadius()).thenReturn(0d);
        TwitterSearchMode searchMode = new TwitterSearchMode(twitter, emptyArguments, emptyGeoLocator);
        searchMode.search();
    }

}
