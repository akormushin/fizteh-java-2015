package ru.fizteh.fivt.students.andrewgark;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class JCommanderTwitterStream {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = {"--query", "-q"})
    public List<String> keywords = new ArrayList<>();

    @Parameter(names = {"--place", "-p"}, arity = 1)
    public String location = "nearby";

    @Parameter(names = {"--stream", "-s"})
    public boolean stream = false;

    @Parameter(names = "--hideRetweets")
    public boolean hideRetweets = false;

    @Parameter(names = {"--limit", "-l"})
    public Integer limit = Integer.MAX_VALUE;

    @Parameter(names = {"--help", "-h"})
    public boolean help = false;
}
