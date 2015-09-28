package ru.fizteh.fivt.students.zerts.TwitterStream.Exeptions;

public class SearchTweetExeption extends Exception {
    public SearchTweetExeption() {
    }
    public SearchTweetExeption(String message) {
        super(message);
    }
}
