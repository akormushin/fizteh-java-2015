package ru.fizteh.fivt.students.nmakeenkov.threads;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Counter {
    private static volatile int activeThread;
    private static volatile boolean stopCount;
    private static Object monitor;

    private static class MyThread extends Thread {
        private int myNumber;
        private int nextNumber;

        @Override
        public void run() {
            while (true) {
                synchronized (monitor) {
                    while (myNumber != activeThread) {
                        try {
                            monitor.wait(1000);
                        } catch (Exception ex) { }
                        if (stopCount) {
                            return;
                        }
                    }
                    System.out.println("Thread-" + myNumber);
                    activeThread = nextNumber;
                    monitor.notifyAll();
                }
            }
        }

        MyThread(int myNumber, int nextNumber) {
            this.myNumber = myNumber;
            this.nextNumber = nextNumber;
        }
    }

    public static void main(String[] args) {
        monitor = new Object();
        LocalDateTime timeStarted = LocalDateTime.now();
        int n = 0;
        long time = -1;
        try {
            n = Integer.valueOf(args[0]);
            if (args.length > 1) {
                time = Long.valueOf(args[1]);
            }
            if (n <= 0 || (time < 0 && time != -1)) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.err.println("Invalid argument");
            System.exit(1);
        }
        activeThread = -1;
        stopCount = false;
        for (int i = 0; i < n; ++i) {
            new MyThread(i + 1, (i + 1) % n + 1).start();
        }
        activeThread = 1;
        if (time != -1) {
            while (ChronoUnit.SECONDS.between(timeStarted, LocalDateTime.now())
                    < time - 1) { // rounding causes -1
                try {
                    Thread.sleep(ChronoUnit.SECONDS.between(timeStarted, LocalDateTime.now()));
                } catch (InterruptedException ex) { }
            }
            stopCount = true;
        }
    }
}
