import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONF_CONST = 1.96;
    
    private double[] thresholds;
    private int t;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        thresholds = new double[trials];
        t = trials;
        int totalSites = n * n;
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int randRow = StdRandom.uniformInt(n) + 1;
                int randCol = StdRandom.uniformInt(n) + 1;
                p.open(randRow, randCol);
            }
            thresholds[i] = p.numberOfOpenSites() / (double) totalSites;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double sqrtTimes = Math.sqrt(t);
        return mean() - ((CONF_CONST * stddev()) / sqrtTimes);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double sqrtTimes = Math.sqrt(t);
        return mean() + ((CONF_CONST * stddev()) / sqrtTimes);
    }

   // test client (see below)
   public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        StdOut.printf("mean\t\t\t= %f", percolationStats.mean());
        StdOut.println();
        StdOut.printf("stddev\t\t\t= %f", percolationStats.stddev());
        StdOut.println();
        StdOut.printf("95%% confidence interval = [%f, %f]", percolationStats.confidenceLo(), percolationStats.confidenceHi());
   }

}
