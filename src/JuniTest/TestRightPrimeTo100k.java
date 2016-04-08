package JuniTest;

import Prime.PrimeMain;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Thomas on 08/04/2016.
 */
public class TestRightPrimeTo100k extends TestCase {
    public final boolean DEBUG = false;

    @Test
    public void testTo100kALGO1() {
        try {
            List<Object> tabPrimeTo100k = new ArrayList<>();
            String line = "";
            Path currentRelativePath = Paths.get("");
            String file = currentRelativePath.toAbsolutePath().toString() + "/res/primes-to-100k.txt";
            BufferedReader buff = new BufferedReader(new FileReader(file));
            while ((line = buff.readLine()) != null) {
                tabPrimeTo100k.add(Integer.valueOf(line));
            }
            for (int i = 2; i < 1000; i++) {
                if (DEBUG)
                    System.out.println("Number test : " + i + ":" + PrimeMain.PrimeAlgorithme2(i) + "Real:" + tabPrimeTo100k.contains(i));
                assertEquals(PrimeMain.PrimeAlgorithme(i), tabPrimeTo100k.contains(i));
            }
        } catch (Exception e) {
            assertFalse("erreur lecture de fichier", false);
        }
    }

    @Test
    public void testTo100kALGO2() {
        try {
            List<Object> tabPrimeTo100k = new ArrayList<>();
            String line = "";
            Path currentRelativePath = Paths.get("");
            String file = currentRelativePath.toAbsolutePath().toString() + "/res/primes-to-100k.txt";
            BufferedReader buff = new BufferedReader(new FileReader(file));
            while ((line = buff.readLine()) != null) {
                tabPrimeTo100k.add(Integer.valueOf(line));
            }
            for (int i = 2; i < 1000; i++) {
                if (DEBUG)
                    System.out.println("Number test : " + i + ":" + PrimeMain.PrimeAlgorithme2(i) + "Real:" + tabPrimeTo100k.contains(i));
                assertEquals(PrimeMain.PrimeAlgorithme2(i), tabPrimeTo100k.contains(i));
            }
        } catch (Exception e) {
            assertFalse("erreur lecture de fichier", false);
        }
    }
}
