package ru.fizteh.fivt.students.sergmiller.twitterStream;

/**
 * Created by sergmiller on 22.09.15.
 */

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

class JCommanderParser {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = {"--stream", "-s"},
            description = "print stream of new tweets")
    private boolean stream = false;

    @Parameter(names = {"--help", "-h"}, description = "print help man")
    private boolean help = false;
    /*
    @Parameter(names = "-debug", description = "Debug mode")
    public boolean debug = false;*/
    public boolean isStream() {
        return stream;
    }

    public boolean isHelp() {
        return help;
    }
} //Thread.sleep(10000) <- sleep
