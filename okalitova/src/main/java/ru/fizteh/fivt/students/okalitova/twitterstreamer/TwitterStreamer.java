package ru.fizteh.fivt.students.okalitova.twitterstreamer;

import com.beust.jcommander.JCommander;
import ru.fizteh.fivt.students.okalitova.moduletests.library.*;
import twitter4j.*;


public class TwitterStreamer {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE =  "\u001b[35m";

    public static ParametersParser getParameters(String[] args) {
        ParametersParser param = new ParametersParser();
        JCommander cmd = new JCommander(param, args);
        if (param.isHelp()) {
            cmd.usage();
            System.exit(0);
        }
        System.out.println("query: " + param.getQuery());
        System.out.println("place: " + param.getPlace());
        System.out.println("stream: " + param.isStream());
        System.out.println("retwitts: " + param.isHideRetwitts());
        System.out.println("limit: " + param.getLimit());
        return param;
    }

    public static void printHello() {
        System.out.println(ANSI_PURPLE
                + "~CUTE twitterstreamer CUTE~"
                + ANSI_RESET);
    }

    public static void main(String[] args) {

        printHello();

        ParametersParser param = getParameters(args);

        if (param.isStream()) {
            try {
                TwitterStream twitterStream;
                twitterStream = new TwitterStreamFactory().getInstance();
                Stream stream = new Stream(twitterStream);
                stream.streamResult(param, s -> System.out.println(s));
            } catch (TwitterException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
        } else {
            try {
                Twitter twitter = new TwitterFactory().getInstance();
                Search search = new Search(twitter);
                search.searchResult(param).stream().forEach(System.out::println);
            } catch (NoTweetsException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
        }
    }
}
