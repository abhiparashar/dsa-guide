import java.util.*;

class Solution {
  public int[] pairWithSum(int[] nums, int target) {
    int[] ans = new int[] { -1, -1 };
    int left = 0;
    int right = nums.length - 1;
    while (left < right) {
      int sum = nums[left] + nums[right];
      if (sum == target) {
        return new int[] { left, right };
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

public class T1PairWithSum {
  public static void main(String[] args) {
    // create the object, like LeetCode does
    Solution solution = new Solution();
    // test 1: 5 + 7 = 12, so the answer is [1, 2]
    System.out.println(Arrays.toString(solution.pairWithSum(new int[] { 2, 5, 7, 11 }, 12)));
    // test 2: 3 + 6 = 9, so the answer is [1, 3]
    System.out.println(Arrays.toString(solution.pairWithSum(new int[] { 1, 3, 4, 6, 9 }, 9)));
  }
}
