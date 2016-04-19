package JuniTest;

import Prime.Eratosthenes_Sieve;
import Prime.PrimeMain;
import de.progra.charting.ChartEncoder;
import de.progra.charting.DefaultChart;
import de.progra.charting.model.DefaultChartDataModel;
import de.progra.charting.model.FunctionPlotter;
import de.progra.charting.model.ObjectChartDataModel;
import de.progra.charting.render.LineChartRenderer;
import de.progra.charting.render.PlotChartRenderer;
import de.progra.charting.render.RowColorModel;
import de.progra.charting.swing.ChartPanel;
import junit.framework.TestCase;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileOutputStream;
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
    public static final boolean DEBUG = false;
    public static final int totalInt = 100000;

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

        boolean[] testAlogo = Eratosthenes_Sieve.PrimeAlgorithmeThread(4, totalInt);
            for (int i = 0; i < totalInt; i++) {
                if (DEBUG)
                    System.out.println("Number test : " + i + ":" + testAlogo[i] + " Real :" + tabPrimeTo100k.contains(i));
                assertEquals("Number test : " + i + ":" + testAlogo[i] + " Real :" + tabPrimeTo100k.contains(i), testAlogo[i], tabPrimeTo100k.contains(i));
            }
    }

    @Test
    public void testTo100kImpairedThread() {

        List<Object> tabPrimeTo100k = getPrime1000kFile();

        boolean[] testAlogo = Eratosthenes_Sieve.PrimeAlgorithmeThread(3, totalInt);
        for (int i = 0; i < totalInt; i++) {
            if (DEBUG)
                System.out.println("Number test : " + i + ":" + testAlogo[i] + " Real:" + tabPrimeTo100k.contains(i));
            assertEquals("Number test : " + i + ":" + testAlogo[i] + " Real: " + tabPrimeTo100k.contains(i), testAlogo[i], tabPrimeTo100k.contains(i));
        }

    }

    @Test
    public static void perfromanceBenchmark(){
        long start=0,end=0;

        //mono Thread
        double[][] time = new double[3][100];
        double[] columns = new double[100];
        for(int i = 0 ; i<100 ; i++) {
            int nbPrimeTest = (int) (500000*Math.random());
            columns[i]=nbPrimeTest;
            start = System.currentTimeMillis();
            Eratosthenes_Sieve.PrimeAlgorithme(nbPrimeTest);
            end = System.currentTimeMillis();
            time[0][i]= (end - start);

        //four Thread


            start = System.currentTimeMillis();
            Eratosthenes_Sieve.PrimeAlgorithmeThread(4, nbPrimeTest);
            end = System.currentTimeMillis();
            time[1][i]= (end - start);

        //ten Thread


            start = System.currentTimeMillis();
            Eratosthenes_Sieve.PrimeAlgorithmeThread(10, nbPrimeTest);
            end = System.currentTimeMillis();
            time[2][i]= (end - start);
            System.out.println(i+"%"+time[0][i]+"|"+time[1][i]+"|"+time[2][i]);
        }

        JFrame frame = new JFrame();
frame.setBounds(100,100,256,256);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());


        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout()); // Used to center the panel
        content.add(p, BorderLayout.SOUTH);



               String[] rows = {"mono Thread","four Thread","ten Thread"};          // Create data set title

               String title = "Temps en milisec en fonction du nombre recherché";          // Create diagram title

                int width = 640;                        // Image size
               int height = 480;

                // Create data model
        DefaultChartDataModel data = new DefaultChartDataModel(time, columns, rows);

                // Create chart with default coordinate system
        ChartPanel c = new ChartPanel(data, title, DefaultChart.LINEAR_X_LINEAR_Y);

                // Add a line chart renderer
             //   c.addChartRenderer(new PlotChartRenderer(c,data)new LineChartRenderer(c.getCoordSystem(), data), 1);
                c.addChartRenderer(new PlotChartRenderer(c.getCoordSystem(),data), 1);
                c.addChartRenderer(new PlotChartRenderer(c.getCoordSystem(),data), 2);
                c.addChartRenderer(new PlotChartRenderer(c.getCoordSystem(),data), 3);

                // Set the chart size
                c.setBounds(new Rectangle(0, 0, width, height));

                // Export the chart as a PNG image
                try {
                        ChartEncoder.createEncodedImage(new FileOutputStream(System.getProperty("user.home")+"/first.png"), c, "png");
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
        content.add(c, BorderLayout.CENTER);
        frame.setVisible(true);
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
