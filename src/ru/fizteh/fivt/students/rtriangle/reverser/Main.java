
/**
 * Created by Daniel on 14.09.2015.
 */
public class Main {
    public static void main(String[] args) {
        for (int i = args.length - 1; i >= 0; --i) {
            String[] number = args[i].split("[\\s*\\\\*]+");
            for (int j = number.length - 1; j >= 0; j--) {
                if (number[j] != " " && !number[j].equals("\\")) {
                    System.out.print(number[j] + ' ');
                }
            }
        }
    }
}
