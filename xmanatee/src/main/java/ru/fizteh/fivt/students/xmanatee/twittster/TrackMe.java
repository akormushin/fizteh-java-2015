package ru.fizteh.fivt.students.xmanatee.twittster;


import org.apache.commons.io.IOUtils;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

class TrackMe {
    private JSONObject object;
    TrackMe() throws JSONException, IOException {
        String url = "http://ipinfo.io/json";
        try {
            String jsonText = IOUtils.toString(new URL(url));
            object = new JSONObject(jsonText);
        } catch (JSONException e) {
            System.err.println("Problems with parsing JSON while trying to track you : " + e.getMessage());
            throw e;
        } catch (MalformedURLException e) {
            System.err.println("Problems with URL: " + url + " while trying to track you : " + e.getMessage());
            throw e;
        } catch (IOException e) {
            System.err.println("Problems with reading from URL : " + e.getMessage());
            throw e;
        }

    }

    String getPlace() throws JSONException {
        String place = null;
        try {
            place = object.getString("city");
        } catch (JSONException e) {
            System.err.println("Problems extracting your city from JSON : " + e.getMessage());
            throw e;
        }
        return place;
    }
}
