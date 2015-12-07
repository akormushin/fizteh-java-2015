package ru.fizteh.fivt.students.andrewgark.ModuleTests.library;

import twitter4j.Status;

import java.time.LocalDateTime;

public interface TwitterStreamService {
    String getForm(Integer n, String[] forms);
    String getTimeBetweenForm(LocalDateTime tweetLDT, LocalDateTime nowLDT);
    String getRetweetsForm(Integer retweets);
    String getTimeForm(Status tweet, LocalDateTime localDateTime);
}
