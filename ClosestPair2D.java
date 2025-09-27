import java.util.*;

public final class ClosestPair2D {
    private ClosestPair2D() {}

    public static final class Point {
        public final double x, y;
        public Point(double x, double y) { this.x = x; this.y = y; }
        public String toString() { return "(" + x + "," + y + ")"; }
    }

    public static final class Result {
        public final Point p, q;
        public final double distance;
        public Result(Point p, Point q, double distance) { this.p = p; this.q = q; this.distance = distance; }
        public String toString() { return p + " — " + q + " : " + distance; }
    }

    public static Result closestPair(Point[] pts) {
        if (pts == null || pts.length < 2) throw new IllegalArgumentException("Need at least 2 points");
        Point[] px = pts.clone();
        Arrays.sort(px, Comparator.comparingDouble(p -> p.x));
        Point[] py = pts.clone();
        Arrays.sort(py, Comparator.comparingDouble(p -> p.y));
        return solve(px, py);
    }

    private static Result solve(Point[] px, Point[] py) {
        int n = px.length;
        if (n <= 3) return brute(px);
        int mid = n / 2;
        double midx = px[mid].x;
        Point[] leftPx = Arrays.copyOfRange(px, 0, mid);
        Point[] rightPx = Arrays.copyOfRange(px, mid, n);
        Set<Point> leftSet = new HashSet<>(Arrays.asList(leftPx));
        Point[] leftPy = new Point[mid];
        Point[] rightPy = new Point[n - mid];
        int li = 0, ri = 0;
        for (Point p : py) {
            if (leftSet.contains(p)) leftPy[li++] = p; else rightPy[ri++] = p;
        }
        if (li != leftPy.length) leftPy = Arrays.copyOf(leftPy, li);
        if (ri != rightPy.length) rightPy = Arrays.copyOf(rightPy, ri);
        Result leftRes = solve(leftPx, leftPy);
        Result rightRes = solve(rightPx, rightPy);
        Result best = leftRes.distance <= rightRes.distance ? leftRes : rightRes;
        double delta = best.distance;
        List<Point> strip = new ArrayList<>();
        for (Point p : py) if (Math.abs(p.x - midx) < delta) strip.add(p);
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < delta && j <= i + 7; j++) {
                double d = dist(strip.get(i), strip.get(j));
                if (d < best.distance) best = new Result(strip.get(i), strip.get(j), d);
            }
        }
        return best;
    }

    private static Result brute(Point[] pts) {
        double best = Double.POSITIVE_INFINITY;
        Point a = null, b = null;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double d = dist(pts[i], pts[j]);
                if (d < best) { best = d; a = pts[i]; b = pts[j]; }
            }
        }
        return new Result(a, b, best);
    }

    private static double dist(Point a, Point b) {
        double dx = a.x - b.x, dy = a.y - b.y;
        return Math.hypot(dx, dy);
    }
}
