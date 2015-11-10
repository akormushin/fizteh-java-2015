package ru.fizteh.fivt.students.zakharovas.twitterstream.library;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.*;

import java.io.InputStream;
import java.sql.Date;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TweetFormaterTest {

    private static List<Status> tweets;
    private static List<String> formatedTweets;
    private static List<String> formatedTweetsWithoutDate;
    private static List<DateTestData> dateTests;
    private static Clock testingCurrentTime;
    private static ZoneId testingZone;

    @BeforeClass
    public static void setUp() throws Exception {
        testingZone = ZoneId.of("GMT+0");
        LocalDateTime testingTime = LocalDateTime.of(2015, Month.NOVEMBER, 4, 17, 30, 0);
        testingCurrentTime = Clock.fixed(testingTime.atZone(testingZone).toInstant(), testingZone);
        tweets = Twitter4jTestUtils.tweetsFromJson("/TweetFormaterTestData.json");
        setUpTestData();
    }

    private static void setUpTestData() throws Exception {
        formatedTweets = new ArrayList<>();
        formatedTweetsWithoutDate = new ArrayList<>();
        dateTests = new ArrayList<>();
        try (InputStream inputStream = TweetFormaterTest.class.getResourceAsStream("/TweetFormaterTestData.json")) {
            JSONObject answersJSON = new JSONObject(IOUtils.toString(inputStream));
            setTweetsWithDate(answersJSON);
            setTweetsWithoutDate(answersJSON);
            setUpDateTests(answersJSON);
        }
    }

    private static void setUpDateTests(JSONObject answersJSON) throws JSONException {
        JSONArray jsonDates = answersJSON.getJSONArray("dates");
        for (int i = 0; i < jsonDates.length(); ++i) {
            JSONObject currentTest = jsonDates.getJSONObject(i);
            Instant testingCurrentTIme = Instant.parse(currentTest.getString("currentTime"));
            Instant tweetTime = Instant.parse(currentTest.getString("tweetTime"));
            String answer = currentTest.getString("answer");
            dateTests.add(new DateTestData(testingCurrentTIme, tweetTime, answer));
        }
    }

    private static void setTweetsWithoutDate(JSONObject answersJSON) throws JSONException {
        JSONArray answerArrayWithoutDate = answersJSON.getJSONArray("answersWithoutDate");
        for (int i = 0; i < answerArrayWithoutDate.length(); ++i) {
            formatedTweetsWithoutDate.add(answerArrayWithoutDate.getString(i));
        }
    }

    private static void setTweetsWithDate(JSONObject answersJSON) throws JSONException {
        JSONArray answerArray = answersJSON.getJSONArray("answers");
        for (int i = 0; i < answerArray.length(); ++i) {
            formatedTweets.add(answerArray.getString(i));
        }
    }

    @Test
    public void testTweetForOutputWithoutDate() {
        testTweetWithoutDate(0, 3);
    }

    @Test
    public void testTweetForOutput() {
        testTweet(0, 3);
    }

    @Test
    public void testDateFormating() {
        Status tweet = mock(Status.class);
        for (DateTestData test : dateTests) {
            when(tweet.getCreatedAt()).thenReturn(Date.from(test.tweetTime));
            Clock clock = Clock.fixed(test.testingCurrentTime, testingZone);
            assertThat(new TweetFormater(tweet, clock).dateFormater(), is(test.answer));
        }
    }

    private void testTweetWithoutDate(int start, int end) {
        for (int i = start; i < end; ++i) {
            assertThat(new TweetFormater(tweets.get(i)).tweetForOutputWithoutDate(), is(formatedTweetsWithoutDate.get(i)));
        }
    }

    private void testTweet(int start, int end) {


        for (int i = start; i < end; ++i) {
            assertThat(new TweetFormater(tweets.get(i), testingCurrentTime).tweetForOutput(), is(formatedTweets.get(i)));
        }
    }


}

class DateTestData {
    Instant testingCurrentTime;
    Instant tweetTime;
    String answer;

    public DateTestData(Instant testingCurrentTime, Instant tweetTime, String answer) {
        this.testingCurrentTime = testingCurrentTime;
        this.tweetTime = tweetTime;
        this.answer = answer;
    }
}