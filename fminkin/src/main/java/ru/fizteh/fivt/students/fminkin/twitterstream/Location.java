package ru.fizteh.fivt.students.fminkin.twitterstream;

/**
 * Created by Федор on 24.09.2015.
*/
public class Location {
    private double latitude;
    private double longitude;
    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
    Location(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }
    Location() {
    }
}
