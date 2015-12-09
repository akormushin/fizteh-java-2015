package threads.counter;

import com.sun.org.apache.xpath.internal.SourceTree;

public class Counter {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: Counter n, where n is a"
                    + " number of the threads");
            return;
        }
        Counted.numberOfThreads = Integer.parseInt(args[1]);
        Counted.last = Counted.numberOfThreads - 1;
        for (int i = 0; i < Counted.numberOfThreads; ++i) {
            (new Thread(new Counted(i))).start();
        }
    }
}
