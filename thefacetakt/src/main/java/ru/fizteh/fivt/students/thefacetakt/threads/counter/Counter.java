package ru.fizteh.fivt.students.thefacetakt.threads.counter;

public class Counter {
    private Integer numberOfThreads;
    private Integer last;

    class Counted implements Runnable {

        private final Integer myNumber;

        Counted(Integer number) {
            myNumber = number;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (Counter.this) {
                    while ((last + 1) % numberOfThreads != myNumber) {
                        try {
                            Counter.this.wait();
                        } catch (InterruptedException e) {
                            System.err.printf("Interrupted %d\n", myNumber);
                            return;
                        }
                    }
                    System.out.printf("Thread %d\n", (myNumber + 1));
                    last = myNumber;
                    Counter.this.notifyAll();
                }
            }
        }
    }

    public void main(int threads) {

        numberOfThreads = threads;
        last = numberOfThreads - 1;
        for (int i = 0; i < numberOfThreads; ++i) {
            (new Thread(new Counted(i))).start();
        }
    }
}
