package ru.fizteh.fivt.students.zakharovas.twitterstream;

public class ArgumentSepatator {
    public static String[] separateArguments(String[] unseparatedArguments) {
        String allArgumentsInOneString = String.join(" ", unseparatedArguments);
        return allArgumentsInOneString.split("\\s+");
    }
}
