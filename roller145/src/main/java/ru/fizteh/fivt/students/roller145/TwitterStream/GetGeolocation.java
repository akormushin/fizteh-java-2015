package ru.fizteh.fivt.students.roller145.TwitterStream;

import com.beust.jcommander.internal.Maps;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.sun.deploy.net.URLEncoder;
import javafx.util.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;


import static java.lang.Math.*;

/**
 * Created by riv on 27.09.15.
 */
public class GetGeolocation {
    public static final String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json";// путь к Geocoding API по HTTP
    public static final String GeoIPUrl  = "http://ipinfo.io/json";
    public static final String near = "nearbly";
    

    public static Pair<GeoLocation, Double> getGeolocation(String were) throws IOException {
        if (were.equals(near)) {
            final JSONObject response = JsonReader.read(GeoIPUrl);
            String city = response.getString("city");
            were = city;
        }
        final Map<String, String> params = Maps.newHashMap();
        params.put("sensor", "false");// исходит ли запрос на геокодирование от устройства с датчиком местоположения
        params.put("address", were);// адрес, который нужно геокодировать
        final String url = baseUrl + '?' + encodeParams(params);
        final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ

        JSONObject location = response.getJSONArray("results").getJSONObject(0);
        location = location.getJSONObject("geometry");
        location = location.getJSONObject("location");
        final double lng = location.getDouble("lng");// долгота
        final double lat = location.getDouble("lat");// широта

        JSONObject south = response.getJSONArray("results").getJSONObject(0);
        south = south.getJSONObject("geometry");
        south = south.getJSONObject("bounds");
        south = south.getJSONObject("southwest");
        final double southLng = south.getDouble("lng");// долгота
        final double southLat = south.getDouble("lat");// широта

        JSONObject noth = response.getJSONArray("results").getJSONObject(0);
        noth = noth.getJSONObject("geometry");
        noth = noth.getJSONObject("bounds");
        noth = noth.getJSONObject("northeast");
        final double nothLng = noth.getDouble("lng");// долгота
        final double nothLat = noth.getDouble("lat");// широта

        GeoLocation northLoc = new GeoLocation(nothLat,nothLng);
        GeoLocation southLoc = new GeoLocation(southLat,southLng);


        final double resultRadius = getDistanse(northLoc,southLoc)/2;
        GeoLocation result = new GeoLocation(lat,lng);
        return new Pair<GeoLocation, Double>(result, resultRadius);

    }


    public static void reCode() throws IOException {
        final JSONObject response = JsonReader.read(GeoIPUrl);
        String city = response.getString("city");
        System.out.println(city);
    }

    private static String getCurrentIP() {
        String result = null;
        try {
            BufferedReader reader = null;
            try {
                URL url = new URL("http://myip.by/");
                InputStream inputStream = null;
                inputStream = url.openStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder allText = new StringBuilder();
                char[] buff = new char[1024];

                int count = 0;
                while ((count = reader.read(buff)) != -1) {
                    allText.append(buff, 0, count);
                }
                Integer indStart = allText.indexOf("\">whois ");
                Integer indEnd = allText.indexOf("</a>", indStart);

                String ipAddress = new String(allText.substring(indStart + 8, indEnd));
                if (ipAddress.split("\\.").length == 4) { // минимальная (неполная)
                    //проверка что выбранный текст является ip адресом.
                    result = ipAddress;
                }
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static class JsonReader {

        private static String readAll(final Reader rd) throws IOException {
            final StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

        public static JSONObject read(final String url) throws IOException, JSONException {
            final InputStream is = new URL(url).openStream();
            try {
                final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                final String jsonText = readAll(rd);
                final JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                is.close();
            }
        }
    }
    private static String encodeParams(final Map<String, String> params) {
        final String paramsUrl = Joiner.on('&').join(// получаем значение вида key1=value1&key2=value2...
                Iterables.transform(params.entrySet(), new Function<Map.Entry<String, String>, String>() {

                    @Override
                    public String apply(final Map.Entry<String, String> input) {
                        try {
                            final StringBuilder buffer = new StringBuilder();
                            buffer.append(input.getKey());// получаем значение вида key=value
                            buffer.append('=');
                            buffer.append(URLEncoder.encode(input.getValue(), "utf-8"));// кодируем строку в соответствии со стандартом HTML 4.01
                            return buffer.toString();
                        } catch (final UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
        return paramsUrl;
    }

    static final double EARTH_RADIUS = 6371;
    static final double  M_PI =  3.14159265358979323846;

    public static double getDistanse(GeoLocation A, GeoLocation B){

        double resutl =  EARTH_RADIUS  * 2* asin(sqrtt(sqr(sin((A.getLatitude() - B.getLatitude()) /2 *M_PI / 180)
                + cos(A.getLatitude()*M_PI / 180) * cos(B.getLatitude()*M_PI / 180) * sqr(sin((A.getLongitude() - B.getLongitude())) / 2 * M_PI / 180))));
        return resutl;
    }

    private static double sqrtt(double v) {
        return pow(v,0.5);
    }

    private static double sqr(double v) {
        return v*v;
    }
}