package ru.fizteh.fivt.students.w4r10ck1337.moduletests;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.fizteh.fivt.students.w4r10ck1337.moduletests.library.TweetBuilder;
import twitter4j.Status;
import twitter4j.User;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@RunWith(PowerMockRunner.class)
public class TweetBuilderTest {
    @Mock
    Status status;

    @Mock
    User user;

    @Test
    public void testTweet() {
        doReturn("Вася Пупкин").when(user).getScreenName();
        doReturn(false).when(status).isRetweet();
        doReturn(user).when(status).getUser();
        doReturn("ололо").when(status).getText();
        doReturn(false).when(status).isRetweeted();
        Assert.assertEquals("@Вася Пупкин: ололо", TweetBuilder.formatTweet(status));

        doReturn(true).when(status).isRetweeted();
        doReturn(1488).when(status).getRetweetCount();
        assertEquals("@Вася Пупкин: ололо (1488 ретвитов)", TweetBuilder.formatTweet(status));

        doReturn(true).when(status).isRetweet();
        doReturn("RT @Коля Пупкин: азаза").when(status).getText();
        assertEquals("@Вася Пупкин ретвитнул @Коля Пупкин: азаза", TweetBuilder.formatTweet(status));
    }
}
