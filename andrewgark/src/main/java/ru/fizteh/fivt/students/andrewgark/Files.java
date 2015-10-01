package ru.fizteh.fivt.students.andrewgark;

import java.io.*;

public class Files {

    public static String getFile(String fileName) {
        BufferedReader fin = null;
        try {
            fin = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s = "";
        try {
            String str;
            while ((str = fin.readLine()) != null) {
                s += str + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
