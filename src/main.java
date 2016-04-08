/**
 * Created by Thomas on 08/04/2016.
 */
public class main {

    public static void main(String[] args) {
        System.out.println();
    }

    public boolean PrimeAlgorithme(int number) {
        return false;
    }

    public boolean PrimeAlgorithme2(int number) {
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
