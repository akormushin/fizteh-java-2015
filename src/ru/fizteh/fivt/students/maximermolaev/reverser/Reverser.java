package ru.fizteh.fivt.students.maximermolaev.reverser;

/**
 * Created by Maxim Ermolaev on 13.09.15.
 */

public class Reverser {
    public static void main(String args[]) {
        for(int i = args.length - 1; i >= 0; --i) {
            printNumbers(args[i]);
        }
        System.out.println();
    }

    private static boolean isWhiteSpace(char c) {
        return c == ' ' ||
                c == '\t' ||
                c == '\n' ||
                c == (char) 0xB ||
                c == '\f' ||
                c == '\r';
    }

    private static void printNumbers(String str) {
        int begin;
        int end = str.length() - 1;

        while (end >= 0) {
            while (end >= 0 && isWhiteSpace(str.charAt(end))) {
                --end;
            }
            begin = end;
            while (begin >= 0 && !isWhiteSpace(str.charAt(begin))) {
                --begin;
            }
            if(end >= 0){
                System.out.print(str.substring(begin + 1, end + 1) + " ");
            }
            end = begin;
        }
    }
}
