
import java.util.ArrayList;
import java.util.Arrays;

public class seqFactorization {
    

    public static ArrayList<Long> factorize(long number, Oblig3Precode precode, int[] primes, boolean writeToPrecode) {
        ArrayList<Long> factors = new ArrayList<>(); 

        long base = number;

        for (int prime : primes) {
            while (number % prime == 0) {
                factors.add((long) prime);
                if (writeToPrecode) {
                    precode.addFactor(base, (long) prime);
                }
                number /= prime;
            }
        }

        if (number > 1 && writeToPrecode) {
            precode.addFactor(base, number);
        }

        return factors;
    }

    public static double[] multiFactorize(int n, SieveOfEratosthenes soe, Oblig3Precode precode, int numRuns) {
        long baseNumber = (long) n * (long) n;
        int numbersToFactorize = 100;

        double[] timeInMilliArray = new double[numRuns];
        int[] primes = soe.getPrimes();

        for (int i = 0; i < numRuns; i++) {
            double startTime = System.nanoTime();

            for (int j = 0; j < numbersToFactorize; j++) {
                long currentNumber = Math.max(1, baseNumber - j);
                boolean write = (i == 0);
                factorize(currentNumber, precode, primes, write);
            }

            double endTime = System.nanoTime();
            double durationInNano = endTime - startTime;
            double durationInMilli = durationInNano / 1_000_000;

            System.out.println("Execution time in nanoseconds: " + durationInNano);
            System.out.println("Execution time in milliseconds: " + durationInMilli);
            timeInMilliArray[i] = durationInMilli;
        }

        return timeInMilliArray;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int numRuns = Integer.parseInt(args[1]);

        SieveOfEratosthenes soe = new SieveOfEratosthenes(n);

        long currentNumber = (long) n * (long) n;
        Oblig3Precode precode = new Oblig3Precode((int) currentNumber);

        double[] timeInMilliArray = multiFactorize(n, soe, precode, numRuns);

        Arrays.sort(timeInMilliArray);
        if (numRuns % 2 == 0) {
            System.out.println((timeInMilliArray[numRuns/2 - 1] + timeInMilliArray[numRuns/2]) / 2);
        } else {
            System.out.println(timeInMilliArray[numRuns/2]);
        }

        precode.writeFactors();
    }
}
