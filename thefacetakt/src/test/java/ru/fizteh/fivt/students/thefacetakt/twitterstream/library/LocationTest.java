package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import org.junit.Test;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.Location;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by thefacetakt on 12.10.15.
 */
public class LocationTest {


    static final double LATITUDE = 55.7500;
    static final double LONGTITUDE = 37.6167;
    static final String NAME = "Moscow";

    @Test
    public void LocationTest() {
        Location Moscow = new Location(LATITUDE, LONGTITUDE, NAME);
        assertThat(Moscow.getLatitude(), is(LATITUDE));
        assertThat(Moscow.getLongitude(), is(LONGTITUDE));
        assertThat(Moscow.getName(), is(NAME));
    }

    @Test
    public void LocationTestNullName() {
        Location Moscow = new Location(LATITUDE, LONGTITUDE);
        assertThat(Moscow.getLatitude(), is(LATITUDE));
        assertThat(Moscow.getLongitude(), is(LONGTITUDE));
        assertThat(Moscow.getName(), is(nullValue()));
    }
}