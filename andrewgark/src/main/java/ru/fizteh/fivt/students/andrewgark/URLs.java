package ru.fizteh.fivt.students.andrewgark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLs {

    public static String getUrl(String url) {
        URL thisUrl = null;
        try {
            thisUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        URLConnection connection = null;
        try {
            connection = thisUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.addRequestProperty("Protocol", "Http/1.1");
        connection.addRequestProperty("Connection", "keep-alive");
        connection.addRequestProperty("Keep-Alive", "1000");
        connection.addRequestProperty("User-Agent", "Web-Agent");
        try {
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = null;
        try {
            do {
                text += in.readLine() + "\n";
            } while (in.ready());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}
