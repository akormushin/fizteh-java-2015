package twitter4j;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Dirty hack to access twitter4j package local classes.
 * <p>
 * Created by kormushin on 29.09.15.
 */
public class Twitter4jTestUtils {

    public static List<Status> tweetsFromJson(String resource) {
        try (InputStream inputStream
                     = Twitter4jTestUtils.class.getResourceAsStream(resource)) {
            JSONObject json = new JSONObject(IOUtils.toString(inputStream));

            JSONArray array = json.getJSONArray("statuses");
            List<Status> tweets = new ArrayList<>(array.length());
            for (int i = 0; i < array.length(); i++) {
                JSONObject tweet = array.getJSONObject(i);
                tweets.add(new StatusJSONImpl(tweet));
            }

            return tweets;
        } catch (IOException | JSONException | TwitterException e) {
            throw new RuntimeException(e);
        }
    }
}
