package ru.fizteh.fivt.students.nmakeenkov.threads;

import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class BlockingQueueTest {

    @Test
    public void testSimple() throws Exception {
        BlockingQueue blockingQueue = new BlockingQueue<>(3);
        List a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);
        blockingQueue.offer(a);
        List b = blockingQueue.take(3);
        Assert.assertEquals(a, b);

        blockingQueue.offer(a);
        a.remove(2);
        b = blockingQueue.take(2);
        Assert.assertEquals(a, b);
    }

    @Test
    public void testFails() throws Exception {
        BlockingQueue blockingQueue = new BlockingQueue<>(3);
        List a = new ArrayList<>();
        IntStream.range(0, 4).forEach(i -> a.add(i));
        blockingQueue.offer(a, 1000);
        List b = blockingQueue.take(1, 1000);
        Assert.assertEquals(new ArrayList(), b);

        IntStream.range(0, 2).forEach(i -> a.remove(3 - i));
        blockingQueue.offer(a, 1000);
        b = blockingQueue.take(1, 1000);
        Assert.assertEquals(a.subList(0, 1), b);

        b = blockingQueue.take(10, 1000);
        Assert.assertEquals(new ArrayList(), b);
    }
}