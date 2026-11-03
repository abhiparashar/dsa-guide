# My DSA Progress (Java)

Goal: crack FAANG interviews. Plan: **Round 1** = 3 questions from every Two Pointers pattern, **Round 2** = the rest. For each pattern: recognise → theory → template → questions.

## How questions are given to me

**Default: just the question link + file name. No explanation.**

Only when I ask "explain", use this format:
1. **What it asks**: input, task, in one or two plain lines.
2. **Example table**: Input → each step → Output (✅ / ❌).
3. **Why it's tricky**: the one thing that breaks the easy idea.
4. **Two ways table**: easy way vs pattern way, with Big-O.
5. **The key fact** that makes the pattern work.
6. **Your turn**: a small paper exercise, then the file to create.

## ▶ Start here (next session)

**I am at:** Two Pointers → Round 1 → **Pattern 2, question 1 of 3**. (Pattern 1 Round 1 ✅ done)

**Round 1:** `██░░░░░░░░░░░░░░░░░░` 3 / 27 (11%)

1. Warm-up (2 min): explain out loud why `write` starts at `nums.length - 1` in [T2SortedSquares.java](java/02-two-pointers/exercises/T2SortedSquares.java).
2. P2 template ✅ typed. Now solve [977 Squares of a Sorted Array](https://leetcode.com/problems/squares-of-a-sorted-array/) on your own → `exercises/Lc977SquaresOfASortedArray.java` (don't look at T2).
3. Then P2 question 2: [11 Container With Most Water](https://leetcode.com/problems/container-with-most-water/).

## Overall

| Topic | Progress | Status |
|---|---|---|
| Two Pointers | `█░░░░░░░░░░░░░░░░░░░` 3 / 59 (5%) | 🟡 in progress |
| Sliding Window | `░░░░░░░░░░░░░░░░░░░░` 0 | ⚪ not started |
| Binary Search | `░░░░░░░░░░░░░░░░░░░░` 0 | ⚪ not started |

## Two Pointers, by pattern

Guide: [java/02-two-pointers/README.md](java/02-two-pointers/README.md)

| # | Pattern | Template typed | Solved | Progress |
|---|---|---|---|---|
| 1 | Opposite Ends | ✅ T1 pair with sum | 3 / 9 | `███████░░░░░░░░░░░░░` 33% |
| 2 | Shrink the Weaker Side | ✅ T2 sorted squares | 0 / 3 | `░░░░░░░░░░░░░░░░░░░░` 0% |
| 3 | Slow Writer, Fast Reader | ⬜ | 0 / 5 | `░░░░░░░░░░░░░░░░░░░░` 0% |
| 4 | One Pointer per Input | ⬜ | 0 / 12 | `░░░░░░░░░░░░░░░░░░░░` 0% |
| 5 | Sort + Fix One (k-Sum) | ⬜ | 0 / 6 | `░░░░░░░░░░░░░░░░░░░░` 0% |
| 6 | Partition | ⬜ | 0 / 4 | `░░░░░░░░░░░░░░░░░░░░` 0% |
| 7 | Fast & Slow | ⬜ | 0 / 12 | `░░░░░░░░░░░░░░░░░░░░` 0% |
| 8 | Expand From the Center | ⬜ | 0 / 3 | `░░░░░░░░░░░░░░░░░░░░` 0% |
| 9 | Reverse a Range | ⬜ | 0 / 4 | `░░░░░░░░░░░░░░░░░░░░` 0% |
| — | Boss level (1163) | — | 0 / 1 | `░░░░░░░░░░░░░░░░░░░░` 0% |

## Solved questions

| Date | # | Problem | Level | Pattern | My file | Mistake I made |
|---|---|---|---|---|---|---|
| 2026-11-03 | — | Template: pair with sum | — | P1 | [T1PairWithSum.java](java/02-two-pointers/exercises/T1PairWithSum.java) | Forgot `static`; empty method → "missing return statement" |
| 2026-11-03 | 167 | [Two Sum II](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/) | Medium | P1 | [Lc167TwoSumII.java](java/02-two-pointers/exercises/Lc167TwoSumII.java) | Returned 0-indexed; problem wants 1-indexed → `left + 1, right + 1` |
| 2026-11-03 | 344 | [Reverse String](https://leetcode.com/problems/reverse-string/) | Easy | P1 | [Lc344ReverseString.java](java/02-two-pointers/exercises/Lc344ReverseString.java) | None. Naming: `temp` reads better than `swap` |
| 2026-11-03 | 125 | [Valid Palindrome](https://leetcode.com/problems/valid-palindrome/) | Easy | P1 | [Lc125ValidPalindrome.java](java/02-two-pointers/exercises/Lc125ValidPalindrome.java) | `strip()` only trims the ends, it doesn't remove `,` `:`; rejected odd lengths, but `aba` is a palindrome |
| 2026-11-03 | — | Template: sorted squares | — | P2 | [T2SortedSquares.java](java/02-two-pointers/exercises/T2SortedSquares.java) | `result = new int[]{...}` replaced the whole array (use `result[write] = x`); no `write` slot; moved both pointers instead of only the one placed |

## Java things I learned

| Concept | One-line meaning |
|---|---|
| `static` | Method belongs to the class; `main` (static) can only call static methods directly |
| LeetCode style | `class Solution` with a non-static method; LeetCode does `new Solution()` for you |
| One `public` class per file | It must match the file name; put `Solution` inside as `static class` to avoid clashes |
| Swap | Needs a third variable (`temp`), or one value is lost |
| Clean a String | `str.toLowerCase().replaceAll("[^a-z0-9]", "")` keeps only letters and digits (`^` inside `[ ]` = NOT) |
| `strip()` | Removes spaces at the start and end only, not in the middle |
| String vs array length | String: `s.length()` (with `()`); array: `arr.length` (no `()`) |
| `arr = new int[]{...}` vs `arr[i] = x` | The first **replaces** the whole array; the second **fills one slot** |

## Plan

**Round 1: 3 questions from every pattern** (breadth first, so every pattern feels familiar). For each pattern: read *How to recognise it* + Theory, type the template, then solve its 3 questions.

| Pattern | Q1 | Q2 | Q3 | Done |
|---|---|---|---|---|
| P1 Opposite Ends | ✅ [344](https://leetcode.com/problems/reverse-string/) Easy | ✅ [167](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/) Medium | ✅ [125](https://leetcode.com/problems/valid-palindrome/) Easy | 3 / 3 ✅ |
| P2 Shrink the Weaker Side | [977](https://leetcode.com/problems/squares-of-a-sorted-array/) Easy | [11](https://leetcode.com/problems/container-with-most-water/) Medium | [42](https://leetcode.com/problems/trapping-rain-water/) Hard | 0 / 3 |
| P3 Slow Writer, Fast Reader | [26](https://leetcode.com/problems/remove-duplicates-from-sorted-array/) Easy | [283](https://leetcode.com/problems/move-zeroes/) Easy | [80](https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/) Medium | 0 / 3 |
| P4 One Pointer per Input | [88](https://leetcode.com/problems/merge-sorted-array/) Easy | [392](https://leetcode.com/problems/is-subsequence/) Easy | [986](https://leetcode.com/problems/interval-list-intersections/) Medium | 0 / 3 |
| P5 Sort + Fix One (k-Sum) | [15](https://leetcode.com/problems/3sum/) Medium | [16](https://leetcode.com/problems/3sum-closest/) Medium | [611](https://leetcode.com/problems/valid-triangle-number/) Medium | 0 / 3 |
| P6 Partition | [905](https://leetcode.com/problems/sort-array-by-parity/) Easy | [75](https://leetcode.com/problems/sort-colors/) Medium | [2161](https://leetcode.com/problems/partition-array-according-to-given-pivot/) Medium | 0 / 3 |
| P7 Fast & Slow | [141](https://leetcode.com/problems/linked-list-cycle/) Easy | [876](https://leetcode.com/problems/middle-of-the-linked-list/) Easy | [142](https://leetcode.com/problems/linked-list-cycle-ii/) Medium | 0 / 3 |
| P8 Expand From the Center | [5](https://leetcode.com/problems/longest-palindromic-substring/) Medium | [647](https://leetcode.com/problems/palindromic-substrings/) Medium | [1793](https://leetcode.com/problems/maximum-score-of-a-good-subarray/) Hard | 0 / 3 |
| P9 Reverse a Range | [189](https://leetcode.com/problems/rotate-array/) Medium | [151](https://leetcode.com/problems/reverse-words-in-a-string/) Medium | [31](https://leetcode.com/problems/next-permutation/) Medium | 0 / 3 |

**Round 1 total: 3 / 27.**

**Round 2:** come back to the remaining questions of each pattern in the guide, then the Boss level.

