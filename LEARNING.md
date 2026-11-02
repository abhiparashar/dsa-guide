# My DSA Progress (Java)

Goal: crack FAANG interviews. Plan: one pattern at a time, theory → template → questions Easy → Medium → Hard.

## Overall

| Topic | Progress | Status |
|---|---|---|
| Two Pointers | `█░░░░░░░░░░░░░░░░░░░` 2 / 59 (3%) | 🟡 in progress |
| Sliding Window | `░░░░░░░░░░░░░░░░░░░░` 0 | ⚪ not started |
| Binary Search | `░░░░░░░░░░░░░░░░░░░░` 0 | ⚪ not started |

## Two Pointers, by pattern

Guide: [java/02-two-pointers/README.md](java/02-two-pointers/README.md)

| # | Pattern | Template typed | Solved | Progress |
|---|---|---|---|---|
| 1 | Opposite Ends | ✅ T1 pair with sum | 2 / 9 | `████░░░░░░░░░░░░░░░░` 22% |
| 2 | Shrink the Weaker Side | ⬜ | 0 / 3 | `░░░░░░░░░░░░░░░░░░░░` 0% |
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

## Java things I learned

| Concept | One-line meaning |
|---|---|
| `static` | Method belongs to the class; `main` (static) can only call static methods directly |
| LeetCode style | `class Solution` with a non-static method; LeetCode does `new Solution()` for you |
| One `public` class per file | It must match the file name; put `Solution` inside as `static class` to avoid clashes |
| Swap | Needs a third variable (`temp`), or one value is lost |

## Next session

1. Re-read **Pattern 1 → How to recognise it** (2 min warm-up).
2. Solve [125 Valid Palindrome](https://leetcode.com/problems/valid-palindrome/) → `exercises/Lc125ValidPalindrome.java`.
3. Then: 345, 917, 2824, 680, 881, 1498 (rest of Pattern 1).
