package Prime;

import JuniTest.TestRightPrimeTo100k;

import java.util.ArrayList;
import java.util.List;

import static Prime.Eratosthenes_Sieve.PrimeAlgorithme;
import static Prime.Eratosthenes_Sieve.PrimeAlgorithmeThread;

/**
 * Created by Thomas on 08/04/2016.
 */
public class PrimeMain {

    /*public static void main(String[] args) {
        System.out.println(PrimeAlgorithme2(13));
    }*/
    public static void main(String[] args) {
        int nb= 120000;
        PrimeAlgorithme(nb);
        //System.out.println(" Est ce que "+nb + " est un nombre premier ? :"+PrimeAlgorithmeThread(100000)[nb]);

        //TestRightPrimeTo100k.perfromanceBenchmark();
    }

}
