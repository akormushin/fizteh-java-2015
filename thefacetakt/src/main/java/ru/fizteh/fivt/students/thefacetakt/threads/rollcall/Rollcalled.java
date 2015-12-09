package ru.fizteh.fivt.students.thefacetakt.threads.rollcall;

import java.util.Random;

/**
 * Created by thefacetakt on 05.12.15.
 */
class Rollcalled implements Runnable {
    private static int numberOfThreads;
    private static boolean everybodyReady = false;
    private static Random random;

    private static int calledCount = 0;
    private static int calledTime = 0;
    static final int RANDOM_CAPACITY = 10;

    private int lastCalled;

    Rollcalled() {
        lastCalled = 0;
        ++calledCount;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (System.out) {
                while (lastCalled == calledTime) {
                    if (calledCount == numberOfThreads && everybodyReady) {
                        return;
                    }
                    try {
                        System.out.wait();
                    } catch (InterruptedException e) {
                        System.err.printf("Interrupted\n");
                        return;
                    }
                }
                ++lastCalled;
                if (random.nextInt(RANDOM_CAPACITY) == 1) {
                    System.out.println("NO");
                    everybodyReady = false;
                } else {
                    System.out.println("YES");
                }
                ++calledCount;
                System.out.notifyAll();
            }
        }
    }

    public static boolean isEverybodyReady() {
        return everybodyReady;
    }

    public static void setEverybodyReady(boolean everybodyReady) {
        Rollcalled.everybodyReady = everybodyReady;
    }

    public static int getCalledTime() {
        return calledTime;
    }

    public static void setCalledTime(int calledTime) {
        Rollcalled.calledTime = calledTime;
    }

    public static void setRandom(Random random) {
        Rollcalled.random = random;
    }

    public static int getNumberOfThreads() {
        return numberOfThreads;
    }

    public static void setNumberOfThreads(int numberOfThreads) {
        Rollcalled.numberOfThreads = numberOfThreads;
    }

    public static int getCalledCount() {
        return calledCount;
    }

    public static void setCalledCount(int calledCount) {
        Rollcalled.calledCount = calledCount;
    }
}
