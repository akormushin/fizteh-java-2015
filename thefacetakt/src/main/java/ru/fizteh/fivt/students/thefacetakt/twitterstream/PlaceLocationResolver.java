package ru.fizteh.fivt.students.thefacetakt.twitterstream;

import ru.fizteh.fivt.students.thefacetakt.twitterstream
        .exceptions.InvalidLocationException;
import ru.fizteh.fivt.students.thefacetakt.twitterstream
        .exceptions.LocationDefinitionErrorException;
import ru.fizteh.fivt.students.thefacetakt.twitterstream
        .exceptions.NoKeyException;
import ru.fizteh.fivt.students.thefacetakt.twitterstream
        .exceptions.QueryLimitException;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

class PlaceLocationResolver {
    static final String LOCATION_DEFINITION_ERROR
            = "Problem while location definition";

    private Map<String, Location> cache
            = new HashMap<String, Location>();
    private String googleMapsKey;
    private String yandexMapsKey;

    PlaceLocationResolver() throws NoKeyException {

        Properties mapsKeys = new Properties();
        try (InputStream inputStream
                     = this.getClass().getResourceAsStream("/geo.properties")) {
            mapsKeys.load(inputStream);
        } catch (IOException e) {
            throw new NoKeyException();
        }

        googleMapsKey = mapsKeys.getProperty("google");
        yandexMapsKey = mapsKeys.getProperty("yandex");
        if (googleMapsKey == null || yandexMapsKey == null) {
            throw new NoKeyException();
        }
    }


    private Location resolvePlaceLocationGoogle(String nameOfLocation)
            throws InvalidLocationException, QueryLimitException,
            LocationDefinitionErrorException, MalformedURLException {
        int numberOfTries = 0;

        do {
            URL googleMapsURL = null;
            try {
                URI uri = new URI("https",
                        "maps.googleapis.com",
                        "/maps/api/geocode/json",
                        "address=" + nameOfLocation + "&key="
                                + googleMapsKey,
                        null);
                googleMapsURL = uri.toURL();
            } catch (URISyntaxException e) {
                throw new LocationDefinitionErrorException("Google: "
                        + "Can't make valid"
                        + "url from place. Perhaps, strange symbols are used");
            }

            try {
                String currentInfo
                        = HttpReader.httpGet(googleMapsURL.toString());

                JSONObject locationInfo =
                        new JSONObject(currentInfo);

                String status = locationInfo.getString("status");

                if (status.equals("ZERO_RESULTS")) {
                    throw new InvalidLocationException("Google: Unknown place");
                } else if (status.equals("OVER_QUERY_LIMIT")) {
                    throw new QueryLimitException(
                            "Google query limit exceeded");
                } else if (status.equals("REQUEST_DENIED")) {
                    if (locationInfo.has("error_message")) {
                        throw new LocationDefinitionErrorException("Google: "
                                + locationInfo.getString("error_message"));
                    } else {
                        throw new LocationDefinitionErrorException(
                                "Google: Unexpected request deny");
                    }
                } else if (!status.equals("OK")) {
                    throw new LocationDefinitionErrorException("Google: "
                            + status);
                }

                locationInfo = locationInfo
                        .getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");

                return new Location(
                        Double.parseDouble(locationInfo.getString("lat")),
                        Double.parseDouble(locationInfo.getString("lng")),
                        nameOfLocation);
            } catch (JSONException | IllegalStateException e) {
                ++numberOfTries;
                if (numberOfTries == TwitterStream.MAX_NUMBER_OF_TRIES) {
                    throw new LocationDefinitionErrorException(
                            "Google: " + LOCATION_DEFINITION_ERROR
                                    + " : " + e.getMessage());
                }
            }
        }
        while (numberOfTries < TwitterStream.MAX_NUMBER_OF_TRIES);

        throw new LocationDefinitionErrorException(LOCATION_DEFINITION_ERROR);
    }

    private Location resolvePlaceLocationYandex(String nameOfLocation)
            throws InvalidLocationException, LocationDefinitionErrorException,
            MalformedURLException {
        int numberOfTries = 0;

        do {
            URL yandexMapsURL = null;

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
                throw new LocationDefinitionErrorException("Yandex: "
                        + "Can't make valid"
                        + "url from place. Perhaps, strange symbols are used: "
                        + e.getMessage());
            }

            try {
                String currentInfo
                        = HttpReader.httpGet(yandexMapsURL.toString());
                JSONObject locationInfo
                        = new JSONObject(currentInfo);

                if (locationInfo.has("error")) {
                    throw new LocationDefinitionErrorException("Yandex: "
                            + locationInfo.getJSONObject("error")
                                    .getString("message"));
                }

                locationInfo = locationInfo
                            .getJSONObject("response")
                            .getJSONObject("GeoObjectCollection");

                if (Integer.parseInt(locationInfo
                        .getJSONObject("metaDataProperty")
                        .getJSONObject("GeocoderResponseMetaData")
                        .getString("found")) == 0) {
                    throw new InvalidLocationException("Yandex: Unknown place");
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
                        Double.parseDouble(coordinates[0]),
                        nameOfLocation);
            } catch (JSONException | IllegalStateException e) {
                ++numberOfTries;
                if (numberOfTries == TwitterStream.MAX_NUMBER_OF_TRIES) {
                    String additionalText = "";
                    throw new LocationDefinitionErrorException(
                            "Yandex: " + LOCATION_DEFINITION_ERROR + " : "
                                    + e.getMessage());
                }
            }
        }
        while (numberOfTries < TwitterStream.MAX_NUMBER_OF_TRIES);

        throw new LocationDefinitionErrorException(LOCATION_DEFINITION_ERROR);
    }

    public Location resolvePlaceLocation(String nameOfLocation)
            throws InvalidLocationException, LocationDefinitionErrorException,
            MalformedURLException {
        nameOfLocation = nameOfLocation.trim();

        if (nameOfLocation.isEmpty()) {
            throw new InvalidLocationException("empty address");
        }

        if (!cache.containsKey(nameOfLocation)) {

            try {
                Location result
                        = resolvePlaceLocationGoogle(nameOfLocation);
                cache.put(nameOfLocation, result);
            } catch (QueryLimitException | LocationDefinitionErrorException e) {
                System.err.println(e.getMessage());
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

    public Location resolveCurrentLocation()
            throws LocationDefinitionErrorException, MalformedURLException {
        int numberOfTries = 0;

        do {
            URL whatIsMyCity;

            whatIsMyCity = new URL("http://ipinfo.io/json");


            try (BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatIsMyCity.openStream()))) {

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
                        Double.parseDouble(coordinates[1]),
                        locationInfo.getString("city"));
                return result;
            } catch (IOException | JSONException e) {
                ++numberOfTries;
            }
        }
        while (numberOfTries < TwitterStream.MAX_NUMBER_OF_TRIES);

        throw new LocationDefinitionErrorException(LOCATION_DEFINITION_ERROR);
    }
}
