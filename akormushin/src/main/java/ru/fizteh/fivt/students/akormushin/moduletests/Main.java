package ru.fizteh.fivt.students.akormushin.moduletests;

import ru.fizteh.fivt.students.akormushin.moduletests.library.TwitterService;
import ru.fizteh.fivt.students.akormushin.moduletests.library.TwitterServiceImpl;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStreamFactory;

/**
 * Kind of manual test
 * <p>
 * Created by kormushin on 29.09.15.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Неверные параметры. Нужно: Main <query>");
            System.exit(1);
        }

        TwitterService twitterService =
                new TwitterServiceImpl(TwitterFactory.getSingleton(), TwitterStreamFactory.getSingleton());
        try {
            System.out.println("Query result:");
            twitterService.getFormattedTweets(args[0])
                    .forEach(System.out::println);

            System.out.println();
            System.out.println("And new ones with streaming:");
            twitterService.listenForTweets(args[0], System.out::println);
        } catch (TwitterException e) {
            System.out.println("Ошибка при получении твитов.");

            e.printStackTrace();
            System.exit(1);
        }
    }
}
