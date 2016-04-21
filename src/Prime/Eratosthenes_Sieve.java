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
    public static boolean DEBUG = false;
    public static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public static Lock readLock = readWriteLock.readLock();
    public static Lock writeLock = readWriteLock.writeLock();

    public static CyclicBarrier barrier = null;
    public static int numberWorker = 5;
    public static boolean[] A;
    private static List<RunnableWorker> listWorker;

    /**
     * Méthode de calcule séquentielle
     * @param n nombre à tester
     * @return true = premier; false = non premier
     */
    public static boolean[] PrimeAlgorithme(int n) {
        // on crée notre tableau de taille n+1 (i.e. un tableau commence à 0)
        int realNumber = n + 1;
        A = new boolean[realNumber];
        //On l'initialise à true
        for (int i = 0; i < realNumber; i++)
            A[i] = true;
        int sqrtInt = (int) Math.ceil(Math.sqrt(realNumber));
        if (DEBUG)
            System.out.println("Squrtint = " + sqrtInt);
        for (int i = 2; i <= sqrtInt; i++) {
            //Si notre nombre est toujours considéré comme un nombre premier alors on supprime tous ses mutltiples
            if (A[i]) {
                //pour tous ses multilples on les coches à faux (ils ne sont pas premier)
                for (int j = (int) Math.pow(i, 2); j < realNumber; j += i) {
                    A[j] = false;
                }
            }
        }
        return A;
    }

    /**
     * Méthode de calcule par thread
     * @param numberOfThread nombre de thread
     * @param n nombre à tester
     * @return true = premier; false = non premier
     */
    public static boolean[] PrimeAlgorithmeThread(int numberOfThread, int n) {
        numberWorker = numberOfThread;
        return PrimeAlgorithmeThread(n);
    }

    /**
     * Méthode de calcule par thread
     * @param n nombre à tester
     * @return true = premier; false = non premier
     */
    public static boolean[] PrimeAlgorithmeThread(int n) {
        // on crée notre tableau de taille n+1 (i.e. un tableau commence à 0)
        int realNumber = n + 1;
        A = new boolean[realNumber];
        //Nous créons notre barrière cyclique de nombre de worker + 1 pour le thread principal
        barrier = new CyclicBarrier(numberWorker + 1);
        //on garde une liste des threads
        listWorker = new ArrayList<>();
        //on initialise notre tableau a true
        for (int i = 0; i < realNumber; i++)
            A[i] = true;
        int sqrtInt = (int) Math.ceil(Math.sqrt(realNumber));
        for (int i = 0; i < numberWorker; i++) {
            // on crére nos thread travailleurs
            RunnableWorker runnableWorker = new RunnableWorker(i);
            listWorker.add(runnableWorker);
            if (DEBUG)
                System.out.println("create a worker" + i);
            //on les lances
            runnableWorker.start();
        }
        for (int i = 2; i <= sqrtInt; i++) {
            //Si notre nombre est toujours considéré comme un nombre premier alors on supprime tous ses mutltiples
            if (A[i]) {
                //séparation du travail en fonction du nombre de thread
                //  distribute work among the k worker threads (*)
                int totalEnd = (realNumber - (int) Math.pow(i, 2));
                int div = totalEnd / (numberWorker * i);
                int endDernierThread = totalEnd % (numberWorker * i);
                if (DEBUG)
                    System.out.println("div : " + div);
                int j = (int) Math.pow(i, 2);
                // on règles nos workers en fonction du travail qu'ils doivent effectuer
                for (RunnableWorker runnableWorker : listWorker) {
                    //calculer ensuite les start end adds
                    int start = j;
                    int add = i;
                    int end = j + div * add;
                    if (DEBUG)
                        System.out.println("for : " + i + " & worker set to :" + runnableWorker.getNumberWorker() + " start :" + start + " end :" + end + " add :" + add);
                    if (runnableWorker.getNumberWorker() == listWorker.size() - 1) {
                        runnableWorker.setParameters(start, end + endDernierThread, add, realNumber);
                    } else {
                        runnableWorker.setParameters(start, end, add, realNumber);
                    }
                    j = end;
                    if (DEBUG)
                        System.out.println("j:" + j);

                }
                // on débloque nos travailleur : nos threads worker en await + 1 le thread principal en await = la barrière lance tout le monde
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
                // on attends que nos workers ai finis
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
        // on stop nos workers
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
