package ru.fizteh.fivt.students.thefacetakt.threads.counter;

public class Counter {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: Counter n, where n is a"
                    + " number of the threads");
            return;
        }
        Counted.setNumberOfThreads(Integer.parseInt(args[0]));
        Counted.setLast(Counted.getNumberOfThreads() - 1);
        for (int i = 0; i < Counted.getNumberOfThreads(); ++i) {
            (new Thread(new Counted(i))).start();
        }
    }
}
