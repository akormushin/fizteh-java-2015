package ru.fizteh.fivt.students.okalitova.TwitterStreamer;

import com.beust.jcommander.Parameter;

/**
 * Created by okalitova on 24.09.15.
 */
public class Parameters {

        public static final int HUNDRED = 100;
        @Parameter(names = {"-q", "--query"},
                description = "ищется как подстрока в твите")
        private String query = "";
        @Parameter(names = {"-p", "--place"},
                description = "поиск твитов из конкретного места")
        private String place = "";
        @Parameter(names = {"-s", "--stream"},
                description = "стрим on!")
        private boolean stream = false;
        @Parameter(names = {"--hideRetwitts"},
                description = "прятать ретвиты")
        private boolean hideRetwitts = false;
        @Parameter(names = {"-h", "--help"},
                description = "выводит подсказку")
        private boolean help = false;
        @Parameter(names = {"-l", "--limit"},
                description = "ограничение на количество"
                        + " выводимых твитов (не в стриме)")
        private Integer limit = HUNDRED;

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

        public final boolean isHideRetwitts() {
            return hideRetwitts;
        }

        public final boolean isHelp() {
            return help;
        }

}
