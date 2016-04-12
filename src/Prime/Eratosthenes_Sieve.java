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
    public static final boolean DEBUG = false;
    public static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public static Lock readLock = readWriteLock.readLock();
    public static Lock writeLock = readWriteLock.writeLock();

    public static CyclicBarrier barrier = null;
    public static int numberWorker = 2;
    public static boolean[] A;
    private static List<RunnableWorker> listWorker;

    public static boolean[] PrimeAlgorithme(int n) {
        int realNumber = n + 1;
        A = new boolean[realNumber];
        for (int i = 0; i < realNumber; i++)
            A[i] = true;
        int sqrtInt = (int) Math.sqrt(realNumber);
        for (int i = 2; i <= sqrtInt; i++) {
            if (A[i] == true) {
                for (int j = (int) Math.pow(i, 2); j < realNumber; j += i) {
                    A[j]= false;
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
        int realNumber = n + 1;
        barrier = new CyclicBarrier(numberWorker + 1);
        A = new boolean[realNumber];
        listWorker = new ArrayList<>();
        for (int i = 0; i < realNumber; i++)
            A[i] = true;
        int sqrtInt = (int) Math.sqrt(realNumber);
        for (int i = 0; i < numberWorker; i++) {
            RunnableWorker runnableWorker = new RunnableWorker(i);
            listWorker.add(runnableWorker);
            if (DEBUG)
                System.out.println("create a worker" + i);
            runnableWorker.start();
        }
        for (int i = 2; i <= sqrtInt; i++) {
            if (A[i]) {
                //séparation du travail en fonction du nombre de thread
                //  distribute work among the k worker threads (*)
                int div = ((realNumber - (int) Math.pow(i, 2)) / numberWorker);
                if (DEBUG)
                    System.out.println("div : " + div);
                int j = (int) Math.pow(i, 2);
                for (RunnableWorker runnableWorker : listWorker) {
                    //calculer ensuite les start end adds
                    int start = j;
                    int add = i;
                    int end = j + div * add;
                    if (DEBUG)
                        System.out.println("for : " + i + " & worker set to :" + runnableWorker.getNumberWorker() + " start :" + start + " end :" + end + " add :" + add);
                    runnableWorker.setParameters(start, end, add, realNumber);

                    j = end;
                    if (DEBUG)
                        System.out.println("j:" + j);
                }

                //unblock the k worker threads (using the appropriate action)
                if (DEBUG)
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
        if (DEBUG)
            System.out.println("rst :" + "nb: =" + (n) + A[n]);
        return A;
    }

   /* public static boolean[] getA() {
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
        if (DEBUG)
            System.out.println("thread :" + threadNumber + " setAVal :" + i + "false");
        A[i] = val;
        writeLock.unlock();
    }

    public static void setAVal(int i, boolean val) {
        writeLock.lock();
        if (DEBUG)
            System.out.println("setAVal :" + i + "false");
        A[i] = val;
        writeLock.unlock();
    }

    public static void setA(boolean[] a) {
        writeLock.lock();
        A = a;
        writeLock.unlock();
    }*/

}
