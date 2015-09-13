package ru.fizteh.fivt.students.AUpshinskiy.Reverser;

/**
 * Created by egiby on 13.09.15.
 */
public class Reverser {
    /**
     * @param args
     * There is a main function.
     */
    public static void main(final String[] args) {

        for (int i = args.length - 1; i >= 0; --i) {
            String[] numbers = args[i].split("\\D");

            for (int j = numbers.length - 1; j >= 0; --j) {
                System.out.print(numbers[j] + " ");
            }
        }
        System.out.print("\n");
    }

    /**
     * There is a default constructor.
     */
    protected Reverser() {
    }
}
