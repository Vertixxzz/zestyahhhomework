import java.util.*;

public final class QuickSort {
    private static final Random RNG = new Random();

    private QuickSort() {}

    public static void sort(int[] a) {
        if (a == null || a.length <= 1) return;
        sort(a, 0, a.length - 1);
    }

    private static void sort(int[] a, int lo, int hi) {
        while (lo < hi) {
            int p = lo + RNG.nextInt(hi - lo + 1);
            int t = a[lo]; a[lo] = a[p]; a[p] = t;
            int pivot = a[lo];

            int lt = lo, i = lo + 1, gt = hi;
            while (i <= gt) {
                if (a[i] < pivot) { t = a[lt]; a[lt] = a[i]; a[i] = t; lt++; i++; }
                else if (a[i] > pivot) { t = a[i]; a[i] = a[gt]; a[gt] = t; gt--; }
                else i++;
            }
            if (lt - lo < hi - gt) {
                if (lo < lt - 1) sort(a, lo, lt - 1);
                lo = gt + 1;
            } else {
                if (gt + 1 < hi) sort(a, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }
}
