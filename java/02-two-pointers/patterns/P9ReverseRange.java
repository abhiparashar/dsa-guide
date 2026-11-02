// Pattern 9: Reverse a Range (opposite ends, used as a building block)
// reverse(left, right) swaps the ends and walks inward.
// Many "rotate" and "reorder" problems are just 2 or 3 reverses.
// Run: java P9ReverseRange.java

import java.util.Arrays;

public class P9ReverseRange {

    // core helper: reverse nums[left..right] in place. Time O(length), space O(1).
    static void reverse(int[] nums, int left, int right) {
        while (left < right) {
            // swap the two ends
            int temp = nums[left];
            nums[left] = nums[right];
            nums[right] = temp;
            // walk inward
            left++;
            right--;
        }
    }

    // Template A: rotate right by k steps (LeetCode 189). Time O(n), space O(1).
    // [1,2,3,4,5,6,7], k=3 -> reverse all -> [7,6,5,4,3,2,1]
    //                      -> reverse first k -> [5,6,7,4,3,2,1]
    //                      -> reverse the rest -> [5,6,7,1,2,3,4]
    static void rotateRight(int[] nums, int k) {
        // rotating by n is the same as not rotating
        int n = nums.length;
        k = k % n;
        // step 1: reverse everything
        reverse(nums, 0, n - 1);
        // step 2: reverse the first k items
        reverse(nums, 0, k - 1);
        // step 3: reverse the remaining items
        reverse(nums, k, n - 1);
    }

    // same helper, for a char array
    static void reverse(char[] chars, int left, int right) {
        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            left++;
            right--;
        }
    }

    // Template B: reverse the order of words (LeetCode 151 idea, single spaces).
    // Reverse the whole thing, then reverse each word back. Time O(n).
    static String reverseWords(String sentence) {
        char[] chars = sentence.toCharArray();
        // step 1: reverse everything: "the sky is blue" -> "eulb si yks eht"
        reverse(chars, 0, chars.length - 1);
        // step 2: reverse each word back
        int wordStart = 0;
        for (int i = 0; i <= chars.length; i++) {
            // a word ends at a space or at the end of the text
            boolean atEnd = i == chars.length;
            if (atEnd || chars[i] == ' ') {
                reverse(chars, wordStart, i - 1);
                // the next word starts after the space
                wordStart = i + 1;
            }
        }
        return new String(chars);
    }

    public static void main(String[] args) {
        // Template A
        int[] nums = {1, 2, 3, 4, 5, 6, 7};
        rotateRight(nums, 3);
        System.out.println(Arrays.toString(nums));
        // prints: [5, 6, 7, 1, 2, 3, 4]

        // Template B
        System.out.println(reverseWords("the sky is blue"));
        // prints: blue is sky the
    }
}
