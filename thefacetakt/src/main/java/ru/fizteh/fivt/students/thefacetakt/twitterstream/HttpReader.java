package ru.fizteh.fivt.students.thefacetakt.twitterstream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by thefacetakt on 28.09.15.
 */

class HttpReader {

    //http://stackoverflow.com/questions/941628/
    // urlconnection-filenotfoundexception-for-non-standard-http-port-sources

    static final int ERROR_CODE_START = 400;

    public static String httpGet(String url) throws IllegalStateException {
        HttpURLConnection con = null;
        InputStream is = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.connect();

            //4xx: client error, 5xx: server error.
            // See: http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html.

            boolean isError = con.getResponseCode() >= ERROR_CODE_START;

            //The normal input stream doesn't work in error-cases.
            if (isError) {
                is = con.getErrorStream();
            } else {
                is = con.getInputStream();
            }

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(is))) {
                String currentInfo;
                StringBuilder responseStrBuilder = new StringBuilder();
                while ((currentInfo = in.readLine()) != null) {
                    responseStrBuilder.append(currentInfo);
                }
                return responseStrBuilder.toString();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }
}
