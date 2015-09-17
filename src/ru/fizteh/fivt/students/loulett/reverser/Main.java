package com.company;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList resultArgs = new ArrayList();
        for (int i = 0; i < args.length; i++){
            String[] helplist = args[i].split("\\s");
            for(int j = 0; j < helplist.length; j++){
                if(helplist[j].charAt(helplist[j].length() - 1) == '\\'){
                    resultArgs.add(helplist[j].substring(0, helplist[j].length() - 1));
                }
                else {
                    resultArgs.add(helplist[j]);
                }
            }
        }
        for (int i = resultArgs.size() - 1; i >= 0; i--) {
            System.out.print(resultArgs.get(i) + " ");
        }
    }
}