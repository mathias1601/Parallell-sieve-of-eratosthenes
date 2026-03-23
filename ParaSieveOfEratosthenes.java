
import java.util.ArrayList;
import java.util.Arrays;

public class ParaSieveOfEratosthenes {
    

    static void printPrimes(ArrayList<Integer> primes) {
        for (int prime : primes)
        System.out.println(prime);
    }

    private static boolean isPrime(int num, byte[] oddNumbers) {
        int bitIndex = (num % 16) / 2;
        int byteIndex = num / 16;

        return (oddNumbers[byteIndex] & (1 << bitIndex)) == 0;
    }

    private static ArrayList<Integer> runParaSieve(int n, int numThreads, SieveOfEratosthenes soe) {

        byte[] oddNumbers = new byte[(n / 16) + 1];

        int[] primes = soe.getPrimes();
        Thread[] listOfThreads = new Thread[numThreads];

        int numBytes = oddNumbers.length;
        int bytesPerThread = numBytes / numThreads;
        int remainder = numBytes % numThreads;

        int byteStart = 0;
        for (int i = 0; i < numThreads; i++) {
            int size = bytesPerThread + (i < remainder ? 1 : 0);
            int byteEnd = byteStart + size;

            int segmentStartNumber = byteStart * 16 + 1;
            int segmentEndNumber = byteEnd * 16 + 15;

            listOfThreads[i] = new SieveThread(n, primes, segmentStartNumber, segmentEndNumber, oddNumbers);

            byteStart = byteEnd;
        }
        
        for (Thread thread: listOfThreads) {
            thread.start();
        }

        for (Thread thread: listOfThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Integer> listOfPrimes = new ArrayList<>();
        listOfPrimes.add(2);

        for (Thread thread : listOfThreads) {
            listOfPrimes.addAll(((SieveThread) thread).localPrimes);
        }

        return listOfPrimes;
    }

    private static double[] multiRun(int n, int root, int numRuns, int numThreads) {

        double[] timeInMilliArray = new double[numRuns];
        SieveOfEratosthenes soe = new SieveOfEratosthenes(root);
        for (int i = 0; i < numRuns; i++) {
            double startTime = System.nanoTime();

            ArrayList<Integer> primes = runParaSieve(n, numThreads, soe);

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
        int k = Integer.parseInt(args[1]);
        int numRuns = Integer.parseInt(args[2]);
        
        int numThreads;

        if (k == 0) {
            numThreads = 4;
        }
        else {
            numThreads = k;
        }

        int root = (int) Math.sqrt(n);

        double[] timeInMilliArray = multiRun(n, root, numRuns, numThreads);

        Arrays.sort(timeInMilliArray);

		if (numRuns % 2 == 0) {
			System.out.println( (timeInMilliArray[numRuns/2 - 1] + timeInMilliArray[numRuns/2]) / 2);
		}
		else {
			System.out.println(timeInMilliArray[numRuns/2]);
		}
    }
    
}
