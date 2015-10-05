package ru.fizteh.fivt.students.andrewgark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLs {
    public static class HTTPQueryException extends Exception {
        public HTTPQueryException(String message) {
            super(message);
        }
    }

    public static class ConnectionException extends Exception {
        public ConnectionException(String message) {
            super(message);
        }
    }

    public static String getUrl(String url) throws HTTPQueryException, ConnectionException, MalformedURLException {
        URL thisUrl = new URL(url);
        try {
            HttpURLConnection connection = (HttpURLConnection) thisUrl.openConnection();
            connection.addRequestProperty("Protocol", "Http/1.1");
            connection.addRequestProperty("Connection", "keep-alive");
            connection.addRequestProperty("Keep-Alive", "1000");
            connection.addRequestProperty("User-Agent", "Web-Agent");
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String text = "";
                do {
                    text += in.readLine() + "\n";
                } while (in.ready());
                connection.disconnect();
                return text;
            } catch (IOException e) {
                throw new HTTPQueryException("We have problem with http-query to" + url);
            }

        } catch (IOException e) {
            throw new ConnectionException("There's a problem with connection to " + url);
        }

    }
}
