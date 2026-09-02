import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    // Segment Tree to handle Range Max Updates for fully contained palindromes
    static class SegmentTree {
        int[] tree;
        int[] lazy;
        int n;

        SegmentTree(int n) {
            this.n = n;
            tree = new int[4 * n];
            lazy = new int[4 * n];
        }

        void update(int node, int start, int end, int l, int r, int val) {
            if (lazy[node] != 0) {
                tree[node] = Math.max(tree[node], lazy[node]);
                if (start != end) {
                    lazy[2 * node] = Math.max(lazy[2 * node], lazy[node]);
                    lazy[2 * node + 1] = Math.max(lazy[2 * node + 1], lazy[node]);
                }
                lazy[node] = 0;
            }

            if (start > end || start > r || end < l) return;

            if (start >= l && end <= r) {
                tree[node] = Math.max(tree[node], val);
                if (start != end) {
                    lazy[2 * node] = Math.max(lazy[2 * node], val);
                    lazy[2 * node + 1] = Math.max(lazy[2 * node + 1], val);
                }
                return;
            }

            int mid = (start + end) / 2;
            update(2 * node, start, mid, l, r, val);
            update(2 * node + 1, mid + 1, end, l, r, val);
            tree[node] = Math.max(tree[2 * node], tree[2 * node + 1]);
        }

        int query(int node, int start, int end, int idx) {
            if (lazy[node] != 0) {
                tree[node] = Math.max(tree[node], lazy[node]);
                if (start != end) {
                    lazy[2 * node] = Math.max(lazy[2 * node], lazy[node]);
                    lazy[2 * node + 1] = Math.max(lazy[2 * node + 1], lazy[node]);
                }
                lazy[node] = 0;
            }

            if (start == end) return tree[node];

            int mid = (start + end) / 2;
            if (idx <= mid) return query(2 * node, start, mid, idx);
            else return query(2 * node + 1, mid + 1, end, idx);
        }
    }

    public static List<Integer> circularPalindromes(String s) {
        int n = s.length();
        String t = s + s;

        // Build transformed string for Manacher's Algorithm
        StringBuilder sb = new StringBuilder();
        sb.append('#');
        for (int i = 0; i < t.length(); i++) {
            sb.append(t.charAt(i)).append('#');
        }
        String m = sb.toString();
        int mLen = m.length();
        int[] p = new int[mLen];

        int c = 0, r = 0;
        for (int i = 0; i < mLen; i++) {
            int mirror = 2 * c - i;
            if (i < r) {
                p[i] = Math.min(r - i, p[mirror]);
            }
            while (i - p[i] - 1 >= 0 && i + p[i] + 1 < mLen && m.charAt(i - p[i] - 1) == m.charAt(i + p[i] + 1)) {
                p[i]++;
            }
            if (i + p[i] > r) {
                c = i;
                r = i + p[i];
            }
        }

        SegmentTree segTree = new SegmentTree(n);
        int INF = 1000000000;
        int[] leftArr = new int[2 * n + 2];
        int[] rightArr = new int[2 * n + 2];
        Arrays.fill(leftArr, -INF);
        Arrays.fill(rightArr, -INF);

        for (int i = 0; i < mLen; i++) {
            int lenMax = p[i];
            int l_p = (i - lenMax) / 2;
            int r_p = (i + lenMax) / 2 - 1;

            // Palindromes cannot natively expand beyond length N in a single rotation window
            if (lenMax > n) {
                if (lenMax % 2 != n % 2) {
                    lenMax = n - 1;
                } else {
                    lenMax = n;
                }

                if (i % 2 == 1) { // Odd center
                    int ct = i / 2;
                    int rad = (lenMax - 1) / 2;
                    l_p = ct - rad;
                    r_p = ct + rad;
                } else { // Even center
                    int rad = lenMax / 2;
                    l_p = i / 2 - rad;
                    r_p = i / 2 + rad - 1;
                }
            }

            if (lenMax <= 0) continue;

            // 1. Fully Contained Range Updates
            int kStart = Math.max(0, r_p - n + 1);
            int kEnd = Math.min(n - 1, l_p);
            if (kStart <= kEnd) {
                segTree.update(1, 0, n - 1, kStart, kEnd, lenMax);
            }

            // 2. Left Chopped Map Setup
            leftArr[l_p] = Math.max(leftArr[l_p], lenMax + 2 * l_p);

            // 3. Right Chopped Map Setup
            rightArr[r_p] = Math.max(rightArr[r_p], lenMax - 2 * r_p);
        }

        // Propulate Left Chopped Bounds via Prefix Max Sweep
        int currMax = -INF;
        for (int i = 0; i < leftArr.length; i++) {
            currMax = Math.max(currMax, leftArr[i]);
            leftArr[i] = currMax;
        }

        // Propulate Right Chopped Bounds via Suffix Max Sweep
        currMax = -INF;
        for (int i = rightArr.length - 1; i >= 0; i--) {
            currMax = Math.max(currMax, rightArr[i]);
            rightArr[i] = currMax;
        }

        List<Integer> result = new ArrayList<>();
        for (int k = 0; k < n; k++) {
            int val1 = segTree.query(1, 0, n - 1, k);
            int val2 = leftArr[k] - 2 * k;
            int R = k + n - 1;
            int val3 = rightArr[R] + 2 * R;

            result.add(Math.max(val1, Math.max(val2, val3)));
        }

        return result;
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());
        String s = bufferedReader.readLine();

        List<Integer> result = Result.circularPalindromes(s);

        bufferedWriter.write(
            result.stream()
                .map(Object::toString)
                .collect(joining("\n"))
            + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}
