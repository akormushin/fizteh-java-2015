package ru.fizteh.fivt.students.okalitova.moduletests.library;

import com.google.maps.model.Bounds;
import com.google.maps.model.LatLng;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Created by nimloth on 04.10.15.
 */
public class NearbyParser {

    public static final double SHIFT = 0.1;
    public static final int EIGHT = 8;

    private static String getKey() throws IOException {
        Properties prop = new Properties();
        try (FileInputStream in = new FileInputStream("/home/nimloth/coding/3sem/"
                + "fizteh-java-2015/okalitova/nearby.properties")) {
            prop.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            throw e;
        }
        return prop.getProperty("key");
    }

    private static InputStream getInputStream() throws IOException {
        String apiUrl = "https://www.googleapis.com/geolocation/v1/geolocate?key="
                + getKey();
        InputStream inputStream = null;
        HttpsURLConnection con = null;
        URL url = new URL(apiUrl);
        con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.connect();
        JSONObject obj = new JSONObject();
        obj.put("considerIp", true);
        con.getOutputStream();
        int responseCode = con.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            inputStream = con.getInputStream();
        } else {
            inputStream = con.getErrorStream();
        }
        return inputStream;
    }

    private static String createJson() throws IOException {
        InputStream inputStream = getInputStream();
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(inputStream,
                "utf-8"), EIGHT);
        StringBuilder sbuild = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sbuild.append(line);
        }
        String json = sbuild.toString();
        return json;
    }

    public static LatLng getLatLng() throws IOException, JSONException, ParseException {
        String json = createJson();
        //now parse json
        JSONParser parser = new JSONParser();
        JSONObject jb = (JSONObject) parser.parse(json);
        JSONObject location = (JSONObject) jb.get("location");

        LatLng latLng = new LatLng((double) location.get("lat"), (double) location.get("lng"));

        return latLng;
    }

    public static Bounds getBounds() throws IOException, ParseException {
        LatLng latLng = getLatLng();
        LatLng neLatLng = new LatLng(latLng.lat + SHIFT, latLng.lng + SHIFT);
        LatLng swLatLng = new LatLng(latLng.lat - SHIFT, latLng.lng - SHIFT);
        Bounds bounds = new Bounds();
        bounds.northeast = neLatLng;
        bounds.southwest = swLatLng;
        return bounds;
    }
}
