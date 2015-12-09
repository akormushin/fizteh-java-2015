package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.TwitterStream;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.exceptions.InvalidLocationException;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.exceptions.LocationDefinitionErrorException;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by thefacetakt on 12.10.15.
 */
public class PlaceLocationResolverTest_yandex {

    private static String normalYandexResponce;
    private static String zeroYandexResponce;
    private static String badKeyYandexResponce;

    @BeforeClass
    public static void loadSamples() throws Exception {
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/yandexResponce.json")) {
            normalYandexResponce = IOUtils.toString(inputStream);
        }
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/yandexZeroResults.json")) {
            zeroYandexResponce = IOUtils.toString(inputStream);
        }
        try (InputStream inputStream
                     = TwitterStream.class
                .getResourceAsStream("/yandexBadKey.json")) {
            badKeyYandexResponce = IOUtils.toString(inputStream);
        }
    }

    @Test
    public void testResolvePlaceLocationYandex() throws Exception {
        HttpReader httpReader = mock(HttpReader.class);
        when(httpReader.httpGet(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith(
                "https://geocode-maps.yandex.ru/1.x/?format=json&"
                        + "geocode=Москва&key="
            ));
            return normalYandexResponce;
        });

        Location resolvedLocation = new PlaceLocationResolver(httpReader)
                .resolvePlaceLocationYandex("Москва");
        assertThat(resolvedLocation.getLatitude(), is(55.753960));
        assertThat(resolvedLocation.getLongitude(), is(37.620393));
        assertThat(resolvedLocation.getName(), is("Москва"));
    }

    @Test(expected = LocationDefinitionErrorException.class)
    public void testResolvePlaceLocationYandexBadSyntax() throws Exception {
        HttpReader httpReader = mock(HttpReader.class);
        when(httpReader.httpGet(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith(
                    "https://geocode-maps.yandex.ru/1.x/?format=json&"
                            + "geocode=Москва&key="
            ));
            return "The phantom of the opera is there inside my mind";
        });

        new PlaceLocationResolver(httpReader)
                .resolvePlaceLocationYandex("Москва");
    }

    @Test(expected = LocationDefinitionErrorException.class)
    public void testResolvePlaceLocationYandexBadConnection() throws Exception {
        HttpReader httpReader = mock(HttpReader.class);
        when(httpReader.httpGet(anyString()))
                .thenThrow(new IllegalStateException());
        new PlaceLocationResolver(httpReader)
                .resolvePlaceLocationYandex("Москва");
    }

    @Test(expected = InvalidLocationException.class)
    public void testResolvePlaceLocationYandexBadLocation() throws Exception {
        HttpReader httpReader = mock(HttpReader.class);
        when(httpReader.httpGet(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith(
                    "https://geocode-maps.yandex.ru/1.x/?format=json&"
                            + "geocode=фовпр&key="
            ));
            return zeroYandexResponce;
        });
        new PlaceLocationResolver(httpReader)
                .resolvePlaceLocationYandex("фовпр");
    }

    @Test(expected = LocationDefinitionErrorException.class)
    public void testResolvePlaceLocationYandexBadKey() throws Exception {
        HttpReader httpReader = mock(HttpReader.class);
        when(httpReader.httpGet(anyString())).then(invocation -> {
            assertThat((String) invocation.getArguments()[0], startsWith(
                    "https://geocode-maps.yandex.ru/1.x/?format=json&"
                            + "geocode=Москва&key="
            ));
            return badKeyYandexResponce;
        });
        new PlaceLocationResolver(httpReader)
                .resolvePlaceLocationYandex("Москва");
    }
}