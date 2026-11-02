import java.util.Arrays;

public class Lc344ReverseString {
  public static void reverseString(char[] word) {
    int left = 0;
    int right = word.length - 1;
    while (left < right) {
      char swap = word[left];
      word[left] = word[right];
      word[right] = swap;
      left++;
      right--;
    }
  }

  public static void main(String[] args) {
    // example 1: expected [o, l, l, e, h]
    char[] word1 = { 'h', 'e', 'l', 'l', 'o' };
    reverseString(word1);
    System.out.println(Arrays.toString(word1));
    // example 2: expected [h, a, n, n, a, H]
    char[] word2 = { 'H', 'a', 'n', 'n', 'a', 'h' };
    reverseString(word2);
    System.out.println(Arrays.toString(word2));
  }
}
