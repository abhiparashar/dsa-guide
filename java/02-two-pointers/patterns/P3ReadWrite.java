// Pattern 3: Slow Writer, Fast Reader (same direction, in place)
// "read" visits every item once. "write" marks where the next item we KEEP goes.
// Everything before "write" is the finished answer.
// Run: java P3ReadWrite.java

import java.util.Arrays;

public class P3ReadWrite {

    // Template A: remove every copy of value, in place (LeetCode 27).
    // Returns how many items are kept. Time O(n), space O(1).
    static int removeElement(int[] nums, int value) {
        // next free slot for a kept item
        int write = 0;
        // look at every item once
        for (int read = 0; read < nums.length; read++) {
            // keep anything that is not the value to remove
            if (nums[read] != value) {
                // copy it into the next free slot
                nums[write] = nums[read];
                // the free slot moves forward
                write++;
            }
        }
        // number of kept items
        return write;
    }

    // Template B: in a SORTED array, keep each value at most k times (LeetCode 26 is k=1, LeetCode 80 is k=2).
    // Returns how many items are kept. Time O(n), space O(1).
    static int keepAtMostK(int[] sortedNums, int k) {
        // next free slot for a kept item
        int write = 0;
        // look at every item once
        for (int read = 0; read < sortedNums.length; read++) {
            // the first k items are always kept
            boolean isEarly = write < k;
            // if this value differs from the one k slots back, it has fewer than k copies so far
            boolean isNotTooMany = isEarly || sortedNums[read] != sortedNums[write - k];
            if (isNotTooMany) {
                // keep it
                sortedNums[write] = sortedNums[read];
                write++;
            }
        }
        // number of kept items
        return write;
    }

    // Template C: move all zeroes to the end, keeping the order of the others (LeetCode 283).
    // SWAP instead of copy, so the zeroes end up at the back by themselves.
    // Time O(n), space O(1).
    static void moveZeroes(int[] nums) {
        // next slot for a non-zero number
        int write = 0;
        // look at every item once
        for (int read = 0; read < nums.length; read++) {
            // a non-zero number goes to the front part
            if (nums[read] != 0) {
                // swap it with whatever is at the write slot (a zero, or itself)
                int temp = nums[write];
                nums[write] = nums[read];
                nums[read] = temp;
                // the front part grew by one
                write++;
            }
        }
    }

    public static void main(String[] args) {
        // Template A
        int[] a = {3, 2, 2, 3};
        int keptA = removeElement(a, 3);
        System.out.println(keptA + " " + Arrays.toString(Arrays.copyOf(a, keptA)));
        // prints: 2 [2, 2]

        // Template B with k = 1
        int[] b1 = {1, 1, 2, 3, 3};
        int keptB1 = keepAtMostK(b1, 1);
        System.out.println(keptB1 + " " + Arrays.toString(Arrays.copyOf(b1, keptB1)));
        // prints: 3 [1, 2, 3]

        // Template B with k = 2
        int[] b2 = {1, 1, 1, 2, 2, 3};
        int keptB2 = keepAtMostK(b2, 2);
        System.out.println(keptB2 + " " + Arrays.toString(Arrays.copyOf(b2, keptB2)));
        // prints: 5 [1, 1, 2, 2, 3]

        // Template C
        int[] c = {0, 1, 0, 3, 12};
        moveZeroes(c);
        System.out.println(Arrays.toString(c));
        // prints: [1, 3, 12, 0, 0]
    }
}
