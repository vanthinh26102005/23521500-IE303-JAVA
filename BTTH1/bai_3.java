import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class bai_3 {
    static class Point implements Comparable<Point> {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point other) {
            if (x != other.x) return x - other.x;
            return y - other.y;
        }
    }

    static long cross(Point a, Point b, Point c) {
        return 1L * (b.x - a.x) * (c.y - a.y) - 1L * (b.y - a.y) * (c.x - a.x);
    }

    static List<Point> convexHull(Point[] points) {
        Arrays.sort(points);

        List<Point> unique = new ArrayList<>();
        for (Point p : points) {
            if (unique.isEmpty()) {
                unique.add(p);
                continue;
            }
            Point last = unique.get(unique.size() - 1);
            if (last.x != p.x || last.y != p.y) unique.add(p);
        }

        int n = unique.size();
        if (n <= 2) return unique;

        List<Point> lower = new ArrayList<>();
        for (Point p : unique) {
            while (lower.size() >= 2
                    && cross(lower.get(lower.size() - 2), lower.get(lower.size() - 1), p) <= 0) {
                lower.remove(lower.size() - 1);
            }
            lower.add(p);
        }

        List<Point> upper = new ArrayList<>();
        for (int i = n - 1; i >= 0; i--) {
            Point p = unique.get(i);
            while (upper.size() >= 2
                    && cross(upper.get(upper.size() - 2), upper.get(upper.size() - 1), p) <= 0) {
                upper.remove(upper.size() - 1);
            }
            upper.add(p);
        }

        lower.remove(lower.size() - 1);
        upper.remove(upper.size() - 1);
        lower.addAll(upper);
        return lower;
    }

    static List<Point> toClockwiseFromLeftmostTop(List<Point> hullCcw) {
        int n = hullCcw.size();
        if (n <= 1) return hullCcw;

        int start = 0;
        for (int i = 1; i < n; i++) {
            Point cur = hullCcw.get(i);
            Point best = hullCcw.get(start);
            if (cur.x < best.x || (cur.x == best.x && cur.y > best.y)) start = i;
        }

        List<Point> result = new ArrayList<>();
        for (int k = 0; k < n; k++) {
            int idx = (start - k + n) % n;
            result.add(hullCcw.get(idx));
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            points[i] = new Point(x, y);
        }

        List<Point> hull = convexHull(points);
        List<Point> warningStations = toClockwiseFromLeftmostTop(hull);

        for (Point p : warningStations) {
            System.out.println(p.x + " " + p.y);
        }
        sc.close();
    }
}
