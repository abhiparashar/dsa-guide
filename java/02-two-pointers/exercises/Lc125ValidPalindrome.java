public class Lc125ValidPalindrome {
  public static boolean isPalindrome(String str) {
    str = str.toLowerCase().replaceAll("[^a-z0-9]", "");
    int left = 0;
    int right = str.length() - 1;
    if (str.length() == 0) {
      return true;
    }
    while (left < right) {
      if (str.charAt(left) != str.charAt(right)) {
        return false;
      }
      left++;
      right--;
    }
    return true;
  }

  public static void main(String[] args) {
    // expected: true
    System.out.println(isPalindrome("A man, a plan, a canal: Panama"));
    // expected: false
    System.out.println(isPalindrome("race a car"));
    // expected: true
    System.out.println(isPalindrome(" "));
  }
}
