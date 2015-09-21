package ru.fizteh.fivt.students.zakharovas.TwitterStream;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class CommandLineArgs {
    @Parameter(names = {"--query", "-q"}, variableArity = true)
    public List<String> stringForQuery = new ArrayList<>();
    @Parameter(names = {"--help", "-h"}, help = true)
    public Boolean help = false;
    @Parameter(names = {"--stream", "-s"})
    public Boolean streamMode = false;
    @Parameter(names = {"--place", "-p"}, variableArity = true)
    public List<String> location = new ArrayList<>();
    @Parameter(names = {"--hideRetweets"})
    public Boolean hideRetweets = false;
    @Parameter(names = {"--limits", "-l"})
    public Integer limit = 100;
}
