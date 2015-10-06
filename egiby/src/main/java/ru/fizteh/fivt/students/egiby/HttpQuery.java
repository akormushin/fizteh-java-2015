package ru.fizteh.fivt.students.egiby;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by egiby on 04.10.15.
 */
public class HttpQuery {
    public static String getQuery(String url) throws MalformedURLException {
        URL answer = new URL(url);

        try (BufferedReader input = new BufferedReader(new InputStreamReader(answer.openStream()))) {
            StringBuilder answerBuilder = new StringBuilder();
            String line;

            while ((line = input.readLine()) != null) {
                answerBuilder.append(line);
                answerBuilder.append("\n");
            }

            return answerBuilder.toString();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
