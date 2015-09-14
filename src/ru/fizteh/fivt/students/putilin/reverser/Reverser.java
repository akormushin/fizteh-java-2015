package ru.fizteh.fivt.students.putilin.reverser;

public class Reverser {
    public static void main(String[] args) {
        for (int i = args.length - 1; i >= 0; i--) {
            String[] numbers = args[i].split("\\D");
            for (int j = numbers.length - 1; j >= 0; j--)
                System.out.print(numbers[j] + " ");
        }
    }
}
