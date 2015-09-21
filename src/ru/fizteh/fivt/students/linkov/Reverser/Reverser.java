/**
 * Created by evlinkov on 20.09.15.
 */

public class Reverser {
    public static void main(String[] args) {
        String[] Numbers;
        for (int i = args.length - 1; i >= 0; i--) {
            Numbers = args[i].split("\\s+");
            for (int j = Numbers.length - 1; j >= 0; --j) {
                System.out.print(Numbers[j] + " ");
            }
        }
        System.out.print("\n");
    }
}

