package ru.fizteh.fivt.students.roller145.TwitterStream;

import com.beust.jcommander.JCommander;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Scanner;

public class TwitterStream {
    public static void printHelp(JCommander command){
        command.usage();
    }

    public static void main(String[] args) throws Exception {
        Twitter twitter = TwitterFactory.getSingleton();

        TwitterStreamParser twParse = new TwitterStreamParser();

        System.out.print("twitterStream: ");
        Scanner sc = new Scanner(System.in);
        String str ;
        while(sc.hasNext()){
            System.out.print("twitterStream: ");
            str = sc.nextLine();
            JCommander command = new JCommander(twParse, str);
            command.setProgramName("TwitterStream");
            if (command.isHelpOn()) {
                printHelp(command);
            }

        }
    }
}

