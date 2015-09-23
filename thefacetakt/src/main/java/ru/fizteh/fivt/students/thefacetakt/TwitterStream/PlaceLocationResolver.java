package ru.fizteh.fivt.students.thefacetakt.TwitterStream;

import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thefacetakt on 23.09.15.
 */
class PlaceLocationResolver {
    static final String LOCATION_DEFINITION_ERROR
            = "Problem while location definition";

    private Map<String, Location> cache
            = new HashMap<String, Location>();
    private String googleMapsKey;
    private String yandexMapsKey;

    PlaceLocationResolver() {
        try (BufferedReader mapsKeyFile = new BufferedReader(
                new FileReader("src/main/resources/geo.properties"))) {
            googleMapsKey = mapsKeyFile.readLine();
            yandexMapsKey = mapsKeyFile.readLine();
        } catch (IOException e) {
            System.err.println("Something went terribly wrong: no maps "
                    + "key found");
            System.exit(1);
        }
    }


    private Location resolvePlaceLocationGoogle(String nameOfLocation)
            throws InvalidLocationException, QueryLimitException {
        int numberOfTries = 0;

        do {
            URL googleMapsURL = null;
            try {
                try {
                    URI uri = new URI("https",
                            "maps.googleapis.com",
                            "/maps/api/geocode/json",
                            "address=" + nameOfLocation + "&key="
                                    + googleMapsKey,
                            null);
                    googleMapsURL = uri.toURL();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                System.err.println(e.getMessage());
                ++numberOfTries;
                continue;
            }

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        googleMapsURL.openStream()));

                String currentInfo;
                StringBuilder responseStrBuilder = new StringBuilder();
                while ((currentInfo = in.readLine()) != null) {
                    responseStrBuilder.append(currentInfo);
                }

                JSONObject locationInfo =
                        new JSONObject(responseStrBuilder.toString());

                String status = locationInfo.getString("status");
                if (status.equals("OVER_QUERY_LIMIT")) {
                    throw new QueryLimitException("Google");
                }
                if (!status.equals("OK")) {
                    throw new InvalidLocationException("Unknown place");
                }

                locationInfo = locationInfo
                        .getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");

                return new Location(
                        Double.parseDouble(locationInfo.getString("lat")),
                        Double.parseDouble(locationInfo.getString("lng")));
            } catch (IOException | JSONException e) {
                System.out.println(e.getMessage());
                ++numberOfTries;
                continue;
            }
        }
        while (numberOfTries < TwitterStream.MAX_NUMBER_OF_TRIES);
        System.err.println(LOCATION_DEFINITION_ERROR);
        System.exit(1);
        return null;
    }

    private Location resolvePlaceLocationYandex(String nameOfLocation)
            throws InvalidLocationException {
        int numberOfTries = 0;

        do {
            URL yandexMapsURL = null;
            try {
                try {
                    URI uri = new URI("https",
                            "geocode-maps.yandex.ru",
                            "/1.x/",
                            "geocode=" + nameOfLocation
                                    + "&key=" + yandexMapsKey
                                    + "&format=json",
                            null);
                    yandexMapsURL = uri.toURL();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                System.err.println(e.getMessage());
                ++numberOfTries;
                continue;
            }

            try {
                assert yandexMapsURL != null;

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        yandexMapsURL.openStream()));

                String currentInfo;
                StringBuilder responseStrBuilder = new StringBuilder();
                while ((currentInfo = in.readLine()) != null) {
                    responseStrBuilder.append(currentInfo);
                }

                JSONObject locationInfo =
                        new JSONObject(responseStrBuilder.toString())
                                .getJSONObject("response")
                                .getJSONObject("GeoObjectCollection");

                if (Integer.parseInt(locationInfo
                        .getJSONObject("metaDataProperty")
                        .getJSONObject("GeocoderResponseMetaData")
                        .getString("found")) == 0) {
                    throw new InvalidLocationException("Unknown place");
                }

                locationInfo = locationInfo
                        .getJSONArray("featureMember")
                        .getJSONObject(0)
                        .getJSONObject("GeoObject")
                        .getJSONObject("Point");
                String[] coordinates =
                        locationInfo.getString("pos").split(" ");

                return new Location(
                        Double.parseDouble(coordinates[1]),
                        Double.parseDouble(coordinates[0]));
            } catch (IOException | JSONException e) {
                System.out.println(e.getMessage());
                ++numberOfTries;
            }
        }
        while (numberOfTries < TwitterStream.MAX_NUMBER_OF_TRIES);
        System.err.println(LOCATION_DEFINITION_ERROR);
        System.exit(1);
        return null;
    }

    public Location resolvePlaceLocation(String nameOfLocation)
            throws InvalidLocationException {
        nameOfLocation = nameOfLocation.trim();

        if (nameOfLocation.length() == 0) {
            throw new InvalidLocationException("empty address");
        }

        if (!cache.containsKey(nameOfLocation)) {

            try {
                Location result
                        = resolvePlaceLocationGoogle(nameOfLocation);
                cache.put(nameOfLocation, result);
            } catch (QueryLimitException e) {
                try {
                    Location result
                            = resolvePlaceLocationYandex(nameOfLocation);
                    cache.put(nameOfLocation, result);
                } catch (InvalidLocationException ie) {
                    cache.put(nameOfLocation, null);
                }
            } catch (InvalidLocationException e) {
                cache.put(nameOfLocation, null);
            }

        }

        if (cache.get(nameOfLocation) == null) {
            throw new InvalidLocationException("Unknown place");
        }
        return cache.get(nameOfLocation);
    }

    public Location resolveCurrentLocation() {
        int numberOfTries = 0;

        do {
            URL whatIsMyCity;
            try {
                whatIsMyCity = new URL("http://ipinfo.io/json");
            } catch (MalformedURLException e) {
                ++numberOfTries;
                continue;
            }

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        whatIsMyCity.openStream()));

                String currentInfo;
                StringBuilder responseStrBuilder = new StringBuilder();
                while ((currentInfo = in.readLine()) != null) {
                    responseStrBuilder.append(currentInfo);
                }

                JSONObject locationInfo =
                        new JSONObject(responseStrBuilder.toString());

                String[] coordinates = locationInfo
                        .getString("loc").split(",");

                Location result = new Location(
                        Double.parseDouble(coordinates[0]),
                        Double.parseDouble(coordinates[1]));
                result.setName(locationInfo.getString("city"));
                return result;
            } catch (IOException | JSONException e) {
                ++numberOfTries;
            }
        }
        while (numberOfTries < TwitterStream.MAX_NUMBER_OF_TRIES);

        System.err.println(LOCATION_DEFINITION_ERROR);
        System.exit(1);
        return null;
    }
}
