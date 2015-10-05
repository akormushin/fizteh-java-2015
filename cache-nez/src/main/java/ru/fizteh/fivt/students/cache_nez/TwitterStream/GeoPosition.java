package ru.fizteh.fivt.students.cache_nez.TwitterStream;

/**
 * Created by cache-nez on 9/29/15.
 */
public class
        GeoPosition {
    private double latitude;
    private double longitude;

    public GeoPosition() {
        latitude = 0;
        longitude = 0;
    }

    public GeoPosition(double latitudeParameter, double longitudeParameter) {
        latitude = latitudeParameter;
        longitude = longitudeParameter;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
