package Prime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.Thread.sleep;

/**
 * Created by Guillaume on 08/04/2016.
 */
public class Eratosthenes_Sieve {
    public static final boolean DEBUG =false;
    public static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public static Lock readLock = readWriteLock.readLock();
    public static Lock writeLock = readWriteLock.writeLock();

    public static CyclicBarrier barrier = null;
    public static int numberWorker = 2;
    private static boolean[] A;
    private static List<RunnableWorker> listWorker;

    public static boolean[] PrimeAlgorithme(int n) {
        int realNumber = n+1;
        A = new boolean[realNumber];
        for (int i = 0; i < realNumber; i++)
            A[i] = true;
        int sqrtInt = (int) Math.sqrt(realNumber);
        for (int i = 2; i <= sqrtInt; i++) {
            if (A[i] == true) {
                for (int j = (int) Math.pow(i, 2); j < realNumber; j += i) {
                    setAVal(j, false);
                }
            }
        }
        return A;
    }
    public static boolean[] PrimeAlgorithmeThread(int numberOfThread, int n) {
         numberWorker = numberOfThread;
        return PrimeAlgorithmeThread(n);
    }

    public static boolean[] PrimeAlgorithmeThread(int n) {
        int realNumber = n+1;
        barrier = new CyclicBarrier(numberWorker + 1);
        A = new boolean[realNumber];
        listWorker = new ArrayList<>();
        for (int i = 0; i < realNumber; i++)
            A[i] = true;
        int sqrtInt = (int) Math.sqrt(realNumber);
        for (int i = 0; i < numberWorker; i++) {
            RunnableWorker runnableWorker = new RunnableWorker(i);
            listWorker.add(runnableWorker);
            if(DEBUG)
            System.out.println("create a worker" + i);
            runnableWorker.start();
        }
        for (int i = 2; i <= sqrtInt; i++) {
            if (getAVal(i)) {
                //séparation du travail en fonction du nombre de thread
                //  distribute work among the k worker threads (*)
                int div = ((realNumber - (int) Math.pow(i, 2)) / numberWorker);
                int j = (int) Math.pow(i, 2);
                for (RunnableWorker runnableWorker : listWorker) {
//calculer ensuite les start end adds
                    int start = j;
                    int end = j + div < realNumber ? j + div : realNumber-1;
                    int add = i;
                    if(DEBUG)
                    System.out.println("for : " +i +" & worker set to :" + runnableWorker.getNumberWorker() + " start :" + start + " end :" + end + " add :" + add);
                    runnableWorker.setParameters(start, end, add);

                    j += div;
                }

                //unblock the k worker threads (using the appropriate action)
                if(DEBUG)
                System.out.println("launch all Thread");
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                //wait for all worker threads to complete their iteration (**)
                while (barrier.getNumberWaiting() != numberWorker) {
                   /* try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }


            }
        }
        for (RunnableWorker runnableWorker : listWorker) {
            runnableWorker.setNeedMeAgain(false);
            runnableWorker.interrupt();
        }
        if(DEBUG)
        System.out.println("rst :"+ "nb: =" + (n) + getAVal(n));
        return A;
    }

    public static boolean[] getA() {
        try {
            readLock.lock();
            return A;
        } catch (Exception e) {

        } finally {
            readLock.unlock();
        }
        return null;
    }

    public static boolean getAVal(int i) {
        try {
            readLock.lock();
            return A[i];
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        return false;
    }

    public static void setAVal(int threadNumber, int i, boolean val) {
        writeLock.lock();
        if(DEBUG)
        System.out.println("thread :"+threadNumber+" setAVal :"+i +"false");
        A[i] = val;
        writeLock.unlock();
    }
    public static void setAVal(int i, boolean val) {
        writeLock.lock();
        if(DEBUG)
        System.out.println("setAVal :"+i +"false");
        A[i] = val;
        writeLock.unlock();
    }

    public static void setA(boolean[] a) {
        writeLock.lock();
        A = a;
        writeLock.unlock();
    }

    public static class RunnableWorker extends Thread implements Runnable {
        private int start;
        private int end;
        private int add;

        public int getNumberWorker() {
            return numberWorker;
        }

        private int numberWorker;

        public boolean isWorkDone() {
            return workDone;
        }

        private boolean workDone = false;

        public void setNeedMeAgain(boolean needMeAgain) {
            this.needMeAgain = needMeAgain;
        }

        private boolean needMeAgain = true;

        public RunnableWorker(int numberWorker) {
            this.numberWorker = numberWorker;
        }

        public void setParameters(int start, int end, int add) {
            this.start = start;
            this.end = end;
            this.add = add;
        }

        public void work() {
            workDone = false;
            for (; start <= end; start += add) {
                setAVal(this.getNumberWorker(),start, false);
                yield();
            }
            workDone = true;
            if(DEBUG)
            System.out.println("Thread " + this.numberWorker + " workDone");
        }

        @Override
        public void run() {
            while (needMeAgain) {
                try {
                    if(DEBUG)
                    System.out.println("Thread " + this.numberWorker + " is calling await()");
                    barrier.await();
                    if(DEBUG)
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
}