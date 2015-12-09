package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.TwitterStream;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.exceptions.InvalidLocationException;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.exceptions.LocationDefinitionErrorException;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.exceptions.QueryLimitException;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by thefacetakt on 12.10.15.
 */
public class PlaceLocationResolverTest_google {
    private static String normalGoogleResponce;
    private static String badKeyGoogleResponce;
    private static String noResultsGoogleResponce;
    private static String queryLimitGoogleResponce;

    @BeforeClass
    public static void loadSamples() throws Exception {
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/googleResponce.json")) {
            normalGoogleResponce = IOUtils.toString(inputStream);
        }
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/googleBadKey.json")) {
            badKeyGoogleResponce = IOUtils.toString(inputStream);
        }
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/googleZeroResults.json")) {
            noResultsGoogleResponce = IOUtils.toString(inputStream);
        }
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/googleQueryLimit.json")) {
            queryLimitGoogleResponce = IOUtils.toString(inputStream);
        }
    }

    @Test
    public void testResolvePlaceLocationGoogle() throws Exception {
        HttpReader httpReader = mock(HttpReader.class);
        when(httpReader.httpGet(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith("https"
                    + "://maps.googleapis.com"
                    + "/maps/api/geocode/json?"
                    + "address=Москва&key="));
            return normalGoogleResponce;
        });
        Location resolvedLocation = new PlaceLocationResolver(httpReader)
                .resolvePlaceLocationGoogle("Москва");
        assertThat(resolvedLocation.getLatitude(), is(55.755826));
        assertThat(resolvedLocation.getLongitude(), is(37.6173));
        assertThat(resolvedLocation.getName(), is("Москва"));
    }

    @Test(expected = LocationDefinitionErrorException.class)
    public void testResolvePlaceLocationGoogleBadSyntax() throws Exception {
        HttpReader httpReader = mock(HttpReader.class);
        when(httpReader.httpGet(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith("https"
                    + "://maps.googleapis.com"
                    + "/maps/api/geocode/json?"
                    + "address=Москва&key="));
            return "Just trash instead of JSON";
        });
        new PlaceLocationResolver(httpReader)
                .resolvePlaceLocationGoogle("Москва");
    }

    @Test(expected = InvalidLocationException.class)
    public void testResolvePlaceLocationGoogleNoResults() throws Exception {
        HttpReader httpReader = mock(HttpReader.class);
        when(httpReader.httpGet(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith("https"
                    + "://maps.googleapis.com"
                    + "/maps/api/geocode/json?"
                    + "address=Москва&key="));
            return noResultsGoogleResponce;
        });
        new PlaceLocationResolver(httpReader)
                .resolvePlaceLocationGoogle("Москва");
    }

    @Test(expected = QueryLimitException.class)
    public void testResolvePlaceLocationGoogleQueryLimit() throws Exception {
        HttpReader httpReader = mock(HttpReader.class);
        when(httpReader.httpGet(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith("https"
                    + "://maps.googleapis.com"
                    + "/maps/api/geocode/json?"
                    + "address=Москва&key="));
            return queryLimitGoogleResponce;
        });
        new PlaceLocationResolver(httpReader)
                .resolvePlaceLocationGoogle("Москва");
    }

    @Test(expected = LocationDefinitionErrorException.class)
    public void testResolvePlaceLocationGoogleBadKey() throws Exception {
        HttpReader httpReader = mock(HttpReader.class);
        when(httpReader.httpGet(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith("https"
                    + "://maps.googleapis.com"
                    + "/maps/api/geocode/json?"
                    + "address=Москва&key="));
            return badKeyGoogleResponce;
        });
        new PlaceLocationResolver(httpReader)
                .resolvePlaceLocationGoogle("Москва");

    }
}