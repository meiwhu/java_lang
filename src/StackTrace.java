import java.util.Scanner;
import java.util.logging.Logger;

public class StackTrace {
    public static Logger logger = Logger.getLogger(StackTrace.class.getName());

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("Enter n:");
            int n = scanner.nextInt();
            factorial(n);
        }
    }

    public static int factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n should be positive");
        }
        logger.info("factorial(" + n + ")");
        Thread.dumpStack();
        int ret;
        if (n <= 1) ret = 1;
        else ret = n * factorial(n - 1);
        logger.info("return " + ret);
        return ret;
    }
}

class Pair<T extends Object &
        Comparable<T> & Cloneable> {
    private final T min;
    private final T max;

    public Pair(T a, T b) {
        this.min = a;
        this.max = b;
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    public T[] sort(T[] a) {
        return a;
    }
}
