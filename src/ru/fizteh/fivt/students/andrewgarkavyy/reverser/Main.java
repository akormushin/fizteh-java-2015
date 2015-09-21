public class Main {
    public static void main(String[] args) {
        String allArgs = "";
        for (int i = 0; i < args.length; i++)
            allArgs += " " + args[i];
        String[] numbers = allArgs.split("((\\s)|(/)|(\\\\))+");
        for (int i = numbers.length - 1; i >= 0; i--) {
            System.out.print(numbers[i] + " ");
        }
    }
}
