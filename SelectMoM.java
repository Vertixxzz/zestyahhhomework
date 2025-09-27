import java.util.*;

public final class SelectMoM {
    private SelectMoM() {}

    public static int select(int[] a, int k) {
        if (a == null || a.length == 0) throw new IllegalArgumentException("empty");
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        return select(a, 0, a.length - 1, k);
    }

    private static int select(int[] a, int lo, int hi, int k) {
        while (true) {
            if (lo == hi) return a[lo];
            int pivot = medianOfMedians(a, lo, hi);

            int lt = lo, i = lo, gt = hi;
            while (i <= gt) {
                if (a[i] < pivot) { swap(a, lt, i); lt++; i++; }
                else if (a[i] > pivot) { swap(a, i, gt); gt--; }
                else i++;
            }
            if (k < lt) hi = lt - 1;
            else if (k > gt) lo = gt + 1;
            else return pivot;
        }
    }

    private static int medianOfMedians(int[] a, int lo, int hi) {
        int n = hi - lo + 1;
        if (n <= 5) {
            insertionSort(a, lo, hi);
            return a[lo + n / 2];
        }
        int numGroups = (n + 4) / 5;
        for (int g = 0; g < numGroups; g++) {
            int start = lo + g * 5;
            int end = Math.min(start + 4, hi);
            insertionSort(a, start, end);
            int medianIndex = start + (end - start) / 2;
            swap(a, lo + g, medianIndex);
        }
        int medianOfMedsRank = lo + (numGroups - 1) / 2;
        return select(a, lo, lo + numGroups - 1, medianOfMedsRank);
    }

    private static void insertionSort(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            int v = a[i], j = i - 1;
            while (j >= lo && a[j] > v) { a[j + 1] = a[j]; j--; }
            a[j + 1] = v;
        }
    }

    private static void swap(int[] a, int i, int j) {
        if (i == j) return;
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }
}
