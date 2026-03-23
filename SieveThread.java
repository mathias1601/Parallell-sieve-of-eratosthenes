
public class SieveThread extends Thread {
    
    int n;
    int[] listOfPrimes;
    int segmentStartNumber;
    int segmentEndNumber;
    byte[] oddNumbers;

    public SieveThread(int n, int[] listOfPrimes, int segmentStartNumber, int segmentEndNumber, byte[] oddNumbers) {
        this.n = n;
        this.listOfPrimes = listOfPrimes;
        this.segmentStartNumber = segmentStartNumber;
        this.segmentEndNumber = segmentEndNumber;
        this.oddNumbers = oddNumbers;
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

    @Override
    public void run() {
        for (int i = 1; i < listOfPrimes.length; i++) {
            markPrimes(listOfPrimes[i]);
        }
    }

}
