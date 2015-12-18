package ru.fizteh.fivt.students.nmakeenkov.threads;

import java.util.Random;

public class RollCall {
    private static volatile int activeThread;
    private static volatile boolean allThreadsAnsweredYes;
    private static volatile boolean stopRollCall;

    private static class MyThread extends Thread {
        private int myNumber;
        private Random random = new Random();

        @Override
        public void run() {
            while (true) {
                while (myNumber != activeThread) {
                    Thread.yield();
                    if (stopRollCall) {
                        return;
                    }
                }
                boolean result = random.nextInt(10) < 9;
                if (result) {
                    System.out.println("Yes");
                } else {
                    System.out.println("No");
                    allThreadsAnsweredYes = false;
                }
                activeThread++;
            }
        }

        MyThread(int myNumber) {
            this.myNumber = myNumber;
        }
    }

    public static void main(String[] args) {
        int n = 0;
        try {
            n = new Integer(args[0]);
            if (n <= 0) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.err.println("Invalid argument");
            System.exit(1);
        }
        activeThread = -1;
        stopRollCall = false;
        for (int i = 0; i < n; ++i) {
            new MyThread(i).start();
        }
        do {
            System.out.println("Are you ready?");
            allThreadsAnsweredYes = true;
            activeThread = 0;
            while (activeThread != n) {
                Thread.yield();
            }
        } while (!allThreadsAnsweredYes);
        stopRollCall = true;
    }
}
