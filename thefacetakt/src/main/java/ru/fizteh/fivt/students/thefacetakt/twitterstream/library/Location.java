package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

/**
 * Created by thefacetakt on 23.09.15.
 */
public final class Location {
    private final double latitude;
    private final double longitude;
    private final String name;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    Location(double newLatitude, double newLongitude) {
        this.latitude = newLatitude;
        this.longitude = newLongitude;
        this.name = null;
    }

    Location(double newLatitude, double newLongitude, String newName) {
        this.latitude = newLatitude;
        this.longitude = newLongitude;
        this.name = newName;
    }
}
