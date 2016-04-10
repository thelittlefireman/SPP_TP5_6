package JuniTest;

import Prime.Eratosthenes_Sieve;
import Prime.PrimeMain;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by Thomas on 08/04/2016.
 */
public class TestRightPrimeTo100k extends TestCase {
    public final boolean DEBUG = false;
    public final int totalInt = 100000;

    @Test
    public void testTo100kALGO1() {


            List<Object> tabPrimeTo100k =  getPrime1000kFile();

            boolean[] testAlogo = Eratosthenes_Sieve.PrimeAlgorithme(totalInt);
            for (int i = 0; i < totalInt; i++) {
                if (DEBUG)
                    System.out.println("Number test : " + i + ":" + testAlogo[i] + "Real:" + tabPrimeTo100k.contains(i));
                assertEquals("Number test : " + i + ":" + testAlogo[i] + "Real:" + tabPrimeTo100k.contains(i), testAlogo[i], tabPrimeTo100k.contains(i));
            }

    }


    @Test
    public void testTo100kPairedThread() {
        List<Object> tabPrimeTo100k = getPrime1000kFile();

        boolean[] testAlogo = Eratosthenes_Sieve.PrimeAlgorithmeThread(2, totalInt);
            for (int i = 0; i < totalInt; i++) {
                if (DEBUG)
                    System.out.println("Number test : " + i + ":" + testAlogo[i] + " Real :" + tabPrimeTo100k.contains(i));
                assertEquals("Number test : " + i + ":" + testAlogo[i] + " Real :" + tabPrimeTo100k.contains(i), testAlogo[i], tabPrimeTo100k.contains(i));
            }
    }

    @Test
    public void testTo100kImpairedThread() {

        List<Object> tabPrimeTo100k = getPrime1000kFile();

        boolean[] testAlogo = Eratosthenes_Sieve.PrimeAlgorithmeThread(5, totalInt);
        for (int i = 0; i < totalInt; i++) {
            if (DEBUG)
                System.out.println("Number test : " + i + ":" + testAlogo[i] + " Real:" + tabPrimeTo100k.contains(i));
            assertEquals("Number test : " + i + ":" + testAlogo[i] + " Real: " + tabPrimeTo100k.contains(i), testAlogo[i], tabPrimeTo100k.contains(i));
        }

    }

    public List<Object> getPrime1000kFile() {
        List<Object> tabPrimeTo100k = new ArrayList<>();
        String line = "";
        Path currentRelativePath = Paths.get("");
        String file = currentRelativePath.toAbsolutePath().toString() + "/res/primes-to-100k.txt";
        try {
            BufferedReader buff = new BufferedReader(new FileReader(file));
            while ((line = buff.readLine()) != null) {
                tabPrimeTo100k.add(Integer.valueOf(line));
            }
            //on ajoute 0 ,1 et 2 qui manque
            tabPrimeTo100k.add(0);
            tabPrimeTo100k.add(1);
            tabPrimeTo100k.add(2);
        } catch (Exception e) {
            assertFalse("erreur lecture de fichier", false);
        }
        return tabPrimeTo100k;
    }
}
