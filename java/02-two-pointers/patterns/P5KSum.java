// Pattern 5: Sort + Fix One + Two Pointers (k-Sum)
// Sort first. Fix one number with a loop. Find the rest with Pattern 1 on the part to its right.
// 3Sum = 1 loop + two pointers = O(n^2). 4Sum = 2 loops + two pointers = O(n^3).
// Run: java P5KSum.java

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P5KSum {

    // Template A: all UNIQUE triplets that sum to 0 (LeetCode 15).
    // Time O(n^2), space O(1) besides the answer (sorting aside).
    static List<List<Integer>> threeSum(int[] nums) {
        // sorting makes two pointers possible and puts duplicates next to each other
        Arrays.sort(nums);
        // answer list
        List<List<Integer>> result = new ArrayList<>();
        // fix the first number of the triplet
        for (int first = 0; first < nums.length - 2; first++) {
            // same first value as last time would give the same triplets: skip it
            if (first > 0 && nums[first] == nums[first - 1]) {
                continue;
            }
            // two pointers on the part to the right of first
            int left = first + 1;
            int right = nums.length - 1;
            while (left < right) {
                // sum of the three numbers
                int sum = nums[first] + nums[left] + nums[right];
                if (sum == 0) {
                    // found one: save it
                    result.add(Arrays.asList(nums[first], nums[left], nums[right]));
                    // move both pointers inward
                    left++;
                    right--;
                    // skip copies of the left value we just used
                    while (left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }
                    // skip copies of the right value we just used
                    while (left < right && nums[right] == nums[right + 1]) {
                        right--;
                    }
                } else if (sum < 0) {
                    // too small: need a bigger number
                    left++;
                } else {
                    // too big: need a smaller number
                    right--;
                }
            }
        }
        // all unique triplets
        return result;
    }

    // Template B: COUNT triplets that can form a triangle (LeetCode 611).
    // Fix the BIGGEST side from the right. Then a + b > biggest is a pair-count problem.
    // Time O(n^2), space O(1).
    static int triangleCount(int[] sides) {
        // sort so the biggest side is on the right
        Arrays.sort(sides);
        // running total
        int count = 0;
        // fix the biggest side, moving from the right end down
        for (int biggest = sides.length - 1; biggest >= 2; biggest--) {
            // two pointers on the part to the left of biggest
            int left = 0;
            int right = biggest - 1;
            while (left < right) {
                if (sides[left] + sides[right] > sides[biggest]) {
                    // works for left, and for every index between left and right too
                    count = count + (right - left);
                    // right is fully counted: move it
                    right--;
                } else {
                    // too small: need a bigger left side
                    left++;
                }
            }
        }
        // total triangles
        return count;
    }

    public static void main(String[] args) {
        // Template A
        System.out.println(threeSum(new int[] {-1, 0, 1, 2, -1, -4}));
        // prints: [[-1, -1, 2], [-1, 0, 1]]
        System.out.println(threeSum(new int[] {0, 0, 0, 0}));
        // prints: [[0, 0, 0]]

        // Template B
        System.out.println(triangleCount(new int[] {2, 2, 3, 4}));
        // prints: 3
    }
}
