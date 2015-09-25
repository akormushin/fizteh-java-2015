package ru.fizteh.fivt.students.roller145.TwitterStream;

import com.beust.jcommander.Parameter;

/**
 * Created by riv on 25.09.15.
 */
public class TwitterStreamParser {
    @Parameter(names = {"-h","--help"}, description = "вывод справки")
    boolean isHelp = false;

    @Parameter(names = {"--updateStatus","-st"}, description = "Создать новый твит")
    String tweet;

    @Parameter(names = {"--place", "-p"}, description = "Искать по заданному региону. Если значение равно nearby или параметр отсутствует - искать по ip")
    String where;

    @Parameter(names = {"--stream", "-s"}, description = "Eсли параметр задан, то приложение должно равномерно и непрерывно с задержкой в 1 секунду печать твиты на экран. ")
    boolean stream = false;

    @Parameter(names = "---hideRetweets", description = "Eсли параметр задан, нужно фильтровать ретвиты")
    boolean isFilter = false;

    @Parameter(names = {"--limit","-l"}, description = "выводить только заданное количество твитов")
    int number;

    public boolean isHelpOn(){
        return isHelp;
    }

    public boolean isStreamOn(){
        return stream;
    }

}
