package ru.fizteh.fivt.students.ocksumoron.twitterstream;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;
import twitter4j.*;

/**
 * Created by ocksumoron on 24.09.15.
 */
public class LocationMaster {

    private GeoLocation getCoordinates(NodeList sections) {
        Element section = (Element) sections.item(0);
        String coordinates = section.getTextContent();
        String[] coordinatesParsed = coordinates.split(" ");
        Double longitude = Double.parseDouble(coordinatesParsed[0]);
        Double latitude = Double.parseDouble(coordinatesParsed[1]);
        return new GeoLocation(latitude, longitude);
    }

    private GeoLocation getCoordinates(Elements elements) {
        org.jsoup.nodes.Element section = elements.first();
        String coordinates = section.text();
        String[] coordinatesParsed = coordinates.split(" ");
        Double longitude = Double.parseDouble(coordinatesParsed[0]);
        Double latitude = Double.parseDouble(coordinatesParsed[1]);
        return new GeoLocation(latitude, longitude);
    }

    private double getRes(GeoLocation lowerCornerPos, GeoLocation upperCornerPos) {
        return 5;
    }

    private Document documentResolver(URL url)
            throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
        URLConnection geoConnection = url.openConnection();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setCoalescing(true); // Convert CDATA to Text nodes
        factory.setNamespaceAware(false); // No namespaces: this is default
        factory.setValidating(false); // Don't validate DTD: also default

        DocumentBuilder parser = factory.newDocumentBuilder();

        Document document = parser.parse(geoConnection.getInputStream());
        return document;
    }

    public Location getLocation(String place) {
        try {
            if (place.equals("nearby")) {
                Document document = documentResolver(new URL("http://api.hostip.info/"));
                Element section = (Element) document.getElementsByTagName("gml:name").item(1);
                place = section.getTextContent();
//                String html = Jsoup.connect("https://ipcim.com/en/?p=where").get().html();

//                Pattern myPattern = Pattern.compile(".*LatLng\\(([0-9.]*), ([0-9.]*)\\);.*");
//                System.out.println("!!!!!!!!!");
//                Matcher m = myPattern.matcher(html);
//                if (!m.find()) {
//                    System.out.println("We can't find your IP.");
//                    System.exit(-1);
//                }
//                GeoLocation centerPos = new GeoLocation(Double.parseDouble(m.group(2)), Double.parseDouble(m.group(1)));
//                GeoLocation SWPos = new GeoLocation(centerPos.getLatitude() - EPS, centerPos.getLongitude() - EPS);
//                GeoLocation NEPos = new GeoLocation(centerPos.getLatitude() + EPS, centerPos.getLongitude() + EPS);
//                double res = getRes(SWPos, NEPos);
//                return new Location(centerPos, SWPos, NEPos, res);
            }

                Document document = documentResolver(new URL("https://geocode-maps.yandex.ru/1.x/?geocode=" + place));
//            org.jsoup.nodes.Document document =
//                    Jsoup.connect("https://geocode-maps.yandex.ru/1.x/?geocode=" + place).get();
            GeoLocation centerPos = getCoordinates(document.getElementsByTagName("pos"));
            GeoLocation SWPos = getCoordinates(document.getElementsByTagName("lowerCorner"));
            GeoLocation NEPos = getCoordinates(document.getElementsByTagName("upperCorner"));
            double res = getRes(SWPos, NEPos);

            return new Location(centerPos, SWPos, NEPos, res);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
//        }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return new Location(0, 0, 0, 0, 10, 10, 100);
    }
}
