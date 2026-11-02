// Pattern 4: One Pointer per Input (two arrays or two strings)
// Pointer i walks the first input, pointer j walks the second.
// Each step, move the pointer whose item is "behind" (smaller, or already matched).
// Run: java P4TwoInputs.java

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P4TwoInputs {

    // Template A: merge two sorted arrays into first, which has room at the end (LeetCode 88).
    // Fill from the BACK so we never overwrite a number we still need.
    // Time O(m + n), space O(1).
    static void mergeFromBack(int[] first, int m, int[] second, int n) {
        // last real number in first
        int i = m - 1;
        // last number in second
        int j = n - 1;
        // last slot of first (where the biggest number goes)
        int write = m + n - 1;
        // while second still has numbers to place
        while (j >= 0) {
            // first still has numbers AND its number is bigger: place it
            if (i >= 0 && first[i] > second[j]) {
                first[write] = first[i];
                i--;
            } else {
                // otherwise place the number from second
                first[write] = second[j];
                j--;
            }
            // next slot to the left
            write--;
        }
        // leftover numbers in first are already in the right place
    }

    // Template B: is small a subsequence of big? (LeetCode 392)
    // Subsequence = same characters in the same order, gaps allowed.
    // Time O(length of big), space O(1).
    static boolean isSubsequence(String small, String big) {
        // pointer in small
        int i = 0;
        // pointer in big
        int j = 0;
        // walk both until one runs out
        while (i < small.length() && j < big.length()) {
            // characters match: this char of small is found
            if (small.charAt(i) == big.charAt(j)) {
                i++;
            }
            // always move forward in big
            j++;
        }
        // true if every character of small was found
        return i == small.length();
    }

    // Template C: common values of two SORTED arrays, keeping repeats (LeetCode 350 after sorting).
    // Time O(m + n), space O(1) besides the answer.
    static List<Integer> commonValues(int[] first, int[] second) {
        // answer list
        List<Integer> result = new ArrayList<>();
        // pointer in first
        int i = 0;
        // pointer in second
        int j = 0;
        // stop when either array runs out
        while (i < first.length && j < second.length) {
            if (first[i] == second[j]) {
                // same value in both: keep it, move both
                result.add(first[i]);
                i++;
                j++;
            } else if (first[i] < second[j]) {
                // first is behind: move it forward
                i++;
            } else {
                // second is behind: move it forward
                j++;
            }
        }
        // all common values
        return result;
    }

    // Template D: intersections of two sorted lists of intervals (LeetCode 986).
    // Time O(m + n), space O(1) besides the answer.
    static List<int[]> intervalIntersections(int[][] first, int[][] second) {
        // answer list
        List<int[]> result = new ArrayList<>();
        // pointer in first
        int i = 0;
        // pointer in second
        int j = 0;
        // stop when either list runs out
        while (i < first.length && j < second.length) {
            // overlap starts at the later start
            int start = Math.max(first[i][0], second[j][0]);
            // overlap ends at the earlier end
            int end = Math.min(first[i][1], second[j][1]);
            // a real overlap exists
            if (start <= end) {
                result.add(new int[] {start, end});
            }
            // the interval that ends first cannot overlap anything else: move past it
            if (first[i][1] < second[j][1]) {
                i++;
            } else {
                j++;
            }
        }
        // all overlaps
        return result;
    }

    public static void main(String[] args) {
        // Template A
        int[] first = {1, 2, 3, 0, 0, 0};
        mergeFromBack(first, 3, new int[] {2, 5, 6}, 3);
        System.out.println(Arrays.toString(first));
        // prints: [1, 2, 2, 3, 5, 6]

        // Template B
        System.out.println(isSubsequence("abc", "ahbgdc"));
        // prints: true
        System.out.println(isSubsequence("axc", "ahbgdc"));
        // prints: false

        // Template C
        System.out.println(commonValues(new int[] {1, 2, 2, 3}, new int[] {2, 2, 3, 4}));
        // prints: [2, 2, 3]

        // Template D
        int[][] a = {{0, 2}, {5, 10}, {13, 23}, {24, 25}};
        int[][] b = {{1, 5}, {8, 12}, {15, 24}, {25, 26}};
        List<int[]> overlaps = intervalIntersections(a, b);
        for (int[] overlap : overlaps) {
            System.out.print(Arrays.toString(overlap) + " ");
        }
        System.out.println();
        // prints: [1, 2] [5, 5] [8, 10] [15, 23] [24, 24] [25, 25]
    }
}
