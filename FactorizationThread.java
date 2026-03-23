
import java.util.List;

public class FactorizationThread extends Thread {

    public List<Long> factorList;
    public long number;
    public int[] primes;

    public int start;
    public int end;

    public FactorizationThread(List<Long> factorList, long number, int start, int end, int[] primes) {
        this.factorList = factorList;
        this.number = number;
        this.primes = primes;

        this.start = start;
        this.end = end;
    }

    public void factorize() {
        
        for (int i = start; i < end; i++) {
            while (number % primes[i] == 0) {
                factorList.add((long) primes[i]);
                number /= primes[i];
            }
        } 
    }

    @Override
    public void run() {
        factorize();
    }
    
}
