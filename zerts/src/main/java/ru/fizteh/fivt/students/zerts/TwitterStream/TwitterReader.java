package ru.fizteh.fivt.students.zerts.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.GeoExeption;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.GetTimelineExeption;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.NoQueryExeption;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.SearchTweetExeption;
import ru.fizteh.fivt.students.zerts.moduletests.library.*;
import twitter4j.*;

import java.io.*;

public class TwitterReader {
    static final int LOCATE_RADIUS = 50;
    public static int getLocateRadius() {
        return LOCATE_RADIUS;
    }
    public static void main(String[] args) throws IOException {
        ArgsParser argsPars = new ArgsParser();
        try {
            JCommander jComm = new JCommander(argsPars, args);
            if (argsPars.isHelpMode()) {
                jComm.usage();
            }
        } catch (ParameterException pe) {
            Printer.printError("Invalid Paramters:\n" + pe.getMessage());
        }
        if (argsPars.isStreamMode()) {
            try {
                ru.fizteh.fivt.students.zerts.moduletests.library.TwitterStream.stream(argsPars);
            } catch (NoQueryExeption | GeoExeption e) {
                e.printStackTrace();
            }
        } else if (argsPars.getQuery() == null) {
            try {
                TwitterUserTimeline.userStream(argsPars);
            } catch (GetTimelineExeption getTimelineExeption) {
                getTimelineExeption.printStackTrace();
            }
        } else {
            try {
                TwitterQuery.query(argsPars);
            } catch (GeoExeption | SearchTweetExeption | InterruptedException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
