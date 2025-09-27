import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<String, String> opts = parseArgs(args);
        String algo = opts.getOrDefault("algo", "demo");
        int n = Integer.parseInt(opts.getOrDefault("n", "20"));
        long seed = Long.parseLong(opts.getOrDefault("seed", String.valueOf(System.nanoTime())));
        Random rnd = new Random(seed);

        switch (algo.toLowerCase()) {
            case "mergesort": {
                int[] a = randomArray(n, rnd);
                System.out.println("Before: " + Arrays.toString(a));
                MergeSort.sort(a);
                System.out.println("After :  " + Arrays.toString(a));
                break;
            }
            case "quicksort": {
                int[] a = randomArray(n, rnd);
                System.out.println("Before: " + Arrays.toString(a));
                QuickSort.sort(a);
                System.out.println("After :  " + Arrays.toString(a));
                break;
            }
            case "select": {
                int[] a = randomArray(n, rnd);
                int k = Integer.parseInt(opts.getOrDefault("k", String.valueOf(n / 2)));
                System.out.println("Array:  " + Arrays.toString(a));
                int kth = SelectMoM.select(a, k);
                System.out.println(k + "-th order statistic: " + kth);
                Arrays.sort(a);
                System.out.println("(Check vs sort):       " + a[k]);
                break;
            }
            case "closest": {
                ClosestPair2D.Point[] pts = randomPoints(n, rnd);
                ClosestPair2D.Result res = ClosestPair2D.closestPair(pts);
                System.out.println("Closest pair: " + res);
                break;
            }
            default: {
                System.out.println("Use algo=mergesort|quicksort|select|closest");
                int[] a = randomArray(n, rnd);
                int[] b = a.clone();
                int[] c = a.clone();
                MergeSort.sort(b);
                QuickSort.sort(c);
                int k = n / 2;
                int kth = SelectMoM.select(a.clone(), k);
                ClosestPair2D.Point[] pts = randomPoints(Math.max(10, n / 2), rnd);
                ClosestPair2D.Result res = ClosestPair2D.closestPair(pts);
                System.out.println("Original : " + Arrays.toString(a));
                System.out.println("MergeSort: " + Arrays.toString(b));
                System.out.println("QuickSort: " + Arrays.toString(c));
                System.out.println("Select k=" + k + ": " + kth);
                System.out.println("Closest :  " + res);
            }
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> m = new HashMap<>();
        for (String s : args) {
            int i = s.indexOf('=');
            if (i > 0) m.put(s.substring(0, i).trim(), s.substring(i + 1).trim());
        }
        return m;
    }

    private static int[] randomArray(int n, Random rnd) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt(1_000_000) - 500_000;
        return a;
    }

    private static ClosestPair2D.Point[] randomPoints(int n, Random rnd) {
        ClosestPair2D.Point[] pts = new ClosestPair2D.Point[n];
        for (int i = 0; i < n; i++) pts[i] = new ClosestPair2D.Point(rnd.nextDouble() * 1000.0, rnd.nextDouble() * 1000.0);
        return pts;
    }
}
