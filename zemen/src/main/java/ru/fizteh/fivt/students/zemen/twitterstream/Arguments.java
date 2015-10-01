/**
 * Created by zemen on 27.09.15.
 */

package ru.fizteh.fivt.students.zemen.twitterstream;

import com.beust.jcommander.Parameter;

import java.util.List;

class Arguments {
    @Parameter(names = {"--query", "-q"}, required = true, variableArity = true,
            description = "Ключевые слова для запроса")
    private List<String> query = null;

    @Parameter(names = {"--place", "-p"},
            description = "Искать по заданному региону. Если параметр не указан, или указан параметр"
                    + " nearby - вычислить по IP")
    private String place = "nearby";

    @Parameter(names = {"--stream", "-s"},
            description = "Равномерно и непрерывно с задержкой в 1 секунду "
                    + "печать твиты на экран")
    private boolean stream = false;

    @Parameter(names = "--hideRetweets", description = "Скрывать ретвиты")
    private boolean hideRetweets = false;

    @Parameter(names = {"--limit", "-l"},
            description = "выводить только ограниченное число твитов. "
                    + "Не применимо для --stream режима")
    private int limit = -1;

    @Parameter(names = {"--help", "-h"}, help = true,
            description = "Show this help")
    private boolean help = false;

    public boolean isHelp() {
        return help;
    }

    public List<String> getKeywords() {
        return query;
    }

    public boolean isHideRetweets() {
        return hideRetweets;
    }

    public int getLimit() {
        return limit;
    }

    public String getLocation() {
        return place;
    }

    public boolean isStream() {
        return stream;
    }
}
