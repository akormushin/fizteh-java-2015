package ru.fizteh.fivt.students.fminkin.twitterstream.library;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import ru.fizteh.fivt.students.fminkin.twitterstream.TwitterStream;

/**
 * Created by Федор on 15.12.2015.
 */


public class GeoLocationTestLocal {
    private static org.json.JSONObject ipInfoResponse;
    private static org.json.JSONObject ipInfoResponseBad;

    @BeforeClass
    public static void loadSamples() throws Exception {
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/ipLocal.json")) {
            ipInfoResponse = new org.json.JSONObject(IOUtils.toString(inputStream));
        }
    }

    @Test
    public void testGeoLocationLocal() throws Exception {
        JSonReader j = mock(JSonReader.class);
        when(j.readJsonFromUrl("http://ipinfo.io/json")).thenReturn(ipInfoResponse);
        Location loc = GeoLocation.getCurrentLocation(j);
        assertThat(loc.getLatitude(), is(55.9041));
        assertThat(loc.getLongitude(), is(37.5606));
        //assertThat(loc.getName(), is("Dolgoprudnyy"));
    }

    @Test(expected = JSONException.class)
    public void testGetLocationBadJSon() throws Exception {
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/ipLocalMistake.json")) {
            ipInfoResponseBad = new org.json.JSONObject(IOUtils.toString(inputStream));
        }
        JSonReader j = mock(JSonReader.class);
        when(j.readJsonFromUrl("http://ipinfo.io/json")).thenReturn(ipInfoResponseBad);
        Location loc = GeoLocation.getCurrentLocation(j);
    }

    @Test(expected = IOException.class)
    public void testGetLocationBadURL() throws Exception {
        JSonReader j = mock(JSonReader.class);
        when(j.readJsonFromUrl("http://ipinfo.io/json")).thenThrow(new IOException());
        Location loc = GeoLocation.getCurrentLocation(j);
    }
}
