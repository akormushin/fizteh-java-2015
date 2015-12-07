public class Reverser {
    public static void main(String[] args) {
        String input = new String();
        for (int i = 0; i < args.length; ++i) {
            input += args[i] + " ";
        }
        String[] numbers = input.split("\\s+");
        for (int i = numbers.length - 1; i >= 0; --i) {
            System.out.print(numbers[i] + " ");
        }
    }
}
