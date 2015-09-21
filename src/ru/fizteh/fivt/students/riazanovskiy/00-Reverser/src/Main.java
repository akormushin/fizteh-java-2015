public class Main {
    public static void main(String[] args) {
        for (int i = args.length - 1; i >= 0; i--) {
            String[] tokens = args[i].split("\\s+");
            for (int j = tokens.length - 1; j >= 0; j--) {
                System.out.print(tokens[j] + ' '); // although i could use String.join, there is no need for it
            }
        }
    }
}
