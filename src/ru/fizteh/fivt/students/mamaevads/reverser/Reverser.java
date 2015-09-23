package ru.fizteh.fivt.students.mamaevads.reverser;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Даша on 14.09.2015.
 */
public class Reverser {
    public static void main(String[] args) {
        for(int i = args.length-1; i >= 0; i--) {
            String [] temp = args[i].split("[\\s+\\\\\"]");
            Collections.reverse(Arrays.asList(temp));
            for(int j =0; j <temp.length; j++){
                System.out.print(temp[j]+" ");
            }
        }
    }
}
