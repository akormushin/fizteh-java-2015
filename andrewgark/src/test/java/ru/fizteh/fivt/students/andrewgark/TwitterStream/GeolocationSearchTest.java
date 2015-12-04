package ru.fizteh.fivt.students.andrewgark.TwitterStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.fizteh.fivt.students.andrewgark.TwitterStream.GeolocationSearch;
import ru.fizteh.fivt.students.andrewgark.TwitterStream.URLs;

import static org.mockito.Matchers.any;

@RunWith(PowerMockRunner.class)
@PrepareForTest({URLs.class, GeolocationSearch.class})
public class GeolocationSearchTest {
    @Test
    public void testGetCoordinatesByIp() throws Exception {
        PowerMockito.mockStatic(URLs.class);
        PowerMockito.when(URLs.getUrl(any(String.class))).thenReturn("LatLng(15.43, -20.07);");
        Double[] coordinates = GeolocationSearch.getCoordinatesByIp();
        Assert.assertEquals(coordinates[0], -20.07, 1E-3);
        Assert.assertEquals(coordinates[1], 15.43, 1E-3);
    }

    @Test (expected = GeolocationSearch.SearchLocationException.class)
    public void testGetCoordinatesByIpError() throws Exception {
        PowerMockito.mockStatic(URLs.class);
        PowerMockito.when(URLs.getUrl(any(String.class))).thenReturn("");
        GeolocationSearch.getCoordinatesByIp();
    }

    @Test
    public void testGetCoordinatesByQuery() throws Exception {
        PowerMockito.mockStatic(URLs.class);
        String location = "Vladivostok";
        String xml = "<pos>-1.79 21.8</pos>";
        String yandexKey = "1d2451AAAA-4fsdf13fwe-f013-fds==";
        PowerMockito.stub(PowerMockito.method(GeolocationSearch.class,
                "readKeyFromResource", String.class)).toReturn(yandexKey);
        PowerMockito.when(URLs.getUrl(any(String.class))).thenReturn(xml);
        Double[] coordinates = GeolocationSearch.getCoordinatesByQuery(location);
        Assert.assertEquals(coordinates[0], 21.8, 1E-3);
        Assert.assertEquals(coordinates[1], -1.79, 1E-3);
    }

    @Test (expected = GeolocationSearch.SearchLocationException.class)
    public void testGetCoordinatesByQueryError() throws Exception {
        PowerMockito.mockStatic(URLs.class);
        String location = "Vladivostok";
        String xml = "";
        String yandexKey = "1d2451AAAA-4fsdf13fwe-f013-fds==";
        PowerMockito.stub(PowerMockito.method(GeolocationSearch.class,
                "readKeyFromResource", String.class)).toReturn(yandexKey);
        PowerMockito.when(URLs.getUrl(any(String.class))).thenReturn(xml);
        GeolocationSearch.getCoordinatesByQuery(location);
    }
}
