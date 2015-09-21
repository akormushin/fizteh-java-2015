package ru.fizteh.fivt.students.zerts.TwitterStream;

import twitter4j.GeoLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static java.lang.Double.parseDouble;

/**
 * Created by User on 21.09.2015.
 */
public class GeoParser {
    public static GeoLocation getCoordinates(String place) throws IOException {
        URL getTheLL =
                new URL("https://geocode-maps.yandex.ru/1.x/?geocode=" + place);
        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(getTheLL.openStream()));

        String xmlParse = "a";
        while (xmlParse != null) {
            xmlParse = in.readLine();
            if (xmlParse.contains("<pos>")) {
                break;
            }
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
        System.out.println(currLattitude + " " + currLongtitude);
        return new GeoLocation(
                parseDouble(currLattitude), parseDouble(currLongtitude));
    }
}
