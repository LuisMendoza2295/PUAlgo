import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF matrix;
    private WeightedQuickUnionUF nonBottomMatrix;
    private boolean[] sites;
    private int n;
    private int vTop;
    private int vBottom;
    private int sitesOpen = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        sites = new boolean[n * n];
        vTop = 0;
        vBottom = (n * n) + 1;
        matrix = new WeightedQuickUnionUF(vBottom + 1);
        nonBottomMatrix = new WeightedQuickUnionUF(vBottom);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkInputs(row, col);
        int p = getFlatPosition(row, col);
        if (!isOpen(row, col)) {
            int sitePosition = getSitesPosition(row, col);
            sites[sitePosition] = true;
            sitesOpen++;
        }
        if (row == 1) {
            matrix.union(vTop, p);
            nonBottomMatrix.union(vTop, p);
        }
        if (row == n) {
            matrix.union(vBottom, p);
        }
        if (row - 1 > 0) {
            if (isOpen(row - 1, col)) {
                int q = getFlatPosition(row - 1, col);
                matrix.union(p, q);
                nonBottomMatrix.union(p, q);
            }
        }
        if (row + 1 <= n) {
            if (isOpen(row + 1, col)) {
                int q = getFlatPosition(row + 1, col);
                matrix.union(p, q);
                nonBottomMatrix.union(p, q);
            }
        }
        if (col - 1 > 0) {
            if (isOpen(row, col - 1)) {
                int q = getFlatPosition(row, col - 1);
                matrix.union(p, q);
                nonBottomMatrix.union(p, q);
            }
        }
        if (col + 1 <= n) {
            if (isOpen(row, col + 1)) {
                int q = getFlatPosition(row, col + 1);
                matrix.union(p, q);
                nonBottomMatrix.union(p, q);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkInputs(row, col);
        int position = getSitesPosition(row, col);
        return sites[position];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkInputs(row, col);
        int p = getFlatPosition(row, col);
        return isOpen(row, col) && nonBottomMatrix.find(p) == nonBottomMatrix.find(vTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return sitesOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return matrix.find(vTop) == matrix.find(vBottom);
    }

    // Each row is an offset of "n" elements
    private int getFlatPosition(int row, int col) {
        int offset = (row - 1) * n;
        return offset + col;
    }

    // Sites index starts at 0, and flat position starts at 1 (because of vTop being at 0), then sitesPosition = flatPosition-1
    private int getSitesPosition(int row, int col) {
        return getFlatPosition(row, col) - 1;
    }

    private void checkInputs(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        p.open(1, 1);
        p.open(1, 2);
        p.open(1, 3);
        p.open(2, 3);
        p.open(3, 1);
        p.open(3, 3);

        StdOut.print("isFull " + p.percolates());
    }
}
