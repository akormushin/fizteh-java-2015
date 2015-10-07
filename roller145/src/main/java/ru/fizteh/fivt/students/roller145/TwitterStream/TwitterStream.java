package ru.fizteh.fivt.students.roller145.TwitterStream;

import com.beust.jcommander.JCommander;
import javafx.util.Pair;
import twitter4j.*;

import java.io.IOException;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;
import static ru.fizteh.fivt.students.roller145.TwitterStream.GetGeolocation.getGeolocation;
import static ru.fizteh.fivt.students.roller145.TwitterStream.GetGeolocation.reCode;
import static ru.fizteh.fivt.students.roller145.TwitterStream.TimeMethods.MILISEC_IN_SEC;
import static ru.fizteh.fivt.students.roller145.TwitterStream.TimeMethods.printTime;
import static ru.fizteh.fivt.students.roller145.TwitterStream.DislenctionForms.*;
public class TwitterStream {

    public static void main(String[] args) throws Exception {

        TwitterStreamParser twParse = new TwitterStreamParser();
        JCommander command = new JCommander(twParse, args);
        if (twParse.isHelpOn()) {
            printHelp(command);
        }
        if (twParse.isTweet()) {
            makeTweet();
        }
        if (twParse.isWhere()){
            reCode();
        }
        if (twParse.isStreamOn() && twParse.isLimit()) {
            System.err.println("Конфлит команд ");
            return;
        }
        if (twParse.isStreamOn()) {
            streamMode(twParse.isFilterRetweet(), twParse.getQueryWords(), twParse.getWhere(), twParse.isPlace());
        }
        if (twParse.isLimit()){
            limitedMode(twParse.isFilterRetweet(), twParse.getNumber(), twParse.getQueryWords(),twParse.getWhere(),twParse.isPlace());
        }
        return;
    }

    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String UNIT_RADIUS = "km";

    public static void printHelp(JCommander command){
        command.usage();
    }

    public static void makeTweet() throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        Scanner sc = new Scanner(System.in);
        String tweet;
        System.out.println("Ваш твит: ");
        tweet = sc.nextLine();
        twitter.updateStatus(tweet);
    }

    static void printName(String name){
        System.out.print(ANSI_PURPLE + "@" + name + ":" + ANSI_RESET);
    }
    static void printTweet(Status tweet){
        printTime(tweet.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        printName(tweet.getUser().getScreenName());
        if (tweet.isRetweet()){
            System.out.print(" ретвитнул ");
            String[] splited = tweet.getText().split(" ");
            System.out.print(
                    "@" + ANSI_PURPLE + splited[1].split("@|:")[1]
                            + ANSI_RESET + " : ");
            for (int i = 2; i < splited.length; ++i){
                System.out.print(splited[i] + " ");
            }
            System.out.println();
        }
        else {
            System.out.println(tweet.getText() + " (" + tweet.getRetweetCount()+ " "+
                    RETWEET_FORMS[getCorrectForm(tweet.getRetweetCount()).getType()] + ")");
        }

    }

    private static void limitedMode(boolean filterRetweet, int numberOfTweets, String queryWords, String where, boolean isLocation) throws IOException {
        twitter4j.Twitter twitter = new TwitterFactory().getInstance();
        if (where == null && queryWords == null){
            System.err.println("Пустой запрос");
            return;
        }
        try {
            boolean isAnyTweets= false;
            Query query = new Query();
            if (queryWords == null){
                Pair<GeoLocation, Double> location = getGeolocation(where);
                query.setGeoCode(location.getKey(), location.getValue(), UNIT_RADIUS);
            }
            else {
                query.setQuery(queryWords);
                if (isLocation) {
                    Pair<GeoLocation, Double> location = getGeolocation(where);
                    query.setGeoCode(location.getKey(), location.getValue(), UNIT_RADIUS);
                }
            }
            QueryResult result;
            Integer count = 0;

            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    if ((tweet.isRetweet() && !filterRetweet) || !tweet.isRetweet() ){
                        printTweet(tweet);
                        isAnyTweets = true;
                        ++count;
                        if (count == numberOfTweets){
                            break;
                        }
                    }
                }
            } while ((query = result.nextQuery()) != null);
            if (!isAnyTweets) {
                System.out.println("\nЯ не могу найти какие-либо твиты для запроса "
                                + queryWords + " для  " + where
                                + " \n\n"
                                + " Пожалуйста, попробуйте проверить орфографию или использовать более общие слова\n");
            }
            return;
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Не удалось найти твиты: " + te.getMessage());
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void streamMode(boolean filterRetweet, String queryWords, String where, boolean isLocation) {
        if (where == null && queryWords == null){
            System.err.println("Пустой запрос ");
            return;
        }
        twitter4j.Twitter twitter = new TwitterFactory().getInstance();
        try {
            Query query = new Query();
            if (queryWords == null){
                Pair<GeoLocation, Double> location = getGeolocation(where);
                query.setGeoCode(location.getKey(), location.getValue(), UNIT_RADIUS);
            }
            else {
                query.setQuery(queryWords);
                if (isLocation) {
                    Pair<GeoLocation, Double> location = getGeolocation(where);
                    query.setGeoCode(location.getKey(), location.getValue(), UNIT_RADIUS);
                }
            }
            QueryResult result;
            boolean isAnyTweets= false;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    if ((tweet.isRetweet() && !filterRetweet) || !tweet.isRetweet()) {
                        printTweet(tweet);
                        sleep(1 * MILISEC_IN_SEC);
                        isAnyTweets = true;
                    }
                }
            } while ((query = result.nextQuery()) != null);
            if (!isAnyTweets) {
                System.err.println("\nЯ не могу найти какие-либо твиты для запроса \n" +
                        queryWords + " для " + where +
                        " \n\n" +
                        "Пожалуйста, попробуйте проверить орфографию или использовать более общие слова\n");
            }
            return;
        } catch (TwitterException te) {
            te.printStackTrace();
            System.err.println("Не удалось найти твиты:  " + te.getMessage());
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

