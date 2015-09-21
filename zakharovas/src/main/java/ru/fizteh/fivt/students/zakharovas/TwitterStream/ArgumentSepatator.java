package ru.fizteh.fivt.students.zakharovas.TwitterStream;

public class ArgumentSepatator {
    public static String[] separateArguments(String[] unseparatedArguments) {
        String allArgumentsInOneString = String.join(" ", unseparatedArguments);
        return allArgumentsInOneString.split("\\s+");
    }
}
