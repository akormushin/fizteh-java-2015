/**
 * Created by evlinkov on 20.09.15.
 */

public class Reverser {
    public static void main(String[] args) {
        String[] numbers;
        for (int i = args.length - 1; i >= 0; i--) {
            numbers = args[i].split("\\s+");
            for (int j = numbers.length - 1; j >= 0; --j) {
                System.out.print(numbers[j] + " ");
            }
        }
        System.out.print("\n");
    }
}

