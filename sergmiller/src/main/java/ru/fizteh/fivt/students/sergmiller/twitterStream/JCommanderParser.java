package ru.fizteh.fivt.students.sergmiller.twitterStream;

/**
 * Created by sergmiller on 22.09.15.
 */

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

class JCommanderParser {
    @Parameter
    public static final int DEFAULT_TWEETS_LIMIT = 100;

    @Parameter(names = {"--stream", "-s"},
            description = "Если параметр задан, то приложение"
                    + " равномерно и непрерывно с задержкой в 1 секунду"
                    + " печатает твиты на экран.")
    private boolean stream = false;

    @Parameter(names = {"--help", "-h"},
            description = "Печатает эту справку.")
    private boolean help = false;

    @Parameter(names = {"--hideRetweets"},
            description = "Если параметр задан, то ретвиты фильтруются.")
    private boolean hideRetweets = false;

    @Parameter(names = {"--query", "-q"},
            description = "Ключевые слова для запроса.")
    private List<String> query = new ArrayList<>();

    @Parameter(names = {"--limit", "-l"},
            description = "Выводить не более чем столько твитов."
                    + " Не применимо для --stream режима.")
    private Integer limit = DEFAULT_TWEETS_LIMIT;

    @Parameter(names = {"--place", "-p"},
            description = "Искать по заданному региону."
                    + "Если регион равен nearby ищет твиты рядом")
    private String location = "";


    public boolean isStream() {
        return stream;
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isHideRetweets() {
        return hideRetweets;
    }

    public List<String> getQuery() {
        return query;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getLocation() {
        return location;
    }
} //Thread.sleep(10000) <- sleep
