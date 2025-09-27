import java.util.*;

public final class MergeSort {
    private MergeSort() {}

    public static void sort(int[] a) {
        if (a == null || a.length <= 1) return;
        int[] buf = new int[a.length];
        sort(a, 0, a.length, buf);
    }

    private static void sort(int[] a, int lo, int hi, int[] buf) {
        int n = hi - lo;
        if (n <= 1) return;
        int mid = lo + (n >>> 1);
        sort(a, lo, mid, buf);
        sort(a, mid, hi, buf);
        if (a[mid - 1] <= a[mid]) return;
        System.arraycopy(a, lo, buf, lo, hi - lo);
        int i = lo, j = mid, k = lo;
        while (i < mid && j < hi) {
            if (buf[i] <= buf[j]) a[k++] = buf[i++];
            else a[k++] = buf[j++];
        }
        while (i < mid) a[k++] = buf[i++];
        while (j < hi) a[k++] = buf[j++];
    }
}
