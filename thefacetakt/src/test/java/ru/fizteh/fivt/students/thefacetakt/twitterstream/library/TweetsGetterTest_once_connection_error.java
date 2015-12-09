package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import com.beust.jcommander.JCommander;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.*;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.exceptions.TwitterStreamException;
import twitter4j.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by thefacetakt on 12.10.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class TweetsGetterTest_once_connection_error {

    private ru.fizteh.fivt.students.thefacetakt.twitterstream.library.Location MoscowLocation = new ru.fizteh.fivt.students.thefacetakt.twitterstream.library.Location(55.7500, 37.6167, "Moscow");

    @Mock
    private Twitter twitter;

    @Mock
    private PlaceLocationResolver geoResolver;

    private JCommanderSetting aMoscowSettings;

    @Before
    public void setUp() throws Exception {
        when(geoResolver.resolvePlaceLocation("Moscow"))
                .thenReturn(MoscowLocation);

        aMoscowSettings = new JCommanderSetting();

        new JCommander(aMoscowSettings, "-q", "a",
                "-p", "Moscow");

        when(twitter.search(argThat(hasProperty("query", equalTo("a")))))
                .thenThrow(new TwitterException("connection error"));
    }



    @Test(expected= TwitterStreamException.class)
    public void testGetTweetsOnce() throws Exception {
        TweetsGetter tweetsGetter = new TweetsGetter(geoResolver);
        tweetsGetter.getTweetsOnce(aMoscowSettings,
                MoscowLocation, twitter);
    }
}