package ru.fizteh.fivt.students.zerts.TwitterStream;

public class Printer {
    static final int LINE_LENGTH = 170;
    public static void print(String message) {
        System.out.print(message);
    }
    public static void print(char message) {
        System.out.print(message);
    }
    public static void printError(String message) {
        System.err.println(message);
    }
    public static void printLine() {
        print("\n");
        for (int i = 0; i < LINE_LENGTH; i++) {
            print("-");
        }
        print("\n");
    }
}
