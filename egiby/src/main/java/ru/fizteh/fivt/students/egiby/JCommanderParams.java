package ru.fizteh.fivt.students.egiby;

import com.beust.jcommander.Parameter;

/**
 * Created by egiby on 29.09.15.
 */
public class JCommanderParams {
    @Parameter(names = {"--query", "-q"}, description = "Поиск по строке")
    private String keyword = null;

    @Parameter(names = {"--stream", "-s"}, description = "Вывод твитов в потоке")
    private boolean stream = false;

    @Parameter(names = "--hideRetweets", description = "Скрывать ретвиты")
    private boolean hideRetweets = false;

    @Parameter(names = {"--limit", "-l"}, description = "Количество твитов. Неприменимо вместе с --stream")
    private Integer limit = null;

    @Parameter(names = {"--help", "-h"}, description = "Вывести эту справку")
    private boolean help = false;

    @Parameter(names = {"--place", "-p"}, description = "Поиск по месту")
    private String location = null;

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
