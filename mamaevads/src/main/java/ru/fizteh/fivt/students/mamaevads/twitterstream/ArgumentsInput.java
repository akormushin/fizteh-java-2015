package ru.fizteh.fivt.students.mamaevads.twitterstream;

import com.beust.jcommander.JCommander;


public class ArgumentsInput {
    public static Arguments inputArguments(String[] args) throws InvalidArgumentsException {
        Arguments arguments = new Arguments();
        try {
            JCommander commander = new JCommander(arguments, args);
            if (arguments.isHelp()) {
                commander.usage();
                System.exit(0);
            }
        } catch (Exception ex) {
            System.out.print("Invalid arguments exception");
            System.exit(1);
        }
        return arguments;
    }
}


