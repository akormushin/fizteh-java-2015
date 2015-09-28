package ru.fizteh.fivt.students.andrewgark;

import twitter4j.GeoLocation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.fizteh.fivt.students.andrewgark.Files.getFile;
import static ru.fizteh.fivt.students.andrewgark.URLs.getUrl;

public class GeolocationTwitterStream {

    public static GeoLocation getLocation(String location) {
        if (location == "nearby") {
            String xml = getUrl("https://ipcim.com/en/?p=where");
            Pattern myPattern = Pattern.compile(".*LatLng\\(([0-9.]*), ([0-9.]*)\\);.*");
            Matcher m = myPattern.matcher(xml);
            if (!m.find()) {
                System.out.println("We can't find your IP.");
                System.exit(-1);
            }
            return new GeoLocation(Double.parseDouble(m.group(2)), Double.parseDouble(m.group(1)));
        } else {
            String urlQuery = "https://geocode-maps.yandex.ru/1.x/?geocode=";
            String yandexKey = getFile("src/main/resources/YandexMapsAPI.properties");
            String xml = getUrl(urlQuery + location + "&key=" + yandexKey);
            Pattern myPattern = Pattern.compile(".*<pos>([0-9.\\-]*) ([0-9.\\-]*)<\\/pos>.*");
            Matcher m = myPattern.matcher(xml);
            if (!m.find()) {
                System.out.println("We can't find this location.");
                System.exit(-1);
            }
            return new GeoLocation(Double.parseDouble(m.group(2)), Double.parseDouble(m.group(1)));
        }
    }
}
