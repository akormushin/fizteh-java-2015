package ru.fizteh.fivt.students.w4r10ck1337.moduletests.library;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 26.11.2015.
 */
public class ArgsParser {
    private static final int INF = 1000000000;
    private static String name = "TwiterStream";

    public static class Parameters {
        @Parameter
        private List<String> parameters = new ArrayList<>();

        @Parameter(
                names = {"-q", "--query"},
                description = "Искать по запросу <query>")
        private String query = "";

        @Parameter(
                names = {"-p", "--place"},
                description = "Искать по региону, если значение равно 'nearby' или отсутствует, искать по ip")
        private String place = "nearby";

        @Parameter(
                names = {"-s", "--stream"},
                description = "Приложение непрерывно с задержкой в 1 секунду печатает твиты на экран")
        private boolean stream = false;

        @Parameter(
                names = "--hideRetweets",
                description = "Не показывать ретвиты")
        private boolean hideRetweets = false;

        @Parameter(
                names = {"-l", "--limit"},
                description = "Выводить не более <tweet> твитов (не в стриме)")
        private int limit = INF;


        @Parameter(
                names = {"-h", "--help"},
                description = "Показать помощь",
                help = true)
        private boolean help = false;

        public String getQuery() {
            return query;
        }

        public void setQuery(String q) {
            query = q;
        }

        public String getPlace() {
            return place;
        }

        public boolean isStream() {
            return stream;
        }

        public boolean isHideRetweets() {
            return hideRetweets;
        }

        public int getLimit() {
            return limit;
        }

        public boolean isHelp() {
            return help;
        }
    }

    public static Parameters parseArgs(String[] args) {
        Parameters parameters = new Parameters();
        JCommander jc = new JCommander(parameters, args);
        jc.setProgramName(name);
        if (parameters.help) {
            jc.usage();
        }
        return parameters;
    }
}
