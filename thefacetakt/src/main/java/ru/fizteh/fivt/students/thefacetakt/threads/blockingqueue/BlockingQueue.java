package ru.fizteh.fivt.students.thefacetakt.threads.blockingqueue;

import java.util.*;

/**
 * Created by thefacetakt on 09.12.15.
 */


public class BlockingQueue<T> {
    private Queue<T> q;
    public static final int MAX_QUEUE_SIZE = 200;

    BlockingQueue() {
        q = new LinkedList<>();
    }

    synchronized void offer(List<T> e) throws InterruptedException {
        while (q.size() + e.size() > MAX_QUEUE_SIZE) {
            wait();
        }
        e.stream().forEach(q::add);
        notifyAll();
    }

    synchronized List<T> take(int n) throws InterruptedException {
        while (q.size() < n) {
            wait();
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            result.add(q.remove());
        }
        notifyAll();
        return result;
    }

    synchronized void offer(List<T> e, long timeout)
            throws InterruptedException {
        long timeLeft = timeout;
        long currentTime = System.currentTimeMillis();
        while (q.size() + e.size() > MAX_QUEUE_SIZE && timeLeft > 0) {
            wait(timeLeft);
            long newCurrentTime = System.currentTimeMillis();
            timeLeft -= (newCurrentTime - currentTime);
            currentTime = newCurrentTime;
        }
        if (timeLeft <= 0) {
            return;
        }
        e.stream().forEach(q::add);
        notifyAll();
    }

    synchronized List<T> take(int n, long timeout) throws InterruptedException {
        long timeLeft = timeout;
        long currentTime = System.currentTimeMillis();
        while (q.size() < n && timeLeft > 0) {
            wait(timeLeft);
            long newCurrentTime = System.currentTimeMillis();
            timeLeft -= (newCurrentTime - currentTime);
            currentTime = newCurrentTime;
        }
        if (timeLeft <= 0) {
            return null;
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            result.add(q.remove());
        }
        return result;
    }
}
