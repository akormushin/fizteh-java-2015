package threads.rollcall;

import java.util.Random;

/**
 * Created by thefacetakt on 05.12.15.
 */
class Rollcalled implements Runnable {
    static Boolean everybodyReady;
    static Random random;
    static Integer calledCount = 0;
    static Integer calledTime = 0;
    static final Integer RANDOM_CAPACITY = 10;
    Integer lastCalled;

    Rollcalled() {
        lastCalled = 0;
    }
    @Override
    public void run() {
        synchronized (System.out) {
            if (!lastCalled.equals(calledTime)) {
                if (random.nextInt(RANDOM_CAPACITY) == 1) {
                    System.out.println("NO");
                    everybodyReady = false;
                }
                ++calledCount;
                System.out.notifyAll();
            }
        }
    }
}

public class Rollcall {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: Counter n, where n is a"
                    + " number of the threads");
            return;
        }
        Integer numberOfThreads = Integer.parseInt(args[1]);
        Rollcalled.everybodyReady = false;
        Rollcalled.random = new Random();
        for (int i = 0; i < numberOfThreads; ++i) {
            (new Thread(new Rollcalled())).start();
        }
        synchronized (System.out) {
            while (!Rollcalled.everybodyReady) {
                Rollcalled.everybodyReady = true;

            }
        }
    }
}
