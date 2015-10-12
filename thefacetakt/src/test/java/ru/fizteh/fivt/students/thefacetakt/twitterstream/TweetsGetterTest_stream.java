package ru.fizteh.fivt.students.thefacetakt.twitterstream;

import com.beust.jcommander.JCommander;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.exceptions.InvalidLocationException;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Created by thefacetakt on 12.10.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class TweetsGetterTest_stream {
    private Location MoscowLocation = new Location(55.7500, 37.6167, "Moscow");
    private int notMoscowTweets = 0;

    @Mock
    twitter4j.TwitterStream twitterStream;

    @Mock
    PlaceLocationResolver geoResolver;

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
        ArgumentCaptor<StatusListener> statusCaptor
                = ArgumentCaptor.forClass(StatusListener.class);
        doNothing().when(twitterStream).addListener((StatusListener)
                statusCaptor.capture());
        doAnswer(invocation -> {
            aMoscowStatuses.forEach(s -> statusCaptor.getValue().onStatus(s));
            return null;
        }).when(twitterStream).filter(any(FilterQuery.class));


        when(geoResolver.resolvePlaceLocation(anyString()))
                .then(invocation -> {
                    switch (invocation.getArguments()[0].toString()) {
                        case "Москва, Россия":
                            return MoscowLocation;
                        case "Russia, Moscow":
                            return MoscowLocation;
                        case "Пермь":
                            ++notMoscowTweets;
                            return new Location(58.0000, 56.3167, "Пермь");
                        case "Омск":
                            ++notMoscowTweets;
                            return new Location(54.9833, 73.3667, "Омск");
                        case "Красноярск":
                            ++notMoscowTweets;
                            return new Location(56.0167, 93.0667, "Красноярск");
                        default:
                            ++notMoscowTweets;
                            throw new InvalidLocationException();
                    }
                });
    }




    @Test
    public void testGetTwitterStream() throws Exception {
        ArrayList<String> tweets = new ArrayList<>();
        TweetsGetter tweetsGetter = new TweetsGetter(geoResolver);
        tweetsGetter.getTwitterStream(setUpJCommanderSettings("-q", "а", "-p",
                "Moscow", "-s"), MoscowLocation, tweets::add, twitterStream);
        assertThat(tweets.size(), is(100 - notMoscowTweets));
        assertThat(tweets, hasItems("@MashaRaif: Я уверена, он(а) делает что-то"
                        + " для вас, потому что это выгодно ему(ей)",
                "@nyoquira82: ретвитнул @plushev: А вот "
                        + "такой полувнутрячок, для коллег и"
                        + "постоянных слушателей. "
                        + "Улыбчивый блондин рядом со мной - "
                        + "это… https://t.co/f74eHz82i5"));
    }


}