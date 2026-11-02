// Pattern 2: Shrink the Weaker Side (opposite ends + greedy choice)
// Both pointers start at the ends. Each step, move the pointer that
// CANNOT do better if it stays. That throws away bad options safely.
// Run: java P2ShrinkWeakerSide.java

import java.util.Arrays;

public class P2ShrinkWeakerSide {

    // Template A: Container With Most Water (LeetCode 11).
    // Area between two walls = shorter wall * distance between them.
    // Time O(n), space O(1).
    static int maxArea(int[] heights) {
        // left wall
        int left = 0;
        // right wall
        int right = heights.length - 1;
        // best area seen so far
        int best = 0;
        // try walls until they meet
        while (left < right) {
            // distance between the two walls
            int width = right - left;
            // water level is limited by the shorter wall
            int shorter = Math.min(heights[left], heights[right]);
            // area for this pair of walls
            int area = shorter * width;
            // keep the biggest
            best = Math.max(best, area);
            // the shorter wall can never give a bigger area with a smaller width, so move it
            if (heights[left] < heights[right]) {
                left++;
            } else {
                right--;
            }
        }
        // biggest area found
        return best;
    }

    // Template B: Trapping Rain Water (LeetCode 42).
    // Water above a bar = min(tallest bar on its left, tallest bar on its right) - its height.
    // Time O(n), space O(1).
    static int trap(int[] heights) {
        // pointer from the left
        int left = 0;
        // pointer from the right
        int right = heights.length - 1;
        // tallest bar seen so far from the left side
        int leftMax = 0;
        // tallest bar seen so far from the right side
        int rightMax = 0;
        // total trapped water
        int water = 0;
        // process bars until the pointers meet
        while (left < right) {
            // the side with the smaller bar is limited by its own max: we can settle it now
            if (heights[left] < heights[right]) {
                // update the tallest bar on the left
                leftMax = Math.max(leftMax, heights[left]);
                // water above this bar (0 if it is the new tallest)
                water = water + (leftMax - heights[left]);
                // this bar is done
                left++;
            } else {
                // update the tallest bar on the right
                rightMax = Math.max(rightMax, heights[right]);
                // water above this bar
                water = water + (rightMax - heights[right]);
                // this bar is done
                right--;
            }
        }
        // total water
        return water;
    }

    // Template C: Squares of a Sorted Array (LeetCode 977). Fill the answer from the BACK.
    // With negatives, the biggest square is always at one of the two ends.
    // Time O(n), space O(n) for the answer.
    static int[] sortedSquares(int[] sortedNums) {
        // number of items
        int n = sortedNums.length;
        // answer array
        int[] result = new int[n];
        // pointer at the left end
        int left = 0;
        // pointer at the right end
        int right = n - 1;
        // fill the answer from the last slot to the first
        for (int write = n - 1; write >= 0; write--) {
            // square of the left number
            int leftSquare = sortedNums[left] * sortedNums[left];
            // square of the right number
            int rightSquare = sortedNums[right] * sortedNums[right];
            // the bigger square goes in the current last slot
            if (leftSquare > rightSquare) {
                result[write] = leftSquare;
                left++;
            } else {
                result[write] = rightSquare;
                right--;
            }
        }
        // squares in sorted order
        return result;
    }

    public static void main(String[] args) {
        // Template A
        System.out.println(maxArea(new int[] {1, 8, 6, 2, 5, 4, 8, 3, 7}));
        // prints: 49

        // Template B
        System.out.println(trap(new int[] {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
        // prints: 6

        // Template C
        System.out.println(Arrays.toString(sortedSquares(new int[] {-4, -1, 0, 3, 10})));
        // prints: [0, 1, 9, 16, 100]
    }
}
