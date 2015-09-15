/**
 * Created by max on 14/09/15.
 */
public class Reverser {
    public static void main(String[] args) {
        for (int argIndex = args.length - 1; argIndex >= 0; --argIndex) {
            String[] parts = args[argIndex].split("\\s+");
            for (int partIndex = parts.length - 1; partIndex >= 0; --partIndex)
                System.out.print(parts[partIndex] + " ");
        }
        System.out.println();
    }
}
