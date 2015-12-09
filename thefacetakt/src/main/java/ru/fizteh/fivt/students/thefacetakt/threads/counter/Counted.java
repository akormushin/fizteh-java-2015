package threads.counter;

/**
 * Created by thefacetakt on 05.12.15.
 */

class Counted implements Runnable {
    static Integer numberOfThreads;
    static Integer last;
    private final Integer myNumber;

    Counted(Integer number) {
        myNumber = number;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (System.out) {
                while ((last + 1) % numberOfThreads != myNumber) {
                    try {
                        System.out.wait();
                    } catch (InterruptedException e) {
                        System.err.printf("Interrupted %d\n", myNumber);
                        return;
                    }
                }
                System.out.printf("Thread %d\n", (myNumber + 1));
                last = myNumber;
                System.out.notifyAll();
            }
        }
    }
}
