package ru.fizteh.fivt.students.ocksumoron.twitterstream;

import twitter4j.GeoLocation;

/**
 * Created by ocksumoron on 28.09.15.
 */
public class Location {
    private double res;
    private double latitudeCenter;
    private double longitudeCenter;
    private double latitudeSWCorner;
    private double longitudeSWCorner;
    private double latitudeNECorner;
    private double longitudeNECorner;

    public Location(double latitudeCenter, double longitudeCenter,
                     double latitudeSWCorner, double longitudeSWCorner,
                     double latitudeNECorner, double longitudeNECorner, double res) {
        this.latitudeCenter = latitudeCenter;
        this.longitudeCenter = longitudeCenter;
        this.latitudeSWCorner = latitudeSWCorner;
        this.longitudeSWCorner = longitudeSWCorner;
        this.latitudeNECorner = latitudeNECorner;
        this.longitudeNECorner = longitudeNECorner;
        this.res = res;
    }

    public Location(GeoLocation center, GeoLocation cornerSW, GeoLocation cornerNE, double res) {
        this.latitudeCenter = center.getLatitude();
        this.longitudeCenter = center.getLongitude();
        this.latitudeSWCorner = cornerSW.getLatitude();
        this.longitudeSWCorner = cornerSW.getLongitude();
        this.latitudeNECorner = cornerNE.getLatitude();
        this.longitudeNECorner = cornerNE.getLongitude();
        this.res = res;
    }

    public GeoLocation getGeoLocation() {
        return new GeoLocation(latitudeCenter, longitudeCenter);
    }

    public double getRes() {
        return res;
    }

    public double getLatitudeCenter() {
        return latitudeCenter;
    }

    public double getLongitudeCenter() {
        return longitudeCenter;
    }

    public double getLatitudeSWCorner() {
        return latitudeSWCorner;
    }

    public double getLongitudeSWCorner() {
        return longitudeSWCorner;
    }

    public double getLatitudeNECorner() {
        return latitudeNECorner;
    }

    public double getLongitudeNECorner() {
        return longitudeNECorner;
    }
}
