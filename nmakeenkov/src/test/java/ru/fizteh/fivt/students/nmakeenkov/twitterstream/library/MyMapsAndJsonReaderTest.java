package ru.fizteh.fivt.students.nmakeenkov.twitterstream.library;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.fizteh.fivt.students.nmakeenkov.twitterstream.TwitterStream;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.stub;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JsonReader.class })
public class MyMapsAndJsonReaderTest {
    private static MyMaps myMaps = new MyMaps();
    private static JsonReader jsonReader = new JsonReader();
    private static TwitterStream twitterStream = new TwitterStream();

    @Test
    public void testGetCoordsOfMoscowCity() throws JSONException, IOException {
        // If Moscow moved of API changed, I have to find it out by a failing test
        Assert.assertArrayEquals(new double[]{55.75582, 37.6173, 2112.06027},
                MyMaps.getCoordsByPlace("Moscow city"), 1e-5);
    }

    @Test(expected = IOException.class)
    public void testFailGettingCoords() throws Exception {
        mockStatic(JsonReader.class);
        when(JsonReader.read(any(String.class))).thenThrow(new IOException());
        MyMaps.getMyCoords();
    }
}