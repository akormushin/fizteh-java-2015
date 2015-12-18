package ru.fizteh.fivt.students.nmakeenkov.threads;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<T> {
    private int maxSize;
    private Queue<T> queue;

    private Lock queueLock = new ReentrantLock();
    private Condition wait = queueLock.newCondition();

    private class OfferThread extends Thread {
        private List<T> e;
        @Override
        public void run() {
            offer(e, -1);
        }

        OfferThread(List<T> e) {
            this.e = e;
        }
    }

    public void offer(List<T> e, long timeout) {
        if (timeout != -1) {
            Thread x = new OfferThread(e);
            x.start();
            try {
                x.join(timeout);
            } catch (InterruptedException ex) { }
            if (x.isAlive()) {
                x.interrupt();
            }
            return;
        }
        boolean done = false;
        while (!done) {
            queueLock.lock();
            try {
                if (queue.size() + e.size() <= maxSize) {
                    for (T i : e) {
                        queue.add(i);
                    }
                    done = true;
                } else {
                    try {
                        wait.await();
                    } catch (InterruptedException ex) {
                        done = true;
                    }
                }
                if (done) {
                    wait.signalAll();
                }
            } finally {
                queueLock.unlock();
            }
        }
    }

    public void offer(List<T> e) {
        offer(e, -1);
    }


    private class TakeThread extends Thread {
        private int n;
        private List<T> ans;

        @Override
        public void run() {
            ans = take(n, -1);
        }

        TakeThread(int n) {
            this.n = n;
            ans = new ArrayList<T>();
        }
    }

    public List<T> take(int n, int timeout) {
        List<T> ans = new ArrayList<>();
        if (timeout != -1) {
            TakeThread x = new TakeThread(n);
            x.start();
            try {
                x.join(timeout);
            } catch (InterruptedException ex) { }
            if (x.isAlive()) {
                x.interrupt();
            }
            return x.ans;
        }
        boolean done = false;
        while (!done) {
            queueLock.lock();
            try {
                if (n <= queue.size()) {
                    for (int i = 0; i < n; ++i) {
                        ans.add(queue.poll());
                    }
                    done = true;
                } else {
                    try {
                        wait.await();
                    } catch (InterruptedException ex) {
                        done = true;
                    }
                }
                if (done) {
                    wait.signalAll();
                }
            } finally {
                queueLock.unlock();
            }
        }
        return ans;
    }

    public List<T> take(int n) {
        return take(n, -1);
    }

    BlockingQueue(int maxSize) {
        this.maxSize = maxSize;
        queue = new ArrayDeque<T>();
    }
}
