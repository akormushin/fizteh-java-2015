package ru.fizteh.fivt.students.ivanovmisha.reverser;

public class Reverser {
    public static void main(String[] args) {
        for (int i = args.length - 1; i >= 0; --i) {
            String[] devidedWords = args[i].split("\\s");
            for (int j = devidedWords.length - 1; j >= 0; --j)
                System.out.print(devidedWords[j] + ' ');
        }
    }
}

