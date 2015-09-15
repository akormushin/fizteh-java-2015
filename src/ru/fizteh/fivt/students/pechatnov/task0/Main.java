package ru.fizteh.fivt.students.pechatnov.task0;

import java.util.*;

public class Main {

    public static void main(String[] args) {
	    List<String> listOfArgs = Arrays.asList(String.join(" ", args).replace("\\", "").split("\\s+"));
        Collections.reverse(listOfArgs);
        System.out.println(String.join(" ", listOfArgs));
    }
}
