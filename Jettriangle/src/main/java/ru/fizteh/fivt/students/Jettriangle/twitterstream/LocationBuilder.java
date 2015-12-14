package ru.fizteh.fivt.students.Jettriangle.twitterstream;

/**
 * Created by rtriangle on 09.10.15.
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import twitter4j.GeoLocation;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class LocationBuilder {
    private static final int DEFAULT_RADIUS = 5;

    private GeoLocation getCoordinates(NodeList sections) {
        Element section = (Element) sections.item(0);
        String coordinates = section.getTextContent();
        String[] coordinatesParsed = coordinates.split(" ");
        Double longitude = Double.parseDouble(coordinatesParsed[0]);
        Double latitude = Double.parseDouble(coordinatesParsed[1]);
        return new GeoLocation(latitude, longitude);
    }

    private Document documentResolver(URL url)
            throws IOException, ParserConfigurationException, SAXException {
        URLConnection geoConnection = url.openConnection();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setCoalescing(true);
        factory.setNamespaceAware(false);
        factory.setValidating(false);

        DocumentBuilder parser = factory.newDocumentBuilder();

        Document document = parser.parse(geoConnection.getInputStream());
        return document;
    }

    private String getNearLocation() throws MalformedURLException {
        Document document = null;
        try {
            document = documentResolver(new URL("http://api.hostip.info/"));
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        NodeList requiredTagList = document.getElementsByTagName("gml:name");
        if (requiredTagList.getLength() < 2) {
            return "-1";
        }
        String place;
        Element section = (Element) requiredTagList.item(1);
        place = section.getTextContent();
        return place;
    }

    public Location getLocation(String place) {
        try {
            if (place.equals("nearby")) {
                place = getNearLocation();
                if (place.equals("-1")) {
                    return new Location(-1);
                }
            }

            Document document = documentResolver(new URL("https://geocode-maps.yandex.ru/1.x/?geocode=" + place));
            if (document.getElementsByTagName("pos").getLength() == 0) {
                return new Location(-1);
            }

            GeoLocation centerPos = getCoordinates(document.getElementsByTagName("pos"));
            GeoLocation nePos = getCoordinates(document.getElementsByTagName("upperCorner"));
            GeoLocation swPos = getCoordinates(document.getElementsByTagName("lowerCorner"));

            double res = DEFAULT_RADIUS;
            return new Location(centerPos, swPos, nePos, res);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return new Location(-1);
    }
}
