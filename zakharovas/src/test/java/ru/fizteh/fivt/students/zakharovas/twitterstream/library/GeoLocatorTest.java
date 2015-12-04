package ru.fizteh.fivt.students.zakharovas.twitterstream.library;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.maps.model.GeocodingResult;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.zakharovas.twitterstream.library.exceptions.GeoSearchException;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by alexander on 05.11.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class GeoLocatorTest {

    private static List<GeoLocatorTestData> tests;


    @BeforeClass
    public static void setUp() throws Exception {
        tests = new ArrayList<>();
        Type type = new TypeToken<GeoLocatorTestData>() {
        }.getType();
        try (InputStream inputStream = GeoLocatorTest.class.getResourceAsStream("/GeoLocatorTests.json")) {
            String json = IOUtils.toString(inputStream);
            JsonParser parser = new JsonParser();
            JsonArray testsInJSON = (JsonArray) parser.parse(json);
            for (JsonElement test : testsInJSON) {
                tests.add(new Gson().fromJson(test, type));
            }
        }
    }

    @Test(expected = GeoSearchException.class)
    public void testEmptyArguments() throws GeoSearchException {
        GeocodingResult[] geocodingResults = {};
        new GeoLocator(geocodingResults);
    }

    @Test(expected = GeoSearchException.class)
    public void testNullArguments() throws GeoSearchException {
        GeocodingResult[] geocodingResults = {null};
        new GeoLocator(geocodingResults);
    }

    @Test
    public void testGetRadius() throws GeoSearchException {
        for (GeoLocatorTestData test : tests) {
            assertThat(new GeoLocator(test.results).getRadius(), is(test.radius));
        }
    }

    @Test
    public void TestGetLocationForStream() throws GeoSearchException {
        for (GeoLocatorTestData test : tests) {
            assertThat(new GeoLocator(test.results).getLocationForStream(), is(test.coordinatesForStream));
        }
    }

    @Test
    public void TesTGetLocationForSearch() throws GeoSearchException {
        for (GeoLocatorTestData test : tests) {
            assertThat(new GeoLocator(test.results).getLocationForSearch().getLatitude(), is(test.coordinatesForSearch[0]));
            assertThat(new GeoLocator(test.results).getLocationForSearch().getLongitude(), is(test.coordinatesForSearch[1]));
        }
    }
}

class GeoLocatorTestData {
    GeocodingResult results[];
    double radius;
    double[] coordinatesForSearch;
    double[][] coordinatesForStream;

    public GeoLocatorTestData() {
    }
}
