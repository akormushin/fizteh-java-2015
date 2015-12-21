package ru.fizteh.fivt.students.nmakeenkov.threads;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<T> {
    private int maxSize;
    private Queue<T> queue;

    private Lock queueLock = new ReentrantLock();
    private Condition wait = queueLock.newCondition();

    public void offer(List<T> e, long timeout) {
        boolean done = false;
        LocalDateTime timeStarted = LocalDateTime.now();
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
                        if (timeout == 0) {
                            wait.await();
                        } else {
                            if (ChronoUnit.MILLIS.between(timeStarted,
                                    LocalDateTime.now()) > 0) {
                                done = true;
                            } else {
                                wait.awaitNanos(ChronoUnit.NANOS.between(timeStarted,
                                        LocalDateTime.now()));
                            }
                        }
                    } catch (InterruptedException ex) { }
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
        offer(e, 0);
    }

    public List<T> take(int n, int timeout) {
        List<T> ans = new ArrayList<>();
        boolean done = false;
        LocalDateTime timeStarted = LocalDateTime.now();
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
                        if (timeout == 0) {
                            wait.await();
                        } else {
                            if (ChronoUnit.MILLIS.between(timeStarted,
                                    LocalDateTime.now()) > 0) {
                                done = true;
                            } else {
                                wait.awaitNanos(ChronoUnit.NANOS.between(timeStarted,
                                        LocalDateTime.now()));
                            }
                        }
                    } catch (InterruptedException ex) { }
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
        return take(n, 0);
    }

    BlockingQueue(int maxSize) {
        this.maxSize = maxSize;
        queue = new ArrayDeque<T>();
    }
}
