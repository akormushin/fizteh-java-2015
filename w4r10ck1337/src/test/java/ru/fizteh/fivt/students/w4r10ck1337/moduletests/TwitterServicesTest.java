package ru.fizteh.fivt.students.w4r10ck1337.moduletests;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.fizteh.fivt.students.w4r10ck1337.moduletests.library.QueryCreator;
import ru.fizteh.fivt.students.w4r10ck1337.moduletests.library.TwitterServices;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TwitterServices.class, QueryCreator.class, QueryResult.class})
public class TwitterServicesTest {
    @Mock
    Twitter twitter;

    List<Status> statuses;

    @Mock
    Query sq = new Query();

    @Before
    public void setUp() {
        stub(method(QueryCreator.class, "createQuery")).toReturn(sq);
        stub(method(QueryCreator.class, "findLocation")).toReturn(null);
        statuses = Twitter4jTestUtils.tweetsFromJson("/statuses.json");
    }

    @Test
    @Ignore
    public void testPrintTweets() throws Exception {
        List<Status> response = new ArrayList<>();
        
        stub(method(TwitterServices.class, "getTweets")).toReturn(statuses);
        TwitterServices.printTweets("", "", null, 100, response::add);
        assertEquals(statuses, response);
    }


    @Test
    @Ignore
    public void testStreamTweets() throws Exception {
        List<Status> response = new ArrayList<>();
        List<List<Status>> answer = new ArrayList<>();
        for (int i = 0; i < statuses.size(); i++) {
            List<Status> t = new ArrayList<>();
            t.add(statuses.get(i));
            answer.add(t);
        }

        spy(TwitterServices.class);
        doAnswer(AdditionalAnswers.returnsElementsOf(answer)).when(TwitterServices.class, "getTweets", any(Twitter.class), any(Query.class), anyInt());
        TwitterServices.streamTweets("", "", null, 10, response::add);
        assertEquals(statuses.subList(0, 10), response);
    }

    @Test
    @Ignore
    public void testGetTweets() throws Exception {
        QueryResult q = mock(QueryResult.class);
        when(twitter.search(any(Query.class))).thenThrow(new TwitterException("")).thenReturn(q);
        List<Status> tweets = new ArrayList<>();
        when(q.getTweets()).thenReturn(tweets);
        assertEquals(tweets, TwitterServices.getTweets(twitter, null, 0));
    }
}

