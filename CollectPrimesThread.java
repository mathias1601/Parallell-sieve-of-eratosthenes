import java.util.List;

public class CollectPrimesThread extends Thread {

    List<Integer> primes;
    int segmentStartNumber;
    int segmentEndNumber;
    byte[] oddNumbers;

    public CollectPrimesThread(List<Integer> primes, int segmentStartNumber, int segmentEndNumber, byte[] oddNumbers) {
        this.primes = primes;
        this.segmentStartNumber = segmentStartNumber;
        this.segmentEndNumber = segmentEndNumber;
        this.oddNumbers = oddNumbers;
    }

    private static boolean isPrime(int num, byte[] oddNumbers) {
        int bitIndex = (num % 16) / 2;
        int byteIndex = num / 16;

        return (oddNumbers[byteIndex] & (1 << bitIndex)) == 0;
    }

    @Override
    public void run() {
    
        for (int i = segmentStartNumber; i <= segmentEndNumber; i += 2)
            if (isPrime(i, oddNumbers))
                primes.add(i);
    }
}
