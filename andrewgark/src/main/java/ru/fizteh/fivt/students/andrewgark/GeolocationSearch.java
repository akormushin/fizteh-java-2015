package ru.fizteh.fivt.students.andrewgark;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.fizteh.fivt.students.andrewgark.URLs.getUrl;

public class GeolocationSearch {
    public static Double[] getCoordinatesByIp() {
        String html = getUrl("https://ipcim.com/en/?p=where");
        Pattern myPattern = Pattern.compile(".*LatLng\\(([0-9.]*), ([0-9.]*)\\);.*");
            Matcher m = myPattern.matcher(html);
        if (!m.find()) {
            System.err.println("We can't find you by IP.");
            System.exit(-1);
        }
        Double[] coordinates = new Double[2];
        coordinates[0] = Double.parseDouble(m.group(2));
        coordinates[1] = Double.parseDouble(m.group(1));
        return coordinates;
    }

    public static Double[] getCoordinatesByQuery(String location) {
        String urlQuery = "https://geocode-maps.yandex.ru/1.x/?geocode=";
        String yandexKey = readFirstLineFromResource("/YandexMapsAPI.properties");
        String xml = getUrl(urlQuery + location + "&key=" + yandexKey);
        Pattern myPattern = Pattern.compile(".*<pos>([0-9.\\-]*) ([0-9.\\-]*)<\\/pos>.*");
        Matcher m = myPattern.matcher(xml);
        if (!m.find()) {
            System.err.println("We can't find this location.");
            System.exit(-1);
        }
        Double[] coordinates = new Double[2];
        coordinates[0] = Double.parseDouble(m.group(2));
        coordinates[1] = Double.parseDouble(m.group(1));
        return coordinates;
    }

    public static String readFirstLineFromResource(String nameResource) {
        try (BufferedReader br = new BufferedReader(new FileReader(
                        GeolocationSearch.class.getResource(nameResource).getFile()))) {
            String line = br.readLine();
            br.close();
            return line;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
