package ru.fizteh.fivt.students.egorovaan.reverser;

/**
 * Created by Алёна on 14.09.2015.
 */
public class reverser {
    public static void main(String[] s){
        for(int i = s.length-1; i != -1; i--){
            String[] some = s[i].split("\\D");
            for(int j = some.length - 1; j != -1; j--){
                System.out.print(some[j] + " ");
            }
        }
        System.out.println();
    }
}
