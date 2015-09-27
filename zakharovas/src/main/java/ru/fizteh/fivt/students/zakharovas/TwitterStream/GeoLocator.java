package ru.fizteh.fivt.students.zakharovas.TwitterStream;


import twitter4j.GeoLocation;

import java.util.List;

public class GeoLocator {

    private String location;
    private double radius = 0;

    public GeoLocator(List<String> locationInList) {
        location = String.join(" ", locationInList);
        if (location.isEmpty()) {
            location = "nearby";
        }
        setCoordinates();
    }

    private void setCoordinates() {
        if (location.equals("nearby")) {
            return;
        }

    }

    public double[] getLocationForStream() {
        return new double[] {0, 0};
    }

    public GeoLocation getLocationForSearch() {
        return /*new GeoLocation();*/ null;

    }

    public double getRadius() {
        return radius;
    }

}


