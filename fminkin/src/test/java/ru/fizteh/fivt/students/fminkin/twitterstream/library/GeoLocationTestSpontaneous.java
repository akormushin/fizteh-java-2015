package ru.fizteh.fivt.students.fminkin.twitterstream.library;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import ru.fizteh.fivt.students.fminkin.twitterstream.TwitterStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Федор on 15.12.2015.
 */



public class GeoLocationTestSpontaneous {
    private static org.json.JSONObject correctGoogleResponce;
    private static org.json.JSONObject badKeyGoogleResponce;
    private static org.json.JSONObject zeroResultsGoogleResponce;
    private static org.json.JSONObject queryLimitGoogleResponce;

    @BeforeClass
    public static void loadSamples() throws Exception {
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/correctGoogleResponse.json")) {
            correctGoogleResponce = new org.json.JSONObject(IOUtils.toString(inputStream));
        }
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/badKeyGoogleResponse.json")) {
            badKeyGoogleResponce = new org.json.JSONObject(IOUtils.toString(inputStream));
        }
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/zeroResultsGoogleResponse.json")) {
            zeroResultsGoogleResponce = new org.json.JSONObject(IOUtils.toString(inputStream));
        }
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/queryLimitGoogleResponse.json")) {
            queryLimitGoogleResponce = new org.json.JSONObject(IOUtils.toString(inputStream));
        }
    }

    @Test
    public void testGeoLocation() throws Exception {
        JSonReader j = mock(JSonReader.class);
        when(j.readJsonFromUrl(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith("https://maps.googleapis.com/maps"
                    + "/api/geocode/json?address=London&key="));
            return correctGoogleResponce;
        });
        GeoLocation g = new GeoLocation();
        Location loc = g.getLocationGoogle("London", j);
        assertThat(loc.getLatitude(), is(51.5073509));
        assertThat(loc.getLongitude(), is(-0.1277583));
    }

    @Test(expected = IOException.class)
    public void testGetLocationGoogleBadSyntax() throws Exception {
        JSonReader j = mock(JSonReader.class);
        when(j.readJsonFromUrl(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith("https://maps.googleapis.com/maps/api/geocode/"
                    + "json?address=Лондон&key="));
            return new JSONObject("bu");
        });
        GeoLocation g = new GeoLocation();
        Location loc = g.getLocationGoogle("Лондон", j);
    }

    @Test(expected = IOException.class)
    public void testResolvePlaceLocationGoogleNoResults() throws Exception {
        JSonReader j = mock(JSonReader.class);
        when(j.readJsonFromUrl(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith("https://maps.googleapis.com/maps/api/geocode/"
                    + "json?address=Лондон&key="));
            return zeroResultsGoogleResponce;
        });
        GeoLocation g = new GeoLocation();
        Location loc = g.getLocationGoogle("Лондон", j);
    }

    @Test(expected = IOException.class)
    public void testResolvePlaceLocationGoogleQueryLimit() throws Exception {
        JSonReader j = mock(JSonReader.class);
        when(j.readJsonFromUrl(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith("https://maps.googleapis.com/maps/api"
                    + "/geocode/json?address=Лондон&key="));
            return queryLimitGoogleResponce;
        });
        GeoLocation g = new GeoLocation();
        Location loc = g.getLocationGoogle("Лондон", j);
    }

    @Test(expected = IOException.class)
    public void testResolvePlaceLocationGoogleBadKey() throws Exception {
        JSonReader j = mock(JSonReader.class);
        when(j.readJsonFromUrl(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith("https://maps.googleapis.com/maps/api"
                    + "/geocode/json?address=Лондон&key="));
            return badKeyGoogleResponce;
        });
        GeoLocation g = new GeoLocation();
        Location loc = g.getLocationGoogle("Лондон", j);

    }
}
