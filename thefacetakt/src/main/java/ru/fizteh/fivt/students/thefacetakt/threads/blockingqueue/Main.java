package ru.fizteh.fivt.students.thefacetakt.threads.blockingqueue;

import java.util.Arrays;
import java.util.List;

/**
 * Created by thefacetakt on 09.12.15.
 */
public class Main {
    private static BlockingQueue<Integer> q = new BlockingQueue<>(1);

    static class Putter implements Runnable {
        private List<Integer> offer;

        Putter(Integer... offered) {
            offer = Arrays.asList(offered);
        }

        @Override
        public void run() {
            try {
                q.offer(offer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Getter implements Runnable {
        private int elementsToTake;

        Getter(int n) {
            elementsToTake = n;
        }
        @Override
        public void run() {
            try {
                List<Integer> x = q.take(elementsToTake);
                x.stream().forEach(System.out::println);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        (new Thread(new Getter(5))).start();
//        Thread.sleep(10000);
//        (new Thread(new Putter(1, 2, 3, 4, 5))).start();
    }
}
