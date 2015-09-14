public class Main {

    public static void main(String[] args) {
        String[] splitted_arg;
        for (int i = args.length - 1; i >= 0; --i) {
            splitted_arg = args[i].split("[\\\\ ]");
            for (int j = splitted_arg.length - 1; j >= 0; --j) {
                if (!splitted_arg[j].equals("")) {
                    System.out.print(splitted_arg[j] + " ");
                }
            }
        }
    }
}