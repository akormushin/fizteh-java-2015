package ru.fizteh.fivt.students.zakharovas.twitterstream.library;

/**
 * Created by alexander on 04.11.15.
 */
public class ArgumentSeparator {
    public static String[] separateArguments(String[] unseparatedArguments) {
        String allArgumentsInOneString = String.join(" ", unseparatedArguments);
        return allArgumentsInOneString.split("\\s+");
    }
}
