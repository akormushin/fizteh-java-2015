package ru.fizteh.fivt.students.thefacetakt.threads.rollcall;

import java.util.Random;

public class Rollcaller {

    private int numberOfThreads;
    private boolean everybodyReady = false;
    private Random random;

    private int calledCount = 0;
    private int calledTime = 0;
    static final double NO_PROBABILITY = 0.1;

    class Rollcalled implements Runnable {
        private int lastCalled;

        Rollcalled() {
            lastCalled = 0;
            ++calledCount;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (Rollcaller.this) {
                    while (lastCalled == calledTime) {
                        if (calledCount == numberOfThreads && everybodyReady) {
                            return;
                        }
                        try {
                            Rollcaller.this.wait();
                        } catch (InterruptedException e) {
                            System.err.printf("Interrupted\n");
                            return;
                        }
                    }
                    ++lastCalled;
                    if (random.nextDouble() < NO_PROBABILITY) {
                        System.out.println("NO");
                        everybodyReady = false;
                    } else {
                        System.out.println("YES");
                    }
                    ++calledCount;
                    Rollcaller.this.notifyAll();
                }
            }
        }
    }


    static final int GOOD_SEED = 3;
    public void main(int threads) {
        numberOfThreads = threads;
        random = new Random(GOOD_SEED);

        for (int i = 0; i < numberOfThreads; ++i) {
            (new Thread(new Rollcalled())).start();
        }

        while (true) {
            synchronized (this) {
                while (calledCount != numberOfThreads) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        System.err.printf("Interrupted\n");
                        return;
                    }
                }
                if (everybodyReady) {
                    return;
                }

                System.out.println("Are you ready?");
                calledCount = 0;
                calledTime += 1;
                everybodyReady = true;
                notifyAll();
            }
        }

    }
}
