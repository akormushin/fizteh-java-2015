package ru.fizteh.fivt.students.thefacetakt.threads.blockingqueue;

import java.util.*;

/**
 * Created by thefacetakt on 09.12.15.
 */


public class BlockingQueue<T> {
    private Queue<T> q;
    private final int maxQueueSize;

    BlockingQueue(int maxSize) {
        q = new LinkedList<>();
        maxQueueSize = maxSize;
    }

    synchronized void offer(List<T> e) throws InterruptedException {
        while (q.size() + e.size() > maxQueueSize) {
            wait();
        }
        q.addAll(e);
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
        while (q.size() + e.size() > maxQueueSize && timeLeft > 0) {
            wait(timeLeft);
            long newCurrentTime = System.currentTimeMillis();
            timeLeft -= (newCurrentTime - currentTime);
            currentTime = newCurrentTime;
        }
        if (timeLeft <= 0) {
            return;
        }
        q.addAll(e);
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
