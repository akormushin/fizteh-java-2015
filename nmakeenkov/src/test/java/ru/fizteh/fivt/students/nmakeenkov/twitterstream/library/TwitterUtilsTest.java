package ru.fizteh.fivt.students.nmakeenkov.twitterstream.library;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.User;
import static org.junit.Assert.*;
import static org.junit.runner.Request.method;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static ru.fizteh.fivt.students.nmakeenkov.twitterstream.library.TwitterUtils.buildQuery;
import static ru.fizteh.fivt.students.nmakeenkov.twitterstream.library.TwitterUtils.buildTweet;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ MyMaps.class })
public class TwitterUtilsTest {
    private static TwitterUtils twitterUtils = new TwitterUtils();

    @Mock
    Status status;

    @Mock
    Status retweet;

    @Mock
    User user1;

    @Mock
    User user2;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(MyMaps.class);

        try {
            PowerMockito.when(MyMaps.getMyCoords()).thenReturn(new double[]{1, 2, 3});
            PowerMockito.when(MyMaps.getCoordsByPlace(any(String.class))).thenReturn(new double[]{2, 2, 8});
        } catch (Exception ex) {
            System.out.println("???: " + ex.getMessage());
        }

        doReturn("User1").when(user1).getName();
        doReturn("User2").when(user2).getName();
        doReturn(user1).when(status).getUser();
        doReturn(user2).when(retweet).getUser();
        doReturn(false).when(status).isRetweet();
        doReturn(true).when(retweet).isRetweet();
        doReturn(228).when(status).getRetweetCount();
        doReturn(status).when(retweet).getRetweetedStatus();
        doReturn("status").when(status).getText();
        doReturn("RT @User1: status").when(retweet).getText();
    }

    @Test
    public void testBuildQuery() throws Exception {
        CommandLineParametersParser.Parameters params = CommandLineParametersParser.
                parse(new String[]{"-q", "java", "--hideRetweets"});
        Query my = buildQuery(params);
        Assert.assertEquals(params.getQuery(), my.getQuery());
        Assert.assertEquals("1.0,2.0,3.0km", my.getGeocode());
    }

    @Test
    public void testBuildQuery2() throws Exception {
        CommandLineParametersParser.Parameters params = CommandLineParametersParser.
                parse(new String[]{"-q", "java", "-p", "LA"});
        Query my = buildQuery(params);
        Assert.assertEquals(params.getQuery(), my.getQuery());
        Assert.assertEquals("2.0,2.0,8.0km", my.getGeocode());
    }

    @Test
    public void testBuildTweet() {
        Assert.assertEquals("@User1: status", buildTweet(status, true));
        Assert.assertEquals("@User1: status (228 ретвитов)", buildTweet(status, false));
        Assert.assertEquals("@User2 ретвитнул @User1: status", buildTweet(retweet, true));
        Assert.assertEquals("@User2 ретвитнул @User1: status", buildTweet(retweet, false));
    }

}
