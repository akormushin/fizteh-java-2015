package ru.fizteh.fivt.students.xmanatee.twittster;


import org.apache.commons.io.IOUtils;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

class TrackMe {
    private JSONObject object;
    TrackMe() {
        String url = "http://ipinfo.io/json";
        try {
            String jsonText = IOUtils.toString(new URL(url));
            object = new JSONObject(jsonText);
        } catch (JSONException e) {
            System.out.println("Problems with parsing JSON while trying to track you : " + e.getMessage());
        } catch (MalformedURLException e) {
            System.out.println("Problems with URL: " + url + " while trying to track you : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Problems with reading from URL : " + e.getMessage());
        }

    }
    String getPlace() {
        String place = null;
        try {
            place = object.getString("city");
        } catch (JSONException e) {
            System.out.println("Problems extracting your city from JSON : " + e.getMessage());
        }
        return place;
    }
}
