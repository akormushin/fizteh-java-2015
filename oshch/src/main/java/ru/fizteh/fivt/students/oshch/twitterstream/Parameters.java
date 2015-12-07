package ru.fizteh.fivt.students.oshch.twitterstream;

import com.beust.jcommander.Parameter;

public class Parameters {
    public static final int HUNDRED = 100;

    @Parameter(names = {"-q", "--query"},
            description = "поисковой запрос")
    private String query = "";
    @Parameter(names = {"-p", "--place"},
            description = "место, рядом с которым искать")
    private String place = "nearby";
    @Parameter(names = {"-s", "--stream"},
            description = "стрим твитов")
    private boolean stream = false;
    @Parameter(names = {"--hideRetweets"},
            description = "прятать ретвиты")
    private boolean hideRt = false;
    @Parameter(names = {"-h", "--help"},
            description = "показать помощь")
    private boolean help = false;
    @Parameter(names = {"-l", "--limit"},
            description = "максимальное количество твиттов (не в стриме)")
    private Integer limit = HUNDRED;

    public final String getQuery() {
        return query;
    }

    public boolean isStream() {
        return stream;
    }

    public boolean isHelp() {
        return help;
    }

    public final String getPlace() {
        return place;
    }

    public final Integer getLimit() {
        return limit;
    }

    public final boolean isHideRt() {
        return hideRt;
    }
}
