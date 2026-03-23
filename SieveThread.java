
import java.util.ArrayList;

public class SieveThread extends Thread {
    
    int n;
    int[] listOfPrimes;
    int segmentStartNumber;
    int segmentEndNumber;
    byte[] oddNumbers;
    ArrayList<Integer> localPrimes;

    public SieveThread(int n, int[] listOfPrimes, int segmentStartNumber, int segmentEndNumber, byte[] oddNumbers) {
        this.n = n;
        this.listOfPrimes = listOfPrimes;
        this.segmentStartNumber = segmentStartNumber;
        this.segmentEndNumber = segmentEndNumber;
        this.oddNumbers = oddNumbers;
        this.localPrimes = new ArrayList<>();
    }
    

    private void markPrimes(int primeNumber) {
        long start = Math.max((long) primeNumber * primeNumber, (long) segmentStartNumber);

        long rem = start % primeNumber;
        if (rem != 0) {
            start += (primeNumber - rem);
        }

        // Ensure we only mark odd multiples (primeNumber is odd)
        if ((start & 1) == 0) {
            start += primeNumber;
        }

        long step = (long) primeNumber * 2;

        long end = Math.min((long) segmentEndNumber, (long) n);
        for (long num = start; num <= end; num += step) {
            mark((int) num);
        }
    }

    private void mark(int num) {
        int bitIndex = (num % 16) / 2;
        int byteIndex = num / 16;

        oddNumbers[byteIndex] |= (1 << bitIndex);
    }

    private boolean isPrime(int num) {
        int bitIndex = (num % 16) / 2;
        int byteIndex = num / 16;

        return (oddNumbers[byteIndex] & (1 << bitIndex)) == 0;
    }

    @Override
    public void run() {
        for (int i = 1; i < listOfPrimes.length; i++) {
            markPrimes(listOfPrimes[i]);
        }

        // Collect primes in this segment
        for (int i = Math.max(3, segmentStartNumber | 1); i <= segmentEndNumber && i <= n; i += 2) {
            if (isPrime(i)) {
                localPrimes.add(i);
            }
        }
    }

}
