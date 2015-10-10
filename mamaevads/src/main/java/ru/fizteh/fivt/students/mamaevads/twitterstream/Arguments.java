package ru.fizteh.fivt.students.mamaevads.twitterstream;

import com.beust.jcommander.Parameter;

public class Arguments {
    private final int maxLimit = 100;
    @Parameter(names = { "-q", "--query" },
            description = "ключевые слова для поиска")
    private String query = "";
    @Parameter(names = { "-p", "--place" },
            description = "поиск твитов в некотором регионе")
    private String place = "";
    @Parameter(names = { "-l", "--limit" },
            description = "количество твитов")
    private Integer limit = maxLimit;
    @Parameter(names = { "-s", "--stream" },
            description = "вывод твитов в стриминге")
    private boolean stream = false;
    @Parameter(names = { "--hideRetweets" },
            description = "скрывать ретвиты")
    private boolean hideRetweets = false;
    @Parameter(names = { "-h", "--help" },
            description = "помошь")
    private boolean help = false;


    public final String getQuery() {
        return query;
    }

    public final String getPlace() {
        return place;
    }

    public final Integer getLimit() {
        return limit;
    }

    public final boolean isStream() {
        return stream;
    }

    public final boolean isHideRetweets() {
        return hideRetweets;
    }

    public final boolean isHelp() {
        return help;
    }
}

