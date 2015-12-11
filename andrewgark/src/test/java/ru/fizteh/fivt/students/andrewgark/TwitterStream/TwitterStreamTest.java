package ru.fizteh.fivt.students.andrewgark.TwitterStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import twitter4j.Status;
import twitter4j.User;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TwitterStream.class)
public class TwitterStreamTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static final String SEPARATOR =
            "----------------------------------------------------------------------------------------";

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void testPrintTweet() throws Exception {
        Status tweet1 = mock(Status.class);
        User user1 = mock(User.class);
        when(tweet1.isRetweet()).thenReturn(Boolean.TRUE);
        when(tweet1.getUser()).thenReturn(user1);
        when(user1.getScreenName()).thenReturn("12thDoctor");
        when(tweet1.getText()).thenReturn("RT @10thDoctor: Allons-y! Shut up. Shut shuttity up up, shut up.");
        when(tweet1.getRetweetCount()).thenReturn(12);
        when(tweet1.getCreatedAt()).thenReturn(new Date(115, 9, 10, 13, 59, 59));

        Status tweet2 = mock(Status.class);
        User user2 = mock(User.class);
        when(tweet2.isRetweet()).thenReturn(Boolean.TRUE);
        when(tweet2.getUser()).thenReturn(user2);
        when(user2.getScreenName()).thenReturn("Missy");
        when(tweet2.getText()).thenReturn("RT @Master: You will obey me. Knock-knock-knock-knock.");
        when(tweet2.getRetweetCount()).thenReturn(0);
        when(tweet2.getCreatedAt()).thenReturn(new Date(115, 9, 10, 12, 0, 0));

        Status tweet3 = mock(Status.class);
        User user3 = mock(User.class);
        when(tweet3.isRetweet()).thenReturn(Boolean.FALSE);
        when(tweet3.getUser()).thenReturn(user3);
        when(user3.getScreenName()).thenReturn("Clara");
        when(tweet3.getText()).thenReturn("I don't know where I am...");
        when(tweet3.getRetweetCount()).thenReturn(1);
        when(tweet3.getCreatedAt()).thenReturn(new Date(115, 9, 10, 13, 40, 0));

        Status tweet4 = mock(Status.class);
        User user4 = mock(User.class);
        when(tweet4.isRetweet()).thenReturn(Boolean.FALSE);
        when(tweet4.getUser()).thenReturn(user4);
        when(user4.getScreenName()).thenReturn("Amy");
        when(tweet4.getText()).thenReturn("RT is not great, FIFT is better");
        when(tweet4.getRetweetCount()).thenReturn(0);
        when(tweet4.getCreatedAt()).thenReturn(new Date(115, 9, 5, 23, 50, 50));

        PowerMockito.stub(PowerMockito.method(LocalDateTime.class,
                "now")).toReturn(LocalDateTime.parse("2015-10-10T12:01:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        TwitterStream.printTweet(tweet1, Boolean.TRUE);
        TwitterStream.printTweet(tweet2, Boolean.FALSE);
        TwitterStream.printTweet(tweet3, Boolean.TRUE);
        TwitterStream.printTweet(tweet4, Boolean.FALSE);

        BufferedReader reader = new BufferedReader(new StringReader(outContent.toString()));

        Assert.assertEquals(SEPARATOR, reader.readLine());
        String answerTweet1 = "@12thDoctor: ретвитнул @10thDoctor: Allons-y! Shut up. Shut shuttity up up, shut up.";
        Assert.assertEquals(answerTweet1, reader.readLine());
        Assert.assertEquals(SEPARATOR, reader.readLine());
        String answerTweet2 ="[Только что] @Missy: ретвитнул @Master: You will obey me. Knock-knock-knock-knock.";
        Assert.assertEquals(answerTweet2, reader.readLine());
        Assert.assertEquals(SEPARATOR, reader.readLine());
        String answerTweet3 ="@Clara: I don't know where I am... (1 ретвит)";
        Assert.assertEquals(answerTweet3, reader.readLine());
        Assert.assertEquals(SEPARATOR, reader.readLine());
        String answerTweet4 ="[5 дней назад] @Amy: RT is not great, FIFT is better";
        Assert.assertEquals(answerTweet4, reader.readLine());
    }
}
