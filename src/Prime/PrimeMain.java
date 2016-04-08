package Prime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 08/04/2016.
 */
public class PrimeMain {

    public static void main(String[] args) {
        System.out.println(PrimeAlgorithme2(13));
    }

    public static boolean[] PrimeAlgorithme(int n) {
        boolean [] A = new boolean[n];
        for(int i = 0 ; i < n ; i++)
            A[i] = true;
        int sqrtInt = (int) Math.sqrt(n);
        for (int i = 2 ; i <= sqrtInt; i++)
        {
            if (A[i] == true) {
                for(int j = (int) Math.pow(i,2); j <= n ; j+=i)
                {
                    A[j] = false;
                }
            }
        }
        return A;
    }

    public static boolean PrimeAlgorithme2(int number) {
        int i = 2;
        if (number == 2)
            return true;

        while (i < number) {
            if (number % i == 0) {
                return false;
            }
            i++;
        }
        return true;
    }
}
