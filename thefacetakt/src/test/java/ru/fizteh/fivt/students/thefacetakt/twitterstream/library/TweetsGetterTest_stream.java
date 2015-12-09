package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import com.beust.jcommander.JCommander;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.*;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.exceptions.InvalidLocationException;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
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
    private ru.fizteh.fivt.students.thefacetakt.twitterstream.library.Location MoscowLocation = new ru.fizteh.fivt.students.thefacetakt.twitterstream.library.Location(55.7500, 37.6167, "Moscow");
    private int notMoscowTweets = 7;

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
                            return new ru.fizteh.fivt.students.thefacetakt.twitterstream.library.Location(58.0000, 56.3167, "Пермь");
                        case "Омск":
                            return new ru.fizteh.fivt.students.thefacetakt.twitterstream.library.Location(54.9833, 73.3667, "Омск");
                        case "Красноярск":
                            return new ru.fizteh.fivt.students.thefacetakt.twitterstream.library.Location(56.0167, 93.0667, "Красноярск");
                        default:
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
        assertThat(tweets, hasItems(
                "@luba_keks: ретвитнул @mr_twittcorn: Я НЕ НАВИЖУ ЭТОТ МИР",
                "@Owl_Juliann_: А мы сегодня с Викой добрались наконец-то до"
                        + " чайных дел мастерской! На удивительной "
                        + "фарворовой… https://t.co/nenvrVtV0o",
                "@MashaRaif: Я уверена, он(а) делает"
                        + " что-то для вас, потому что это выгодно ему(ей)"));
        assertThat(tweets, not(hasItems("@paorarti88: ретвитнул @malahovajenia: "
                + "Почему категорически не хочется вставать ,когда "
                + "надо. А когда некуда спешить,глаза сами открываются"
                + " в… https://t.co/XbezLDXwpE")));
    }

    @Test
    public void testGetTwitterNoRTStream() throws Exception {
        ArrayList<String> tweets = new ArrayList<>();
        TweetsGetter tweetsGetter = new TweetsGetter(geoResolver);
        tweetsGetter.getTwitterStream(setUpJCommanderSettings("-q", "а", "-p",
                "Moscow", "-s" , "--hideRetweets"),
                MoscowLocation, tweets::add, twitterStream);
        assertThat(tweets.size(), is(91));
        assertThat(tweets, hasItems(
                "@Owl_Juliann_: А мы сегодня с Викой добрались наконец-то до"
                        + " чайных дел мастерской! На удивительной "
                        + "фарворовой… https://t.co/nenvrVtV0o",
                "@MashaRaif: Я уверена, он(а) делает"
                        + " что-то для вас, потому что это выгодно ему(ей)"));
        assertThat(tweets, not(hasItems(
                "@luba_keks: ретвитнул @mr_twittcorn: Я НЕ НАВИЖУ ЭТОТ МИР",
                "@paorarti88: ретвитнул @malahovajenia: "
                + "Почему категорически не хочется вставать ,когда "
                + "надо. А когда некуда спешить,глаза сами открываются"
                + " в… https://t.co/XbezLDXwpE")));
    }


}