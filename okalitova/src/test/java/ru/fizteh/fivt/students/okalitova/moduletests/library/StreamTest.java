package ru.fizteh.fivt.students.okalitova.moduletests.library;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * Created by nimloth on 10.11.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class StreamTest {

    @Mock
    TwitterStream twitterStream;

    @InjectMocks
    Stream stream;

    public static List<Status> statuses;

    @BeforeClass
    public static void loadSampleData() {
        statuses = Twitter4jTestUtils.tweetsFromJson("/statuses.json");
    }

    @Test
    public void streamResultTest() throws Exception {
        final String ansiReset = "\u001B[0m";
        final String ansiPurple =  "\u001b[35m";
        ArgumentCaptor<StatusListener> statusListener = ArgumentCaptor.forClass(StatusListener.class);
        doNothing().when(twitterStream).addListener((StatusListener) statusListener.capture());
        doAnswer(i -> {
            statuses.forEach(s -> statusListener.getValue().onStatus(s));
            return null;
        }).when(twitterStream).filter(any(FilterQuery.class));

        List<String> tweets = new ArrayList<>();

        ParametersParser param = new ParametersParser();
        param.setQuery("mipt");
        param.setStream(true);
        param.setLimit(100);
        param.setHideRetwitts(false);
        param.setHelp(false);
        param.setPlace("");

        stream.streamResult(param, tweets::add);

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

        verify(twitterStream).addListener((StatusListener) any(StatusAdapter.class));
        verify(twitterStream).filter(any(FilterQuery.class));
    }
}
