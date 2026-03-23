
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParaFactorization {

    public static HashMap<Long, ArrayList<Long>> runParaFactorization(Oblig3Precode precode, long number, int[] primes, int threadCount){

        FactorizationThread[] threadList = new FactorizationThread[threadCount];
        HashMap<Long, ArrayList<Long>> numbersAndFactors = new HashMap<>();


        for (int i = 0; i < 100; i++) {
            
            long currentNumber = number - i;
            
            ArrayList<Long> factorList = new ArrayList<>();
            List<Long> syncedFactorList = Collections.synchronizedList(factorList);

            for (int t = 0; t < threadCount; t++) {
                int start = t * primes.length / threadCount;
                int end = (t + 1) * primes.length / threadCount;

                threadList[t] = new FactorizationThread(syncedFactorList, currentNumber, start, end, primes);

                threadList[t].start();
            }

            for (Thread thread: threadList) {
                try {
                    thread.join();
                } catch (InterruptedException ex) {
                    System.getLogger(ParaFactorization.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
            
            long leftover = currentNumber;

            for (Long factor: factorList) {
                leftover /= factor;
            }

            if (leftover > 1) {
               factorList.add(leftover);
            }

            numbersAndFactors.put(currentNumber, factorList);
        }


        return numbersAndFactors; 

    }

    public static double[] multiRunFactorization(Oblig3Precode precode, long number, int[] primes, int threadCount, int numRuns) {
        double[] timeInMilliArray = new double[numRuns];

        HashMap<Long, ArrayList<Long>> numbersAndFactors = new HashMap<>();

        for (int i = 0; i < numRuns; i++) {
            double startTime = System.nanoTime();

            numbersAndFactors = runParaFactorization(precode, number, primes, threadCount);

            double endTime = System.nanoTime();

            double durationInNano = endTime - startTime;
            double durationInMilli = durationInNano / 1_000_000;

            System.out.println("Execution time in nanoseconds: " + durationInNano);
            System.out.println("Execution time in milliseconds: " + durationInMilli);

            timeInMilliArray[i] = durationInMilli;
        }

        for (Map.Entry<Long, ArrayList<Long>> factorizedNum: numbersAndFactors.entrySet()) {
            Long base = factorizedNum.getKey();

            for (Long num : factorizedNum.getValue()) {
                precode.addFactor(base, num);
            }
        }

        return timeInMilliArray;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]); //amount of threads
        int numRuns = Integer.parseInt(args[2]);

        SieveOfEratosthenes soe = new SieveOfEratosthenes(n);
        int[] primes = soe.getPrimes();

        long currentNumber = (long) n * (long) n;

        Oblig3Precode precode = new Oblig3Precode((int) currentNumber);
        
        double[] timeInMilliArray = multiRunFactorization(precode, currentNumber, primes, k, numRuns);

        Arrays.sort(timeInMilliArray);

		if (numRuns % 2 == 0) {
			System.out.println( (timeInMilliArray[numRuns/2 - 1] + timeInMilliArray[numRuns/2]) / 2);
		}
		else {
			System.out.println(timeInMilliArray[numRuns/2]);
		}
        

        precode.writeFactors();
    }
}
