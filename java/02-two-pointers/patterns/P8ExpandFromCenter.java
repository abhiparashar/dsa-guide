// Pattern 8: Expand From the Center (pointers move AWAY from each other)
// Every palindrome has a center: one character ("aba") or a gap between two ("abba").
// Start both pointers at the center and grow outward while the ends match.
// Run: java P8ExpandFromCenter.java

public class P8ExpandFromCenter {

    // core helper: grow outward from left/right while it stays a palindrome.
    // Returns the length of the palindrome found.
    static int expand(String text, int left, int right) {
        // keep growing while inside the string and the two ends match
        while (left >= 0 && right < text.length() && text.charAt(left) == text.charAt(right)) {
            left--;
            right++;
        }
        // the loop went one step too far on both sides, so the length is right - left - 1
        return right - left - 1;
    }

    // Template A: longest palindromic substring (LeetCode 5). Time O(n^2), space O(1).
    static String longestPalindrome(String text) {
        // start index of the best palindrome so far
        int bestStart = 0;
        // length of the best palindrome so far
        int bestLength = 0;
        // try every center
        for (int center = 0; center < text.length(); center++) {
            // odd length: center is one character, like "aba"
            int oddLength = expand(text, center, center);
            // even length: center is the gap after this character, like "abba"
            int evenLength = expand(text, center, center + 1);
            // the longer of the two
            int length = Math.max(oddLength, evenLength);
            // new best: work out where it starts
            if (length > bestLength) {
                bestLength = length;
                bestStart = center - (length - 1) / 2;
            }
        }
        // cut the best palindrome out of the text
        return text.substring(bestStart, bestStart + bestLength);
    }

    // Template B: count all palindromic substrings (LeetCode 647). Time O(n^2), space O(1).
    static int countPalindromes(String text) {
        // running total
        int count = 0;
        // try every center, odd and even
        for (int center = 0; center < text.length(); center++) {
            // count palindromes around an odd center
            count = count + countAround(text, center, center);
            // count palindromes around an even center
            count = count + countAround(text, center, center + 1);
        }
        // total palindromes
        return count;
    }

    // each successful step outward is one more palindrome
    static int countAround(String text, int left, int right) {
        // palindromes found around this center
        int count = 0;
        // grow outward while inside the string and the ends match
        while (left >= 0 && right < text.length() && text.charAt(left) == text.charAt(right)) {
            // text[left..right] is one more palindrome
            count++;
            // grow one step on each side
            left--;
            right++;
        }
        // palindromes around this center
        return count;
    }

    public static void main(String[] args) {
        // Template A
        System.out.println(longestPalindrome("babad"));
        // prints: bab
        System.out.println(longestPalindrome("cbbd"));
        // prints: bb

        // Template B: "aaa" has a, a, a, aa, aa, aaa
        System.out.println(countPalindromes("aaa"));
        // prints: 6
    }
}
