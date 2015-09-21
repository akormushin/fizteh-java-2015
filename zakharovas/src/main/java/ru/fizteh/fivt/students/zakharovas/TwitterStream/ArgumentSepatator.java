package ru.fizteh.fivt.students.zakharovas.TwitterStream;

/**
 * Created by User on 19.09.2015.
 */
public class ArgumentSepatator {
    public static String[] separateArguments(String[] unseparatedArguments) {
        String allArgumentsInOneString = String.join(" ", unseparatedArguments);
        return allArgumentsInOneString.split("\\s+");
    }
}
