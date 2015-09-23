package ru.fizteh.fivt.students.riazanovskiy;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class Main {
    public static void main(String[] args) {
        ArgumentParser argumentParser = new ArgumentParser();
        try {
            new JCommander(argumentParser, args);
        } catch (ParameterException ignored) {
            new JCommander(argumentParser).usage();
        }
    }
}
