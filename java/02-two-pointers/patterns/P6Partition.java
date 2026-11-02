// Pattern 6: Partition (two or three pointers split an array into groups, in place)
// Dutch National Flag: low = end of the 0s, mid = current item, high = start of the 2s.
// Run: java P6Partition.java

import java.util.Arrays;

public class P6Partition {

    // small helper: swap two items of an array
    static void swap(int[] nums, int a, int b) {
        // remember the first item
        int temp = nums[a];
        // overwrite the first with the second
        nums[a] = nums[b];
        // put the remembered first into the second's place
        nums[b] = temp;
    }

    // Template A: sort an array of only 0s, 1s and 2s in ONE pass (LeetCode 75).
    // Time O(n), space O(1).
    static void sortColors(int[] nums) {
        // everything before low is 0
        int low = 0;
        // the item we are looking at
        int mid = 0;
        // everything after high is 2
        int high = nums.length - 1;
        // items between mid and high are not looked at yet
        while (mid <= high) {
            if (nums[mid] == 0) {
                // a 0 belongs at the front: swap it into the 0s group
                swap(nums, low, mid);
                // both groups grew; the item swapped in is a 1 (already checked), so mid moves too
                low++;
                mid++;
            } else if (nums[mid] == 1) {
                // a 1 is already in the middle group
                mid++;
            } else {
                // a 2 belongs at the back: swap it into the 2s group
                swap(nums, mid, high);
                // the 2s group grew; do NOT move mid, the item swapped in is not checked yet
                high--;
            }
        }
    }

    // Template B: evens first, then odds (LeetCode 905). Opposite ends partition.
    // Time O(n), space O(1).
    static void evensFirst(int[] nums) {
        // looks for an odd number on the left side
        int left = 0;
        // looks for an even number on the right side
        int right = nums.length - 1;
        while (left < right) {
            if (nums[left] % 2 == 0) {
                // left is already even: fine, move on
                left++;
            } else if (nums[right] % 2 == 1) {
                // right is already odd: fine, move on
                right--;
            } else {
                // left is odd and right is even: swap them
                swap(nums, left, right);
                left++;
                right--;
            }
        }
    }

    public static void main(String[] args) {
        // Template A
        int[] colors = {2, 0, 2, 1, 1, 0};
        sortColors(colors);
        System.out.println(Arrays.toString(colors));
        // prints: [0, 0, 1, 1, 2, 2]

        // Template B
        int[] numbers = {3, 1, 2, 4};
        evensFirst(numbers);
        System.out.println(Arrays.toString(numbers));
        // prints: [4, 2, 1, 3]
    }
}
