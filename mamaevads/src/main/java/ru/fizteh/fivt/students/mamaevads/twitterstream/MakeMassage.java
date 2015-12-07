package ru.fizteh.fivt.students.mamaevads.twitterstream;
import twitter4j.*;

public class MakeMassage {
    static String getName(Status st) {
        return "@" + st.getUser().getName() + " : ";
    }
    static String getRetweets(Status st) {
        int retweet = st.getRetweetCount();
        if (retweet == 0) {
            return "";
        } else {
            return "(" + retweet + WordForms.retweetForm(retweet) + ")";
        }
    }
    static String getMessage(Status st) {
        String text = st.getText();
        if (st.isRetweet()) {
            text = " ретвитнул " + st.getRetweetedStatus().getUser().getScreenName() + " : ";
        }
        return text;
    }

    static String getTime(Status st) {
        return TimeHandler.getType(st);
    }

    static String info(Status st) throws LostInformationException {
        String message = getTime(st) + " " + getName(st) + getMessage(st) + " " + getRetweets(st) + "\n";
        message += "-----------------------------------------------------------------------------------";
        return message;
    }
}
