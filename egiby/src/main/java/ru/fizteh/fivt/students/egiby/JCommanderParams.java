package ru.fizteh.fivt.students.egiby;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egiby on 29.09.15.
 */
public class JCommanderParams {
    public static final Integer DEFAULT_NUMBER_OF_TWEETS = 100;
    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = {"--query", "-q"}, description = "Поиск по строке")
    private String keyword = new String("");

    @Parameter(names = {"--stream", "-s"}, description = "Вывод твитов в потоке")
    private boolean stream = false;

    @Parameter(names = "--hideRetweets", description = "Скрывать ретвиты")
    private boolean hideRetweets = false;

    @Parameter(names = {"--limit", "-l"}, description = "Количество твитов. Неприменимо вместе с --stream")
    private Integer limit = DEFAULT_NUMBER_OF_TWEETS;

    @Parameter(names = {"--help", "-h"}, description = "Вывести эту справку")
    private boolean help = false;

    @Parameter(names = {"--place", "-p"}, description = "Поиск по месту")
    private String location = new String("");

    public String getKeyword() {
        return keyword;
    }

    public boolean isStream() {
        return stream;
    }

    public boolean isHideRetweets() {
        return hideRetweets;
    }

    public Integer getNumberTweets() {
        return limit;
    }

    public boolean isHelp() {
        return help;
    }

    public String getLocation() {
        return location;
    }
}
