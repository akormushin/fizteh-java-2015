package ru.fizteh.fivt.students.xmanatee.Twittster;


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
            System.out.println("Problems with parsing JSON while trying to track you");
            //e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("Problems with URL: " + url + " while trying to track you");
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Problems with reading from URL");
            //e.printStackTrace();
        }

    }
    String getPlace() {
        String place = null;
        try {
            place = object.getString("city");
        } catch (JSONException e) {
            //e.printStackTrace();
            System.out.println("Couldn't get your location from your IP");
        }
        return place;
    }
}
