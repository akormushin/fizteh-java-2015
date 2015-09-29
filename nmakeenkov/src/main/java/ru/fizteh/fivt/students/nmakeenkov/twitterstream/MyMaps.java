package ru.fizteh.fivt.students.nmakeenkov.twitterstream;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Maps;

public class MyMaps {
    private static String encodeParams(final Map<String, String> params) {
        final String paramsUrl = Joiner.on('&').join(// получаем значение вида key1=value1&key2=value2...
                Iterables.transform(params.entrySet(), new Function<Entry<String, String>, String>() {

                    @Override
                    public String apply(final Entry<String, String> input) {
                        try {
                            final StringBuffer buffer = new StringBuffer();
                            buffer.append(input.getKey());// получаем значение вида key=value
                            buffer.append('=');
                            buffer.append(URLEncoder.encode(input.getValue(), "utf-8"));// кодирует строку в
                            // соответствии со стандартом
                            // HTML 4.01
                            return buffer.toString();
                        } catch (final UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
        return paramsUrl;
    }

    public static double[] getCoordsByPlace(String place) {
        double[] ans = {0, 0, 0};
        final String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json";
        final Map<String, String> params = Maps.newHashMap();
        params.put("sensor", "false"); // исходит ли запрос на геокодирование от устройства с датчиком местоположения
        params.put("address", place); // адрес, который нужно геокодировать
        final String url = baseUrl + '?' + encodeParams(params); // генерируем путь с параметрами
        try {
            final JSONObject response = JsonReader.read(url);
            JSONObject location = response.getJSONArray("results").getJSONObject(0);
            location = location.getJSONObject("geometry");

            JSONObject center = location.getJSONObject("location");
            ans[0] = center.getDouble("lat"); // широта
            ans[1] = center.getDouble("lng"); // долгота

            location = location.getJSONObject("viewport");
            JSONObject northEast = location.getJSONObject("northeast");
            JSONObject southWest = location.getJSONObject("southwest");

            ans[2] = Utils.getDistance(northEast.getDouble("lat"),
                    northEast.getDouble("lng"), southWest.getDouble("lat"),
                    southWest.getDouble("lng")) / 2.0;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        } catch (JSONException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        return ans;
    }

    public static double[] getMyCoords() {
        try {
            final JSONObject response = JsonReader.read("http://ipinfo.io/json");
            return getCoordsByPlace(response.getString("city"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (JSONException ex) {
            System.err.println(ex.getMessage());
        }
        System.exit(1);
        return new double[] {};
    }
}
