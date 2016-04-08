package Prime;

/**
 * Created by Guillaume on 08/04/2016.
 */
public class Eratosthenes_Sieve {

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
}
