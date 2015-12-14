package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.TwitterStream;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.exceptions.LocationDefinitionErrorException;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by thefacetakt on 12.10.15.
 */
public class PlaceLocationResolverTest_current {
    private static String ipInfoResponce;
    private static String ipInfoResponceBad;

    @BeforeClass
    public static void loadSamples() throws Exception {
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/ipInfoSample.json")) {
            ipInfoResponce = IOUtils.toString(inputStream);
        }
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/ipInfoSampleBadSyntax.json")) {
            ipInfoResponceBad = IOUtils.toString(inputStream);
        }
    }

    @Test
    public void testResolveCurrentLocation() throws Exception {
        HttpReader httpReader = mock(HttpReader.class);
        when(httpReader.httpGet("http://ipinfo.io/json"))
                .thenReturn(ipInfoResponce);
        PlaceLocationResolver geoResolver
                = new PlaceLocationResolver(httpReader);
        Location resolvedLocation = geoResolver.resolveCurrentLocation();

        assertThat(resolvedLocation.getLatitude(), is(55.9041));
        assertThat(resolvedLocation.getLongitude(), is(37.5606));
        assertThat(resolvedLocation.getName(), is("Dolgoprudnyy"));
    }

    @Test(expected = LocationDefinitionErrorException.class)
    public void testResolveCurrentLocationBadSyntax() throws Exception {
        HttpReader httpReader = mock(HttpReader.class);
        when(httpReader.httpGet("http://ipinfo.io/json"))
                .thenReturn(ipInfoResponceBad);
        PlaceLocationResolver geoResolver
                = new PlaceLocationResolver(httpReader);
        geoResolver.resolveCurrentLocation();
    }

    @Test(expected = LocationDefinitionErrorException.class)
    public void testResolveCurrentLocationBadConnection() throws Exception {
        HttpReader httpReader = mock(HttpReader.class);
        when(httpReader.httpGet("http://ipinfo.io/json"))
                .thenThrow(new IllegalStateException());
        PlaceLocationResolver geoResolver
                = new PlaceLocationResolver(httpReader);
        geoResolver.resolveCurrentLocation();
    }
}