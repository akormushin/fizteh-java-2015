package ru.fizteh.fivt.students.andrewgark;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class JCommanderTwitterStream {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = {"--query", "-q"}, description = "Поиск твитов по ключевым словам.")
    private List<String> keywords = new ArrayList<>();

    @Parameter(names = {"--place", "-p"}, arity = 1, description = "Поиск твитов по месту.")
    private String location = "nearby";

    @Parameter(names = {"--stream", "-s"}, description = "Режим потока твитов.")
    private boolean stream = false;

    @Parameter(names = "--hideRetweets", description = "Не показывать ретвиты.")
    private boolean hideRetweets = false;

    @Parameter(names = {"--limit", "-l"}, description = "Ограничение числа выведенных твитов (не работает с потоком).")
    private Integer limit = Integer.MAX_VALUE;

    @Parameter(names = {"--help", "-h"}, description = "Справка.")
    private boolean help = false;

    public String getLocation() {
        return location;
    }

    public boolean isStream() {
        return stream;
    }

    public boolean isHideRetweets() {
        return hideRetweets;
    }

    public Integer getLimit() {
        return limit;
    }

    public boolean isHelp() {
        return help;
    }

    public List<String> getKeywords() {
        return keywords;
    }
}
