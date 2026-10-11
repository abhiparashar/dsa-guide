# Phase 1: Arrays & Strings

[← Phase 0](00-basics.md) · [Roadmap](README.md) · [Next: Phase 2 →](02-searching-and-linear.md)

About 40% of interview questions are arrays or strings. These six patterns cover almost all of them.

| # | Pattern | Turns this… | …into this |
|---|---|---|---|
| 1 | [Hashing](#pattern-1-hashing) | O(n²) "search again" | O(n) |
| 2 | [Two Pointers](#pattern-2-two-pointers) | O(n²) pairs | O(n) |
| 3 | [Sliding Window](#pattern-3-sliding-window) | O(n²) subarrays | O(n) |
| 4 | [Prefix Sum](#pattern-4-prefix-sum) | O(n) per range sum | O(1) per range sum |
| 5 | [Cyclic Sort](#pattern-5-cyclic-sort) | O(n) extra space | O(1) extra space |
| 6 | [Matrix Traversal](#pattern-6-matrix-traversal) | messy index code | one clean template |

---

## Pattern 1: Hashing

**Idea:** a `dict` or `set` remembers what you have already seen, and answers "have I seen X?" in O(1). It's like a coat check: you hand over a ticket and get your coat back instantly, instead of searching every hanger.

**Spot it when:**
- "Find a pair / a complement" (Two Sum).
- "Count how many times…", "most frequent", "duplicates".
- "Group things that are the same in some way" (anagrams).
- Your brute force has an inner loop that **searches** for something.

**Complexity:** O(n) time, O(n) space.

### Template A: "have I seen the partner?"

```python
def two_sum(nums, target):
    # Remember every number we have seen: value -> its index
    seen = {}
    # Walk through the list once
    for i in range(len(nums)):
        # The partner we need so the two add up to target
        need = target - nums[i]
        # Did we already see that partner?
        if need in seen:
            # Yes: return both positions
            return [seen[need], i]
        # No: remember this number for later
        seen[nums[i]] = i
    # No pair exists
    return []


print(two_sum([2, 7, 11, 15], 9))  # [0, 1]
```

### Template B: group by a key

```python
from collections import defaultdict


def group_anagrams(words):
    # key -> list of words that share that key
    groups = defaultdict(list)
    for word in words:
        # Anagrams become the same string once sorted
        key = "".join(sorted(word))
        # Put the word in its group
        groups[key].append(word)
    # We only need the groups, not the keys
    return list(groups.values())


print(group_anagrams(["eat", "tea", "tan", "ate", "nat", "bat"]))
# [['eat', 'tea', 'ate'], ['tan', 'nat'], ['bat']]
```

**The trap:** a `list` cannot be a dict key (it can change). Convert it to a `tuple` or a `string` first.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 1 | [Two Sum](https://leetcode.com/problems/two-sum/) ★ | Store value -> index; look up target - x |
| 217 | [Contains Duplicate](https://leetcode.com/problems/contains-duplicate/) ★ | set; seen before? |
| 242 | [Valid Anagram](https://leetcode.com/problems/valid-anagram/) ★ | Counter(s) == Counter(t) |
| 383 | [Ransom Note](https://leetcode.com/problems/ransom-note/) | Count letters, subtract |
| 205 | [Isomorphic Strings](https://leetcode.com/problems/isomorphic-strings/) | Two dicts: s->t and t->s |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 49 | [Group Anagrams](https://leetcode.com/problems/group-anagrams/) ★ | Key = sorted word or 26-count tuple |
| 347 | [Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/) ★ | Counter + bucket by frequency |
| 128 | [Longest Consecutive Sequence](https://leetcode.com/problems/longest-consecutive-sequence/) ★ | Only start counting at x where x-1 not in set |
| 36 | [Valid Sudoku](https://leetcode.com/problems/valid-sudoku/) ★ | Sets per row, col, box (r//3, c//3) |
| 238 | [Product of Array Except Self](https://leetcode.com/problems/product-of-array-except-self/) ★ | Prefix product x suffix product |
| 271 | [Encode and Decode Strings](https://leetcode.com/problems/encode-and-decode-strings/) ★ (Premium) | Length + '#' + word |
| 380 | [Insert Delete GetRandom O(1)](https://leetcode.com/problems/insert-delete-getrandom-o1/) ★ | List + dict of value -> index; swap with last to delete |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 41 | [First Missing Positive](https://leetcode.com/problems/first-missing-positive/) ★ | Use the array itself as the hash |
| 149 | [Max Points on a Line](https://leetcode.com/problems/max-points-on-a-line/) | Per point: dict of reduced slope (dy/g, dx/g) |

---

## Pattern 2: Two Pointers

**Idea:** use two index variables that move toward each other, or move in the same direction at different speeds. Each step throws away options you no longer need, so you check pairs in O(n) instead of O(n²).

Analogy: two people walk toward each other from both ends of a sorted shelf to find two books whose prices add to 100. If the total is too high, the right person steps left (cheaper). Too low, the left person steps right.

**Spot it when:**
- The array is **sorted** (or you can sort it) and you look for pairs / triplets.
- "Palindrome", "reverse", "compare from both ends".
- "Remove / move items **in place**".

**Complexity:** O(n) time (plus O(n log n) if you sort), O(1) space.

### Template A: opposite ends

```python
def pair_with_sum(sorted_nums, target):
    # Start at both ends
    left = 0
    right = len(sorted_nums) - 1
    # Stop when the pointers meet
    while left < right:
        total = sorted_nums[left] + sorted_nums[right]
        if total == target:
            return [left, right]
        if total < target:
            # Too small: the only way up is a bigger left number
            left = left + 1
        else:
            # Too big: the only way down is a smaller right number
            right = right - 1
    return []


print(pair_with_sum([1, 3, 4, 6, 9], 10))  # [0, 4]
```

### Template B: slow writer, fast reader (same direction)

```python
def remove_duplicates(sorted_nums):
    # Nothing to do for an empty list
    if len(sorted_nums) == 0:
        return 0
    # "write" is where the next unique value goes
    write = 1
    # "read" looks at every value once
    for read in range(1, len(sorted_nums)):
        # A new value is different from the last one we kept
        if sorted_nums[read] != sorted_nums[write - 1]:
            # Keep it
            sorted_nums[write] = sorted_nums[read]
            write = write + 1
    # Number of unique values
    return write


nums = [1, 1, 2, 3, 3]
k = remove_duplicates(nums)
print(k, nums[:k])  # 3 [1, 2, 3]
```

**The trap:** in 3Sum, forgetting to **skip duplicate values** gives repeated triplets. After using a value, move the pointer past every copy of it.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 125 | [Valid Palindrome](https://leetcode.com/problems/valid-palindrome/) ★ | Skip non-alphanumerics, compare ends |
| 26 | [Remove Duplicates from Sorted Array](https://leetcode.com/problems/remove-duplicates-from-sorted-array/) | Slow writer, fast reader |
| 283 | [Move Zeroes](https://leetcode.com/problems/move-zeroes/) | Slow writer for non-zeros |
| 88 | [Merge Sorted Array](https://leetcode.com/problems/merge-sorted-array/) ★ | Fill from the back |
| 977 | [Squares of a Sorted Array](https://leetcode.com/problems/squares-of-a-sorted-array/) | Biggest square is at one of the ends |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 167 | [Two Sum II - Input Array Is Sorted](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/) ★ | Sum too big -> move right left |
| 15 | [3Sum](https://leetcode.com/problems/3sum/) ★ | Sort, fix one, two-pointer the rest, skip duplicates |
| 11 | [Container With Most Water](https://leetcode.com/problems/container-with-most-water/) ★ | Move the shorter wall |
| 75 | [Sort Colors](https://leetcode.com/problems/sort-colors/) ★ | Dutch flag: low, mid, high pointers |
| 16 | [3Sum Closest](https://leetcode.com/problems/3sum-closest/) | Same as 3Sum, track best distance |
| 18 | [4Sum](https://leetcode.com/problems/4sum/) | Two loops + two pointers |
| 881 | [Boats to Save People](https://leetcode.com/problems/boats-to-save-people/) | Sort; pair heaviest with lightest |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 42 | [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) ★ | Water = min(maxLeft, maxRight) - height |

---

## Pattern 3: Sliding Window

**Idea:** a window is a continuous piece of the array, from `left` to `right`. Grow it by moving `right`. When it breaks a rule, shrink it by moving `left`. Each item enters once and leaves once, so it's O(n).

Analogy: a caterpillar crawling along a branch. The head moves forward. The tail catches up when the body gets too long.

**Spot it when:**
- The words **"subarray"** or **"substring"** (continuous!) plus "longest", "shortest", "at most K", "contains all".
- Fixed size: "every window of size k".

**Complexity:** O(n) time, O(k) space for the window's counts.

### Template A: variable-size window (longest valid window)

```python
def longest_unique_substring(s):
    # Characters inside the current window
    in_window = set()
    # Left edge of the window
    left = 0
    best = 0
    # Right edge moves one step each time
    for right in range(len(s)):
        # Shrink from the left until s[right] is no longer a repeat
        while s[right] in in_window:
            in_window.remove(s[left])
            left = left + 1
        # Now it's safe to add s[right]
        in_window.add(s[right])
        # Window size is right - left + 1
        best = max(best, right - left + 1)
    return best


print(longest_unique_substring("abcabcbb"))  # 3
```

### Template B: fixed-size window

```python
def max_sum_of_k(nums, k):
    # Sum of the first window
    window_sum = 0
    for i in range(k):
        window_sum = window_sum + nums[i]
    best = window_sum
    # Slide: add the new right item, drop the old left item
    for right in range(k, len(nums)):
        window_sum = window_sum + nums[right] - nums[right - k]
        best = max(best, window_sum)
    return best


print(max_sum_of_k([2, 1, 5, 1, 3, 2], 3))  # 9
```

**The trap:** "exactly K" is hard to slide directly. Use **exactly(K) = atMost(K) − atMost(K − 1)**. Also, sliding window needs the window to get "worse" as it grows. With **negative numbers** that breaks, so use Prefix Sum instead.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 121 | [Best Time to Buy and Sell Stock](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/) ★ | Track min price so far |
| 643 | [Maximum Average Subarray I](https://leetcode.com/problems/maximum-average-subarray-i/) | Fixed window: add right, drop left |
| 219 | [Contains Duplicate II](https://leetcode.com/problems/contains-duplicate-ii/) | Set of last k values |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 3 | [Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/) ★ | Shrink while char repeats |
| 424 | [Longest Repeating Character Replacement](https://leetcode.com/problems/longest-repeating-character-replacement/) ★ | Valid if window - maxFreq <= k |
| 567 | [Permutation in String](https://leetcode.com/problems/permutation-in-string/) ★ | Fixed window, compare counts |
| 209 | [Minimum Size Subarray Sum](https://leetcode.com/problems/minimum-size-subarray-sum/) ★ | Shrink while sum >= target |
| 438 | [Find All Anagrams in a String](https://leetcode.com/problems/find-all-anagrams-in-a-string/) | Fixed window counts |
| 1004 | [Max Consecutive Ones III](https://leetcode.com/problems/max-consecutive-ones-iii/) ★ | Shrink while zeros > k |
| 904 | [Fruit Into Baskets](https://leetcode.com/problems/fruit-into-baskets/) | Longest window with <= 2 distinct |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 76 | [Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/) ★ | Expand to valid, shrink to minimal |
| 992 | [Subarrays with K Different Integers](https://leetcode.com/problems/subarrays-with-k-different-integers/) | atMost(k) - atMost(k-1) |
| 30 | [Substring with Concatenation of All Words](https://leetcode.com/problems/substring-with-concatenation-of-all-words/) | Window per offset of word length |

---

## Pattern 4: Prefix Sum

**Idea:** `prefix[i]` = sum of the first `i` numbers. Then the sum of any range `l..r` is `prefix[r + 1] - prefix[l]` in O(1).

Analogy: a car's odometer. To know how far you drove between two towns, you subtract the two readings. You don't re-drive the road.

**Spot it when:**
- Many "sum of range" questions.
- "Count subarrays with sum = K" (especially with **negative numbers**).
- "Equal number of 0s and 1s", "sum divisible by K".

**Complexity:** O(n) to build, O(1) per query.

### Template A: build and query

```python
def build_prefix(nums):
    # prefix[0] = 0 means "sum of nothing"
    prefix = [0]
    for x in nums:
        # Each entry = previous total + this number
        prefix.append(prefix[-1] + x)
    return prefix


nums = [3, 1, 4, 1, 5]
prefix = build_prefix(nums)
# Sum of nums[1..3] = 1 + 4 + 1
print(prefix[3 + 1] - prefix[1])  # 6
```

### Template B: count subarrays with sum K (prefix sum + hashing)

```python
def subarray_sum_equals_k(nums, k):
    # How many times each running total has appeared
    # A running total of 0 has appeared once (before we start)
    seen_totals = {0: 1}
    running = 0
    count = 0
    for x in nums:
        running = running + x
        # If an earlier total was (running - k),
        # the part between then and now sums to k
        count = count + seen_totals.get(running - k, 0)
        # Record this total
        seen_totals[running] = seen_totals.get(running, 0) + 1
    return count


print(subarray_sum_equals_k([1, 2, 3, -2, 2], 3))  # 4
```

**The trap:** forgetting `{0: 1}`. Without it, you miss subarrays that start at index 0.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 1480 | [Running Sum of 1d Array](https://leetcode.com/problems/running-sum-of-1d-array/) | The prefix array itself |
| 724 | [Find Pivot Index](https://leetcode.com/problems/find-pivot-index/) | left sum == total - left - x |
| 303 | [Range Sum Query - Immutable](https://leetcode.com/problems/range-sum-query-immutable/) ★ | pre[r+1] - pre[l] |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 560 | [Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/) ★ | Count of prefix (sum - k) seen so far |
| 523 | [Continuous Subarray Sum](https://leetcode.com/problems/continuous-subarray-sum/) ★ | Same remainder twice -> multiple of k |
| 974 | [Subarray Sums Divisible by K](https://leetcode.com/problems/subarray-sums-divisible-by-k/) | Count remainders |
| 525 | [Contiguous Array](https://leetcode.com/problems/contiguous-array/) ★ | 0 -> -1, then longest sum 0 |
| 304 | [Range Sum Query 2D - Immutable](https://leetcode.com/problems/range-sum-query-2d-immutable/) ★ | 2D prefix, inclusion-exclusion |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 1074 | [Number of Submatrices That Sum to Target](https://leetcode.com/problems/number-of-submatrices-that-sum-to-target/) | Fix row pair, then 'subarray sum = k' |

---

## Pattern 5: Cyclic Sort

**Idea:** when the numbers are in the range `1..n`, each number has a "home" index: number `x` lives at index `x - 1`. Swap numbers into their homes. Whatever is not at home is missing or duplicated. O(n) time, O(1) extra space.

Analogy: a hotel where guest 5 must sleep in room 5. Walk the corridor and send each guest to their own room.

**Spot it when:** "numbers from 1 to n" (or 0 to n) plus "missing", "duplicate", "first missing positive", and "O(1) extra space".

```python
def find_missing_numbers(nums):
    i = 0
    while i < len(nums):
        # Where this number should live
        home = nums[i] - 1
        # If it's not home yet, and home doesn't already hold the same number
        if nums[i] != nums[home]:
            # Swap it into its home
            nums[i], nums[home] = nums[home], nums[i]
        else:
            # It's home (or a duplicate), move on
            i = i + 1
    missing = []
    for i in range(len(nums)):
        # Index i should hold the number i + 1
        if nums[i] != i + 1:
            missing.append(i + 1)
    return missing


print(find_missing_numbers([4, 3, 2, 7, 8, 2, 3, 1]))  # [5, 6]
```

**The trap:** checking `nums[i] != i + 1` instead of `nums[i] != nums[home]` causes an infinite loop when there are duplicates.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 268 | [Missing Number](https://leetcode.com/problems/missing-number/) ★ | Sum formula or XOR |
| 448 | [Find All Numbers Disappeared in an Array](https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/) | Mark index |x|-1 negative |
| 645 | [Set Mismatch](https://leetcode.com/problems/set-mismatch/) | Cyclic sort, then scan |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 442 | [Find All Duplicates in an Array](https://leetcode.com/problems/find-all-duplicates-in-an-array/) | Mark visited by negating |
| 287 | [Find the Duplicate Number](https://leetcode.com/problems/find-the-duplicate-number/) ★ | Floyd cycle on index -> value |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 41 | [First Missing Positive](https://leetcode.com/problems/first-missing-positive/) ★ | Put x at index x-1, then scan |

---

## Pattern 6: Matrix Traversal

**Idea:** a matrix is a list of rows. Most matrix questions need three tools: a **directions list** to move to neighbours, a **bounds check**, and a few index tricks (transpose, layer by layer).

**Spot it when:** a 2D grid. (If the grid is about **islands / regions / shortest path**, go to [Graphs](04-recursion-and-graphs.md#pattern-18-graph-bfs--dfs).)

```python
def neighbours(grid, r, c):
    # Up, down, left, right as (row change, col change)
    directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
    rows = len(grid)
    cols = len(grid[0])
    result = []
    for dr, dc in directions:
        nr = r + dr
        nc = c + dc
        # Keep only cells inside the grid
        if 0 <= nr < rows and 0 <= nc < cols:
            result.append((nr, nc))
    return result


def rotate_clockwise(matrix):
    n = len(matrix)
    # Step 1: transpose (swap across the main diagonal)
    for r in range(n):
        for c in range(r + 1, n):
            matrix[r][c], matrix[c][r] = matrix[c][r], matrix[r][c]
    # Step 2: reverse every row
    for row in matrix:
        row.reverse()
    return matrix


print(neighbours([[1, 2], [3, 4]], 0, 0))  # [(1, 0), (0, 1)]
print(rotate_clockwise([[1, 2, 3], [4, 5, 6], [7, 8, 9]]))
# [[7, 4, 1], [8, 5, 2], [9, 6, 3]]
```

**The trap:** `[[0] * cols] * rows` makes every row **the same list**. Changing one row changes all of them. Build rows in a loop instead.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 867 | [Transpose Matrix](https://leetcode.com/problems/transpose-matrix/) | res[c][r] = m[r][c] |
| 1572 | [Matrix Diagonal Sum](https://leetcode.com/problems/matrix-diagonal-sum/) | Don't double count the center |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 48 | [Rotate Image](https://leetcode.com/problems/rotate-image/) ★ | Transpose, then reverse each row |
| 54 | [Spiral Matrix](https://leetcode.com/problems/spiral-matrix/) ★ | Four walls: top, bottom, left, right |
| 73 | [Set Matrix Zeroes](https://leetcode.com/problems/set-matrix-zeroes/) ★ | Use first row/col as markers |
| 289 | [Game of Life](https://leetcode.com/problems/game-of-life/) | Encode old+new state in one cell |
| 240 | [Search a 2D Matrix II](https://leetcode.com/problems/search-a-2d-matrix-ii/) ★ | Start top-right: go left or down |
