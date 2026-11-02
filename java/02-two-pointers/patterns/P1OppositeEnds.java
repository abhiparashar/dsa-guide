// Pattern 1: Opposite Ends
// One pointer starts at the left end, one at the right end.
// They walk toward each other until they meet.
// Run: java P1OppositeEnds.java

import java.util.Arrays;

public class P1OppositeEnds {

    // Template A: find two numbers in a SORTED array that add up to target.
    // Returns their indexes, or [-1, -1] if no pair exists.
    // Time O(n), space O(1).
    static int[] pairWithSum(int[] sortedNums, int target) {
        // left starts at the smallest number
        int left = 0;
        // right starts at the biggest number
        int right = sortedNums.length - 1;
        // stop when the two pointers meet (a number can't pair with itself)
        while (left < right) {
            // add the two numbers the pointers are on
            int sum = sortedNums[left] + sortedNums[right];
            // found the pair
            if (sum == target) {
                return new int[] {left, right};
            }
            // sum too small: the only way to grow it is a bigger left number
            if (sum < target) {
                left++;
            } else {
                // sum too big: the only way to shrink it is a smaller right number
                right--;
            }
        }
        // pointers met without finding a pair
        return new int[] {-1, -1};
    }

    // Template B: is the string a palindrome, ignoring non-letters/digits and case?
    // Time O(n), space O(1).
    static boolean isPalindrome(String text) {
        // pointer at the first character
        int left = 0;
        // pointer at the last character
        int right = text.length() - 1;
        // compare until the pointers meet
        while (left < right) {
            // skip anything on the left that is not a letter or digit
            if (!Character.isLetterOrDigit(text.charAt(left))) {
                left++;
                continue;
            }
            // skip anything on the right that is not a letter or digit
            if (!Character.isLetterOrDigit(text.charAt(right))) {
                right--;
                continue;
            }
            // lower-case both so 'A' and 'a' count as the same
            char leftChar = Character.toLowerCase(text.charAt(left));
            char rightChar = Character.toLowerCase(text.charAt(right));
            // one mismatch is enough to say "not a palindrome"
            if (leftChar != rightChar) {
                return false;
            }
            // both matched: move both pointers inward
            left++;
            right--;
        }
        // every pair matched
        return true;
    }

    // Template C: COUNT pairs in a SORTED array whose sum is less than target.
    // Key trick: if nums[left] + nums[right] < target, then left pairs with
    // EVERY index from left+1 up to right. That is (right - left) pairs at once.
    // Time O(n), space O(1).
    static int countPairsLessThan(int[] sortedNums, int target) {
        // pointer at the smallest number
        int left = 0;
        // pointer at the biggest number
        int right = sortedNums.length - 1;
        // running total of valid pairs
        int count = 0;
        // stop when the pointers meet
        while (left < right) {
            // sum of the current pair
            int sum = sortedNums[left] + sortedNums[right];
            if (sum < target) {
                // left + any number between left and right is also < target
                count = count + (right - left);
                // left is fully counted: move on
                left++;
            } else {
                // sum too big: try a smaller right number
                right--;
            }
        }
        // total pairs found
        return count;
    }

    public static void main(String[] args) {
        // Template A
        System.out.println(Arrays.toString(pairWithSum(new int[] {1, 3, 4, 6, 9}, 10)));
        // prints: [0, 4]
        System.out.println(Arrays.toString(pairWithSum(new int[] {1, 2, 3}, 100)));
        // prints: [-1, -1]

        // Template B
        System.out.println(isPalindrome("A man, a plan, a canal: Panama"));
        // prints: true
        System.out.println(isPalindrome("race a car"));
        // prints: false

        // Template C: pairs with sum below 5 are (1,2)=3 and (1,3)=4
        System.out.println(countPairsLessThan(new int[] {1, 2, 3, 4}, 5));
        // prints: 2
    }
}
