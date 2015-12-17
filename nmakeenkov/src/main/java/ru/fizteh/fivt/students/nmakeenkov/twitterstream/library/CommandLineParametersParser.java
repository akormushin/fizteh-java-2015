package ru.fizteh.fivt.students.nmakeenkov.twitterstream.library;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.util.List;
import java.util.ArrayList;

public class CommandLineParametersParser {

    public static class Parameters {
        @Parameter(names = {"-q", "--query"}, required = true, variableArity = true,
                description = "Keywords")
        private List<String> query = new ArrayList<>();

        @Parameter(names = {"-p", "--place"}, variableArity = true,
                description = "Location of tweets")
        private List<String> place = new ArrayList<>();

        @Parameter(names = {"-s", "--stream"},
                description = "Stream mode")
        private boolean stream = false;

        @Parameter(names = "--hideRetweets",
                description = "hides retweets")
        private boolean hideRetweets = false;

        @Parameter(names = {"-l", "--limit"},
                description = "Limits number of tweets shown,"
                        + " -1 - no limit")
        private int limit = -1; // -1 - no limit

        @Parameter(names = {"-h", "--help"}, help = true,
                description = "Show this message ans exit")
        private boolean help = false;

        public final String getQuery() {
            StringBuilder ans = new StringBuilder("");
            boolean empty = true;
            for (String cur : query) {
                if (!empty) {
                    ans.append(" ");
                }
                empty = false;
                ans.append(cur);
            }
            return ans.toString();
        }

        public final String getPlace() {
            if (place.isEmpty()) {
                return "nearby";
            }
            StringBuilder ans = new StringBuilder("");
            boolean empty = true;
            for (String cur : place) {
                if (!empty) {
                    ans.append(" ");
                }
                empty = false;
                ans.append(cur);
            }
            return ans.toString();
        }

        public final boolean isStream() {
            return stream;
        }

        public final boolean isHideRetweets() {
            return hideRetweets;
        }

        public final int getLimit() {
            return limit;
        }

        public final boolean isHelp() {
            return help;
        }
    }

    public static Parameters parse(String[] args) throws ParameterException {
        Parameters params = new Parameters();
        JCommander comm = new JCommander(params, args);
        if (params.isHelp()) {
            comm.usage();
        }
        return params;
    }
}
