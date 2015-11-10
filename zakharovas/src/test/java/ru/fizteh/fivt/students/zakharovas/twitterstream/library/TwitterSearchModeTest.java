package ru.fizteh.fivt.students.zakharovas.twitterstream.library;

import com.beust.jcommander.JCommander;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.zakharovas.twitterstream.CommandLineArgs;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by alexander on 10.11.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class TwitterSearchModeTest {

    @Test(expected = TwitterException.class)
    public void testTwitterExceptionThrowing() throws Exception {

        CommandLineArgs emptyArguments = new CommandLineArgs();
        new JCommander(emptyArguments, ArgumentSeparator.separateArguments(new String[]{"-q a"}));

        Twitter twitter = mock(Twitter.class);
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

}
