package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

/**
 * Created by cache-nez on 9/29/15.
 */

class GeoException extends Exception {

    GeoException(String message) {
        super(message);
    }
}

class NoKeyFoundException extends Exception {

    NoKeyFoundException(String message) {
        super(message);
    }
}


class HttpConnect {

    public static String getAnswer(String request) throws IOException {
        URL yandex = new URL(request);
        URLConnection connection = yandex.openConnection();
        StringBuilder answer = new StringBuilder();
        try (InputStreamReader streamReader =  new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(streamReader)) {
            while (bufferedReader.ready()) {
                answer.append(bufferedReader.readLine());
            }
        }
        return answer.toString();
    }
}

public class GeoLocater {
    public static final String YANDEX_URL = "https://geocode-maps.yandex.ru/1.x/?geocode=";
    public static final String YANDEX_KEYS_FILE = "yandex.properties";

    static GeoPosition parseAnswer(String geoAnswer) throws GeoException {
        try {
            JSONObject toParse = new JSONObject(geoAnswer);
            if (toParse.has("error")) {
                throw new GeoException("Request failed: " + toParse.getJSONArray("error").toString());
            }

            toParse = toParse.getJSONObject("response").getJSONObject("GeoObjectCollection");

            if (toParse.getJSONObject("metaDataProperty").getJSONObject("GeocoderResponseMetaData")
                    .getString("found").equals("0")) {
                throw new GeoException("Invalid location: yandex found nothing");
            }

            //the most relevant argument
            toParse = toParse.getJSONArray("featureMember").getJSONObject(0).getJSONObject("GeoObject")
                    .getJSONObject("Point");

            String[] coordinates = toParse.getString("pos").split(" ");
            double longitude = Double.parseDouble(coordinates[0]);
            double latitude = Double.parseDouble(coordinates[1]);
            return new GeoPosition(latitude, longitude);

        } catch (JSONException e) {
            throw new GeoException(e.getMessage());
        }

    }

    static double[][] parseBoundingBox(String geoAnswer) throws GeoException {
        try {
            JSONObject toParse = new JSONObject(geoAnswer);
            if (toParse.has("error")) {
                throw new GeoException("Request failed: " + toParse.getJSONArray("error").toString());
            }

            toParse = toParse.getJSONObject("response").getJSONObject("GeoObjectCollection");

            if (toParse.getJSONObject("metaDataProperty").getJSONObject("GeocoderResponseMetaData")
                    .getString("found").equals("0")) {
                throw new GeoException("Invalid location: yandex found nothing");
            }

            //the most relevant argument
            toParse = toParse.getJSONArray("featureMember").getJSONObject(0).getJSONObject("GeoObject")
                    .getJSONObject("boundedBy").getJSONObject("Envelope");
            String[] lowerCorner = toParse.getString("lowerCorner").split(" ");
            String[] upperCorner = toParse.getString("upperCorner").split(" ");

            double[][] boundingBox = new double[2][2];
            boundingBox[0][0] = Double.parseDouble(lowerCorner[0]);
            boundingBox[0][1] = Double.parseDouble(lowerCorner[1]);
            boundingBox[1][0] = Double.parseDouble(upperCorner[0]);
            boundingBox[1][1] = Double.parseDouble(upperCorner[1]);

            return boundingBox;

        } catch (JSONException e) {
            throw new GeoException(e.getMessage());
        }
    }


    static String getKey() throws NoKeyFoundException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Properties yandex = new Properties();
        try (InputStream inputStream = classLoader.getResourceAsStream(YANDEX_KEYS_FILE)) {
            if (inputStream == null) {
                throw new NoKeyFoundException("File yandex.properties not found");
            }
            yandex.load(inputStream);
        } catch (IOException e) {
            throw new NoKeyFoundException("Error while reading from yandex.properties");
        }
        String key = yandex.getProperty("key");
        if (key == null) {
            throw new NoKeyFoundException("key=<key> not found in the properties file");
        }
        return key;
    }

    public static GeoPosition getLocation(String location) throws GeoException {
        String key = null;
        String answer = null;
        try {
            key = getKey();
            String request = YANDEX_URL + location + "&key=" + key + "&format=json";
            answer = HttpConnect.getAnswer(request);
        } catch (NoKeyFoundException | IOException e) {
            throw new GeoException("Location failed");
        }

        return parseAnswer(answer);
    }

    public static double[][] getBoundingBox(String location) throws GeoException {
        String key = null;
        String answer = null;
        try {
            key = getKey();
            String request = YANDEX_URL + location + "&key=" + key + "&format=json";
            answer = HttpConnect.getAnswer(request);
        } catch (NoKeyFoundException | IOException e) {
            throw new GeoException("Location failed");
        }

        return parseBoundingBox(answer);
    }

    public static GeoPosition getLocationByIP(String ip) {

        return new GeoPosition();
    }
}
