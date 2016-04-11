package Prime;

import java.util.concurrent.BrokenBarrierException;

/**
 * Created by Thomas on 11/04/2016.
 */

public class RunnableWorker extends Thread implements Runnable {
    private int start;
    private int end;
    private int realNumber;
    private int add;

    public int getNumberWorker() {
        return numberWorker;
    }

    private int numberWorker;

    public boolean isWorkDone() {
        return workDone;
    }

    private  boolean workDone = false;

    public void setNeedMeAgain(boolean needMeAgain) {
        this.needMeAgain = needMeAgain;
    }

    private boolean needMeAgain = true;

    public RunnableWorker(int numberWorker) {
        this.numberWorker = numberWorker;
    }

    public void setParameters(int start, int end, int add, int realNumber) {
        this.start = start;
        this.end = end;
        this.add = add;
        this.realNumber = realNumber;
    }

    public void work() {
        workDone = false;
        for (; start <= end && start < realNumber; start += add) {
            Eratosthenes_Sieve.setAVal(this.getNumberWorker(), start, false);
            yield();
        }
        workDone = true;
        if (Eratosthenes_Sieve.DEBUG)
            System.out.println("Thread " + this.numberWorker + " workDone");
    }

    @Override
    public void run() {
        while (needMeAgain) {
            try {
                if (Eratosthenes_Sieve.DEBUG)
                    System.out.println("Thread " + this.numberWorker + " is calling await()");
                Eratosthenes_Sieve.barrier.await();
                if (Eratosthenes_Sieve.DEBUG)
                    System.out.println("Thread " + this.numberWorker + " has started running again");

            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }
            work();
        }

    }
}

