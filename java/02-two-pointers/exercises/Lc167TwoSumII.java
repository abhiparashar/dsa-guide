import java.util.Arrays;

public class Lc167TwoSumII {
  static class Solution {
    public int[] twoSum(int[] nums, int target) {
      int[] ans = new int[] { -1, -1 };
      int left = 0;
      int right = nums.length - 1;
      while (left < right) {
        int sum = nums[left] + nums[right];
        if (sum == target) {
          return new int[] { left + 1, right + 1 };
        }
        if (sum < target) {
          left++;
        } else {
          right--;
        }
      }
      return ans;
    }
  }

  public static void main(String[] args) {
    // create the object, like LeetCode does
    Solution solution = new Solution();
    // example 1: expected [1, 2]
    System.out.println(Arrays.toString(solution.twoSum(new int[] { 2, 7, 11, 15 }, 9)));
    // example 2: expected [1, 3]
    System.out.println(Arrays.toString(solution.twoSum(new int[] { 2, 3, 4 }, 6)));
    // example 3: expected [1, 2]
    System.out.println(Arrays.toString(solution.twoSum(new int[] { -1, 0 }, -1)));
  }
}
