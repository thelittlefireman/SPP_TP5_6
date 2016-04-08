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
public class TestRightPrimeTo100k extends TestCase{
    @Test
    public void testTo100k(){
        try {
            List<Object> tabPrimeTo100k = new ArrayList<>();
            String line="";
            Path currentRelativePath = Paths.get("");
            String file = currentRelativePath.toAbsolutePath().toString()+"/res/primes-to-100k.txt";
            BufferedReader buff = new BufferedReader(new FileReader(file));
            while ((line = buff.readLine()) != null) {
                tabPrimeTo100k.add(Integer.valueOf(line));
            }
            for(int i=0; 1 < 1000 ; i++ ){
                assertEquals(PrimeMain.PrimeAlgorithme2(i) , tabPrimeTo100k.contains(i) );
            }
        }catch (Exception e){
            assertFalse("erreur lecture de fichier",false);
        }
    }
}
