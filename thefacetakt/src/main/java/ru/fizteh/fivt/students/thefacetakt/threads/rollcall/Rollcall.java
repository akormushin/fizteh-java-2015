package ru.fizteh.fivt.students.thefacetakt.threads.rollcall;

import java.util.Random;

public class Rollcall {
    static final int GOOD_SEED = 3;
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Usage: Counter n, where n is a"
                    + " number of the threads");
        }
        Rollcalled.setNumberOfThreads(Integer.parseInt(args[0]));
        Rollcalled.setRandom(new Random(GOOD_SEED));

        for (int i = 0; i < Rollcalled.getNumberOfThreads(); ++i) {
            (new Thread(new Rollcalled())).start();
        }

        while (true) {
            synchronized (System.out) {
                while (Rollcalled.getCalledCount()
                        != Rollcalled.getNumberOfThreads()) {
                    try {
                        System.out.wait();
                    } catch (InterruptedException e) {
                        System.err.printf("Interrupted\n");
                        return;
                    }
                }
                if (Rollcalled.isEverybodyReady()) {
                    return;
                }

                System.out.println("Are you ready?");
                Rollcalled.setCalledCount(0);
                Rollcalled.setCalledTime(Rollcalled.getCalledTime() + 1);
                Rollcalled.setEverybodyReady(true);
                System.out.notifyAll();
            }
        }

    }
}
