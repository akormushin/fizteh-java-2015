package ru.fizteh.fivt.students.zerts.TwitterStream;
        import com.beust.jcommander.JCommander;
        import twitter4j.*;

        import java.util.Arrays;
        import java.util.List;

        import static java.lang.Thread.*;

public class PrintUserStream {
    private static int printedTweets = 0;
    public static void printTweet(Status status, ArgsParser argsPars)
    {
        if (status.isRetweet())
            if (argsPars.noRetweetMode)
                return;
        TimeParcer timeParc = new TimeParcer();
        TimeParcer.printGoneDate(status.getCreatedAt());
        printedTweets++;
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("@" + status.getUser().getScreenName() + " - ");
        int start = 0;
        String text = status.getText();
        if (status.isRetweet()) {
            if (argsPars.noRetweetMode)
                return;
            System.out.print("ретвитнул ");
            while (text.charAt(start) != '@')
                start++;
        }
        for (int i = start; i < text.length(); i++) {
            if (text.charAt(i) != '\n')
                System.out.print(text.charAt(i));
            else
                System.out.print(" ");
        }
        System.out.print("\n\n");
    }
    public static void main(String[] args) {
        ArgsParser argsPars = new ArgsParser();
        new JCommander(argsPars, args);
        System.out.print(argsPars.streamMode + "\n");
        try {
            int currPage = 1;
            Twitter twitter = new TwitterFactory().getInstance();
            User user = twitter.verifyCredentials();
            do {
                System.out.print("Hey!\n");
                Paging p = new Paging(currPage);
                List<Status> statuses = twitter.getHomeTimeline(p);
                System.out.println("Showing @" + user.getScreenName() + "'s home timeline.\n");
                for (Status status : statuses) {
                    printTweet(status, argsPars);
                    if(argsPars.numberOfTweets == printedTweets)
                        return;
                }
                currPage++;
            } while (true);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
    }
}