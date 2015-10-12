package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import com.bytebybyte.google.geocoding.service.IGeocodingService;
import com.bytebybyte.google.geocoding.service.request.GeocodeRequest;
import com.bytebybyte.google.geocoding.service.request.GeocodeRequestBuilder;
import com.bytebybyte.google.geocoding.service.response.LatLng;
import com.bytebybyte.google.geocoding.service.response.Result;
import com.bytebybyte.google.geocoding.service.standard.StandardGeocodingService;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class GeocodeWrapper {
    // That would be ~60km
    static final double NEARBY_THRESHOLD = 0.5;

    static Location getCoordinatesByIp() throws IOException, JSONException {
        URL url = new URL("http://ipinfo.io/json/");
        try (InputStream stream = url.openStream()) {
            JSONTokener tokenizer = new JSONTokener(stream);
            JSONObject root = new JSONObject(tokenizer);
            String[] coordinates = ((String) root.get("loc")).split(",");
            return new Location(Double.valueOf(coordinates[0]), Double.valueOf(coordinates[1]));
        }
    }

    public static boolean isNearby(LatLng point1, LatLng point2) {
        return Math.hypot(point1.getLat() - point2.getLat(), point1.getLng() - point2.getLng()) < NEARBY_THRESHOLD;
    }

    public static LatLng getCoordinatesByString(String place) {
        if ("nearby".equals(place)) {
            try {
                return getCoordinatesByIp();
            } catch (IOException | JSONException e) {
                throw new IllegalArgumentException("Can't find location for ip " + place, e);
            }
        } else {
            IGeocodingService geocoding = new StandardGeocodingService();
            GeocodeRequest request = new GeocodeRequestBuilder().output("json").address(place).build();
            Result[] response = geocoding.geocode(request).getResults();
            if (response.length == 0) {
                throw new IllegalArgumentException("No such place as " + place);
            }
            return response[0].getGeometry().getLocation();
        }
    }

    public static class Location extends LatLng {
        public Location(Double lat, Double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }
}
