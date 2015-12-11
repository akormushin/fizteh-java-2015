package ru.fizteh.fivt.students.oshch.moduletests;

import com.beust.jcommander.JCommander;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.oshch.moduletests.library.Parameters;
import ru.fizteh.fivt.students.oshch.moduletests.library.StreamTweets;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StreamTweetsTest {
    @Mock
    twitter4j.TwitterStream twitterStream;

    @InjectMocks
    StreamTweets stream;

    public static List<Status> statuses;

    @BeforeClass
    public static void loadSampleData() {
        statuses = Twitter4jTestUtils.tweetsFromJson("/statuses.json");
    }

    @Test
    public void streamResultTest() throws Exception {
        ArgumentCaptor<StatusListener> statusListener = ArgumentCaptor.forClass(StatusListener.class);
        doNothing().when(twitterStream).addListener((StatusListener) statusListener.capture());
        doAnswer(i -> {
            statuses.forEach(s -> statusListener.getValue().onStatus(s));
            return null;
        }).when(twitterStream).filter(any(FilterQuery.class));

        List<Status> tweets = new ArrayList<>();

        Parameters param = new Parameters();
        String[] args = {"-q", "mipt", "-s", "-l", "100", "-p", ""};
        JCommander cmd = new JCommander(param, args);

        stream.stream(param, tweets::add);

        assertTrue(tweets.size() == 13);

        verify(twitterStream).addListener((StatusListener) any(StatusAdapter.class));
        verify(twitterStream).filter(any(FilterQuery.class));
    }
}