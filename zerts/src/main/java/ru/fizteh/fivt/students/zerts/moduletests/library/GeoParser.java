package ru.fizteh.fivt.students.zerts.moduletests.library;

import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.GeoExeption;
import twitter4j.GeoLocation;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.Objects;

import static java.lang.Double.parseDouble;

public class GeoParser {
    static final int CITY_PARSER_TAB = 7;
    static final int TIME_TO_WAIT_FOR_YANDEX = 100;
    private static String getKey() throws IOException, GeoExeption {
        try (BufferedReader in = new BufferedReader(new FileReader(
                GeoParser.class.getResource("/yandexkey.properties").getFile()))) {
            return in.readLine();
        } catch (IOException ioe) {
            throw new GeoExeption("Can't read the yandex key. Please, check your keyfile!");
        }
    }
    public static String getMyPlace() throws IOException, JSONException, GeoExeption {
        URL getCityName = new URL("http://api.hostip.info/get_json.php");
        String city = "";
        try (BufferedReader apihostipIn = new BufferedReader(new InputStreamReader(getCityName.openStream()))) {
            String siteAnswer = apihostipIn.readLine();
            JSONObject jsonParse = new JSONObject(siteAnswer);
            city = jsonParse.getString("city");
            if (Objects.equals("(Unknown city)", city)) {
                getCityName = new URL("http://ipinfo.io/json");
                try (BufferedReader ipinfoIn = new BufferedReader(new InputStreamReader(getCityName.openStream()))) {
                    siteAnswer = "";
                    while (!siteAnswer.contains("}")) {
                        siteAnswer += ipinfoIn.readLine();
                    }
                    jsonParse = new JSONObject(siteAnswer);
                    city = jsonParse.getString("city");
                }
            }
        }
        if (Objects.equals(city, "")) {
            throw new GeoExeption("Can't detect your location");
        }
        //System.out.println(city);
        return city;
    }
    public static GeoLocation getCoordinates(String place) throws IOException, GeoExeption, InterruptedException,
            JSONException {
        //System.out.println(place);
        if (place.equals("nearby")) {
            place = getMyPlace();
        }
        //System.out.println(place);
        URL getTheLL = new URL("https://geocode-maps.yandex.ru/1.x/?geocode=" + place + "&apikey=" + getKey());
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(getTheLL.openStream()));
        } catch (IOException io) {
            Thread.sleep(TIME_TO_WAIT_FOR_YANDEX);
            in = new BufferedReader(new InputStreamReader(getTheLL.openStream()));
        }
        String xmlParse;
        do {
            xmlParse = in.readLine();
            //System.out.println(xmlParse);
            if (xmlParse != null && xmlParse.contains("<pos>")) {
                break;
            }
        } while (xmlParse != null);
        if (xmlParse == null) {
            return null;
        }
        int i = 0;
        while (xmlParse.charAt(i) != '>') {
            i++;
        }
        i++;
        String currLattitude = "", currLongtitude = "";
        while (xmlParse.charAt(i) != ' ') {
            currLongtitude += xmlParse.charAt(i);
            i++;
        }
        i++;
        while (xmlParse.charAt(i) != '<') {
            currLattitude += xmlParse.charAt(i);
            i++;
        }
        //System.out.println(currLattitude + " " + currLongtitude);
        return new GeoLocation(parseDouble(currLattitude), parseDouble(currLongtitude));
    }
    static double sqr(double number) {
        return number * number;
    }
    static final double EARTH_RADIUS = 6371;
    public static boolean near(GeoLocation first, GeoLocation second, double radius) {
        double firstLatitude = Math.toRadians(first.getLatitude());
        double firstLongtitute = Math.toRadians(first.getLongitude());
        double secondLatitude = Math.toRadians(second.getLatitude());
        double secondLongtitude = Math.toRadians(second.getLongitude());
        double deltaPhi = secondLatitude - firstLatitude;
        double deltaLambda = secondLongtitude - firstLongtitute;

        double distance = 2 * Math.asin(Math.sqrt(sqr(Math.sin(deltaPhi / 2))
                + Math.cos(firstLatitude) * Math.cos(secondLatitude) * sqr(Math.sin(deltaLambda / 2)))) * EARTH_RADIUS;
        //System.out.println(distance);
        return distance < radius;
    }
}

