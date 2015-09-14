package ru.fizteh.fivt.students.oshch.task0;

public class Main {

    public static void main(String[] args) {
        for(int i = args.length - 1; i >= 0; i--) {
            String[] arg = args[i].split("\\s+");
            for (int j = arg.length - 1; j >= 0; j--) {
                arg[j] = arg[j].replaceAll("\\D", "");
                System.out.print(arg[j] + ' ');
            }
        }
    }
}
