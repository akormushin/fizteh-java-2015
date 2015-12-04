package ru.fizteh.fivt.students.andrewgark.TwitterStream;

import java.io.*;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeolocationSearch {
    public static class SearchLocationException extends Exception {
        public SearchLocationException(String message) {
            super(message);
        }
    }

    public static class NoKeyException extends Exception {
        public NoKeyException(String message) {
            super(message);
        }
    }

    public static Double[] getCoordinatesByIp() throws SearchLocationException, URLs.HTTPQueryException,
            URLs.ConnectionException, MalformedURLException {
        String html = URLs.getUrl("https://ipcim.com/en/?p=where");
        Pattern myPattern = Pattern.compile(".*LatLng\\(([0-9.\\-]*), ([0-9.\\-]*)\\);.*");
        Matcher m = myPattern.matcher(html);
        if (!m.find()) {
            throw new SearchLocationException("We can't find you by IP.");
        }
        Double[] coordinates = new Double[2];
        coordinates[0] = Double.parseDouble(m.group(2));
        coordinates[1] = Double.parseDouble(m.group(1));
        return coordinates;
    }

    public static Double[] getCoordinatesByQuery(String location)
            throws URLs.HTTPQueryException, SearchLocationException, NoKeyException,
            URLs.ConnectionException, MalformedURLException {
        String urlQuery = "https://geocode-maps.yandex.ru/1.x/?geocode=";
        String yandexKey = readKeyFromResource("/YandexMapsAPI.properties");
        String xml = URLs.getUrl(urlQuery + location + "&key=" + yandexKey);
        Pattern myPattern = Pattern.compile(".*<pos>([0-9.\\-]*) ([0-9.\\-]*)<\\/pos>.*");
        Matcher m = myPattern.matcher(xml);
        if (!m.find()) {
            throw new SearchLocationException("We can't find this location: " + location);
        }
        Double[] coordinates = new Double[2];
        coordinates[0] = Double.parseDouble(m.group(2));
        coordinates[1] = Double.parseDouble(m.group(1));
        return coordinates;
    }

    public static String readKeyFromResource(String nameResource) throws NoKeyException {
        try (BufferedReader br = new BufferedReader(new FileReader(
                        GeolocationSearch.class.getResource(nameResource).getFile()))) {
            return br.readLine();
        } catch (IOException ioe) {
            throw new NoKeyException("There isn't key.");
        }
    }
}
