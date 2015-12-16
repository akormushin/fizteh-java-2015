package ru.fizteh.fivt.students.fminkin.twitterstream.library;

/**
 * Created by Федор on 24.09.2015.
*/
public final class Location {
    private final double latitude;
    private final double longitude;
    private final String name;
    public double getLongitude() {
        return longitude;
    }
    public String getName() {
        return name;
    }
    public double getLatitude() {
        return latitude;
    }
    public Location(double lat, double lng) {
        latitude = lat;
        longitude = lng;
        name = null;
    }
    public Location(double lat, double lng, String n) {
        latitude = lat;
        longitude = lng;
        name = n;
    }
}
