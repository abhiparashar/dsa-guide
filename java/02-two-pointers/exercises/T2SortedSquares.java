import java.util.Arrays;

public class T2SortedSquares {
  public static int[] sortedSquares(int[] nums) {
    int[] result = new int[nums.length];
    int left = 0;
    int right = nums.length - 1;
    int write = nums.length - 1;
    while (left <= right) {
      int leftNum = nums[left] * nums[left];
      int rightNum = nums[right] * nums[right];
      if (leftNum > rightNum) {
        result[write] = leftNum;
        left++;
      } else {
        result[write] = rightNum;
        right--;
      }
      write--;
    }
    return result;
  }

  public static void main(String[] args) {
    // expected: [0, 1, 9, 16, 100]
    System.out.println(Arrays.toString(sortedSquares(new int[] { -4, -1, 0, 3, 10 })));
  }
}
