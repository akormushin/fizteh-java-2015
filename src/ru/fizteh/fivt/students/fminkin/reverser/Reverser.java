package ru.fizteh.fivt.students.fminkin.reverser;

/**
 * Created by Ôåäîð on 14.09.2015.
 */
public class Reverser {
    public static void main(String[] args){
        for(int i = args.length - 1; i >= 0; --i) {
            String[] tokens = args[i].split("[\\\\s+]");
            for (int j = tokens.length - 1; j >= 0; --j) {
                System.out.print(tokens[j] + " ");
            }
        }
    }
}
