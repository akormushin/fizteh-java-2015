package ru.fizteh.fivt.students.mamaevads.twitterstream;

import com.beust.jcommander.JCommander;
import com.sun.javaws.exceptions.InvalidArgumentException;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.InterningXmlVisitor;


public class ArgumentsInput {
    public static Arguments inputArguments(String[] args)throws InvalidArgumentsException  {
        Arguments arguments = new Arguments();
        try {
            JCommander commander = new JCommander(arguments, args);
            if (arguments.isHelp()) {
                commander.usage();
                System.exit(0);
            }
        }
        catch (Exception ex){
            System.out.print("Invalid arguments exception");
            System.exit(1);
        }
        return arguments;
    }
}

