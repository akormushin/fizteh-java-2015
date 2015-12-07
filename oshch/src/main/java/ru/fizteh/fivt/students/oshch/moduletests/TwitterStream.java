package ru.fizteh.fivt.students.oshch.moduletests;

import com.beust.jcommander.JCommander;
import ru.fizteh.fivt.students.oshch.moduletests.library.Parameters;
import ru.fizteh.fivt.students.oshch.moduletests.library.StatusPrinter;
import twitter4j.*;

import java.io.IOException;

import static ru.fizteh.fivt.students.oshch.moduletests.library.SearchTweets.search;
import static ru.fizteh.fivt.students.oshch.moduletests.library.StreamTweets.stream;

public class TwitterStream {
    private static Parameters param;

    public static Parameters getParameters(String[] args) throws Exception {
        Parameters param = new Parameters();
        JCommander cmd = new JCommander(param, args);
        if (param.isHelp()) {
            cmd.usage();
        }
        return param;
    }


    public static void main(String[] args) throws Exception {
        param = getParameters(args);
        if (param.isHelp()) {
            System.exit(0);
        }
        StatusPrinter.setStream(param.isStream());
        if (param.isStream()) {
            try {
                stream(param, StatusPrinter::printStatus);
            } catch (TwitterException e) {
                System.err.println("twitter error");
            } catch (IOException e) {
                System.err.println("cant find api key");
            } catch (Exception e) {
                System.err.println("connection error");
            }
        } else {
            try {
                search(param, StatusPrinter::printStatus);
            } catch (TwitterException e) {
                System.err.println("twitter error");
            } catch (Exception e) {
                System.err.println("connection error");
            }
        }
    }
}
