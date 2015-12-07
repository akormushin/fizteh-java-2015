package ru.fizteh.fivt.students.w4r10ck1337.moduletests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.fizteh.fivt.students.w4r10ck1337.moduletests.library.GeoApi;
import twitter4j.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ GeoApi.class, URL.class, InputStream.class, String.class })
public class GeoApiTest {
    @Mock
    URL url;

    @Mock
    InputStream is;

    @Mock
    Twitter twitter;

    @Mock
    ResponseList<Place> response;

    @Mock
    Place place = mock(Place.class);

    @Mock
    GeoLocation g = mock(GeoLocation.class);

    @Test(expected = MalformedURLException.class)
    public void testGetLocationByIPURLException() throws Exception {
        whenNew(URL.class).withParameterTypes(String.class).withArguments(anyString()).thenThrow(new MalformedURLException());

        GeoApi.getLocationByIP();
    }

    @Test(expected = IOException.class)
    public void testGetLocationByIPStreamException() throws Exception {
        doThrow(new IOException()).when(url).openStream();
        whenNew(URL.class).withParameterTypes(String.class).withArguments(anyString()).thenReturn(url);

        GeoApi.getLocationByIP();
    }

    @Test(expected = NumberFormatException.class)
    public void testGetLocationWrongLocation() throws Exception {
        when(url.openStream()).thenReturn(is);
        whenNew(URL.class).withParameterTypes(String.class).withArguments(anyString()).thenReturn(url);

        GeoApi.getLocationByIP();
    }

    @Test
    public void testGetLocationCorrectLocation() throws Exception {
        when(url.openStream()).thenReturn(is);
        whenNew(URL.class).withParameterTypes(String.class).withArguments(anyString()).thenReturn(url);

        stub(method(GeoApi.class, "parseLocation")).toReturn("55.9041,37.5606");

        double[] ans = GeoApi.getLocationByIP();
        assertEquals(55.9041, ans[0], 0.00001);
        assertEquals(37.5606, ans[1], 0.00001);
    }

    @Test(expected = Exception.class)
    public void testGetLocationByNameWrongName() throws Exception {
        when(response.size()).thenReturn(0);
        when(twitter.searchPlaces(any(GeoQuery.class))).thenReturn(response);

        GeoApi.getLocationByName("asd", twitter);
    }

    @Test
    public void testGetLocationByNameCorrectName() throws Exception {
        when(response.size()).thenReturn(1);
        when(response.get(0)).thenReturn(place);
        when(g.getLatitude()).thenReturn(1.0);
        when(g.getLongitude()).thenReturn(1.0);
        when(place.getBoundingBoxCoordinates()).thenReturn(new GeoLocation[][] {{g}});
        when(twitter.searchPlaces(any(GeoQuery.class))).thenReturn(response);

        double[] ans = GeoApi.getLocationByName("asd", twitter);
        assertEquals(1, ans[0], 0.00001);
        assertEquals(1, ans[1], 0.00001);
    }

    @Test(expected = TwitterException.class)
    public void testGetLocationByNameTwitterError() throws Exception {
        when(twitter.searchPlaces(any(GeoQuery.class))).thenThrow(new TwitterException(""));

        GeoApi.getLocationByName("asd", twitter);
    }
}
