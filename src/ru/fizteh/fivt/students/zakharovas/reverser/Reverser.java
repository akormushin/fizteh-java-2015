package ru.fizteh.fivt.students.zakharovas.reverser;

public class Reverser {

    public static void main(String[] args) {
        for (int i = args.length - 1; i >= 0; --i) {
            String[] separatedArguments = args[i].split("\\s+");
            for (int j = separatedArguments.length - 1; j >= 0; --j) {
                System.out.print(separatedArguments[j] + " ");
            }
        }
    }
}
