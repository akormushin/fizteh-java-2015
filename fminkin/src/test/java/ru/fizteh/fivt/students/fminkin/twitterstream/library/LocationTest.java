package ru.fizteh.fivt.students.fminkin.twitterstream.library;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by Федор on 14.12.15.
 */
public class LocationTest {


    static final double LATITUDE = -0.12750;
    static final double LONGTITUDE = 51.50722;
    static final String NAME = "Лондон";

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