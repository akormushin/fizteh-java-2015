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
    public void locationTest() {
        Location london = new Location(LATITUDE, LONGTITUDE, NAME);
        assertThat(london.getLatitude(), is(LATITUDE));
        assertThat(london.getLongitude(), is(LONGTITUDE));
        assertThat(london.getName(), is(NAME));
    }

    @Test
    public void locationTestNullName() {
        Location london = new Location(LATITUDE, LONGTITUDE);
        assertThat(london.getLatitude(), is(LATITUDE));
        assertThat(london.getLongitude(), is(LONGTITUDE));
        assertThat(london.getName(), is(nullValue()));
    }
}
