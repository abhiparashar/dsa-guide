# Phase 6: Advanced (the top 1% edge)

[← Phase 5](05-dp-and-greedy.md) · [Roadmap](README.md)

These show up mostly in **Hard** questions and in on-site rounds at Google, Meta and similar companies. Learn them after Phases 1–5 feel comfortable.

| # | Pattern | Key tool |
|---|---|---|
| 31 | [Monotonic Queue](#pattern-31-monotonic-queue) | max / min of a sliding window in O(1) |
| 32 | [Fenwick Tree & Segment Tree](#pattern-32-fenwick-tree--segment-tree) | range sums with updates in O(log n) |
| 33 | [String Matching (KMP, Rolling Hash)](#pattern-33-string-matching-kmp--rolling-hash) | find a pattern in O(n + m) |
| 34 | [Line Sweep](#pattern-34-line-sweep--difference-array) | events sorted by time |
| 35 | [Design Questions](#pattern-35-design-questions) | combine data structures |

---

## Pattern 31: Monotonic Queue

**Idea:** a `deque` of indexes whose values are always decreasing. The front is the maximum of the current window. When a new value arrives, remove smaller values from the back (they can never be the max again). When the front leaves the window, remove it from the front.

**Spot it when:** "maximum / minimum of every window of size k", DP where `dp[i]` needs the best of the last k values, "shortest subarray with sum at least K" with negatives.

```python
from collections import deque


def max_sliding_window(nums, k):
    # Indexes; their values are decreasing from front to back
    window = deque()
    result = []
    for i in range(len(nums)):
        # Smaller values at the back can never be the max again
        while len(window) > 0 and nums[window[-1]] <= nums[i]:
            window.pop()
        window.append(i)
        # Front index has slid out of the window
        if window[0] <= i - k:
            window.popleft()
        # Once the first full window exists, record its max
        if i >= k - 1:
            result.append(nums[window[0]])
    return result


print(max_sliding_window([1, 3, -1, -3, 5, 3, 6, 7], 3))  # [3, 3, 5, 5, 6, 7]
```

### Questions

_No good Easy questions for this pattern. Start with Medium._

**Medium**

| # | Problem | Hint |
|---|---|---|
| 1438 | [Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit](https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/) ★ | Max deque + min deque |
| 1696 | [Jump Game VI](https://leetcode.com/problems/jump-game-vi/) | dp + max deque of last k |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 239 | [Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) ★ | Deque of indices, values decreasing |
| 862 | [Shortest Subarray with Sum at Least K](https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/) ★ | Increasing deque of prefix sums |
| 1425 | [Constrained Subsequence Sum](https://leetcode.com/problems/constrained-subsequence-sum/) | dp + max deque |

---

## Pattern 32: Fenwick Tree & Segment Tree

**Idea:** prefix sums are O(1) to query but O(n) to update. A **Fenwick tree** (also called a **BIT**, Binary Indexed Tree) makes **both** O(log n). A **segment tree** does the same for min / max / any combine rule, and supports range updates.

**Spot it when:** "update a value, then ask a range sum" many times; "count smaller numbers after self"; "count inversions / reverse pairs".

```python
class FenwickTree:
    def __init__(self, n):
        # 1-indexed; tree[0] is unused
        self.tree = [0] * (n + 1)

    def add(self, i, delta):
        # Add delta at position i (1-indexed)
        while i < len(self.tree):
            self.tree[i] = self.tree[i] + delta
            # Jump to the next node that covers i
            i = i + (i & -i)

    def prefix_sum(self, i):
        # Sum of positions 1..i
        total = 0
        while i > 0:
            total = total + self.tree[i]
            # Jump to the previous block
            i = i - (i & -i)
        return total

    def range_sum(self, left, right):
        return self.prefix_sum(right) - self.prefix_sum(left - 1)


ft = FenwickTree(5)
values = [1, 3, 5, 7, 9]
for pos in range(1, 6):
    ft.add(pos, values[pos - 1])
print(ft.range_sum(2, 4))  # 15
# Change position 3 from 5 to 6
ft.add(3, 1)
print(ft.range_sum(2, 4))  # 16
```

**The trap:** Fenwick trees are **1-indexed**. Calling `add(0, …)` loops forever because `0 & -0 == 0`.

### Questions

_No good Easy questions for this pattern. Start with Medium._

**Medium**

| # | Problem | Hint |
|---|---|---|
| 307 | [Range Sum Query - Mutable](https://leetcode.com/problems/range-sum-query-mutable/) ★ | Fenwick tree: update + prefix query |
| 729 | [My Calendar I](https://leetcode.com/problems/my-calendar-i/) | Sorted list + bisect |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 315 | [Count of Smaller Numbers After Self](https://leetcode.com/problems/count-of-smaller-numbers-after-self/) ★ | Fenwick on ranks, right to left |
| 493 | [Reverse Pairs](https://leetcode.com/problems/reverse-pairs/) | Merge sort count (or Fenwick) |
| 327 | [Count of Range Sum](https://leetcode.com/problems/count-of-range-sum/) | Merge sort on prefix sums |
| 732 | [My Calendar III](https://leetcode.com/problems/my-calendar-iii/) | Difference map, max running sum |
| 715 | [Range Module](https://leetcode.com/problems/range-module/) | Sorted interval list |
| 699 | [Falling Squares](https://leetcode.com/problems/falling-squares/) | Coordinate compress + segment tree |

---

## Pattern 33: String Matching (KMP & Rolling Hash)

**Idea:** naive search for a pattern in a text restarts from scratch after a mismatch: O(n · m). **KMP** (Knuth-Morris-Pratt) precomputes, for each prefix of the pattern, the longest part that is **both a prefix and a suffix** (the `lps` array). On a mismatch it jumps back using `lps` instead of restarting. O(n + m).

A **rolling hash** (Rabin-Karp) turns each window of text into a number that updates in O(1) as the window slides.

**Spot it when:** "find pattern in text", "repeated substring", "shortest palindrome by adding letters in front", "longest duplicate substring".

```python
def build_lps(pattern):
    # lps[i] = length of the longest proper prefix of pattern[:i+1]
    #          that is also a suffix of it
    lps = [0] * len(pattern)
    length = 0
    i = 1
    while i < len(pattern):
        if pattern[i] == pattern[length]:
            length = length + 1
            lps[i] = length
            i = i + 1
        elif length > 0:
            # Fall back to the next shorter prefix-suffix
            length = lps[length - 1]
        else:
            lps[i] = 0
            i = i + 1
    return lps


def kmp_search(text, pattern):
    lps = build_lps(pattern)
    # j = how many pattern letters currently match
    j = 0
    for i in range(len(text)):
        # On mismatch, fall back instead of restarting
        while j > 0 and text[i] != pattern[j]:
            j = lps[j - 1]
        if text[i] == pattern[j]:
            j = j + 1
        if j == len(pattern):
            # Match ends at i
            return i - len(pattern) + 1
    return -1


print(build_lps("aabaaab"))  # [0, 1, 0, 1, 2, 2, 3]
print(kmp_search("sadbutsad", "sad"))  # 0
print(kmp_search("hello", "ll"))  # 2
```

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 28 | [Find the Index of the First Occurrence in a String](https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/) ★ | KMP template |
| 459 | [Repeated Substring Pattern](https://leetcode.com/problems/repeated-substring-pattern/) | LPS: n % (n - lps[-1]) == 0 |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 187 | [Repeated DNA Sequences](https://leetcode.com/problems/repeated-dna-sequences/) | Set of 10-length windows (rolling hash) |
| 686 | [Repeated String Match](https://leetcode.com/problems/repeated-string-match/) | Repeat until length >= b, try +1 |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 214 | [Shortest Palindrome](https://leetcode.com/problems/shortest-palindrome/) ★ | KMP on s + '#' + reversed s |
| 1392 | [Longest Happy Prefix](https://leetcode.com/problems/longest-happy-prefix/) | lps[-1] |
| 1044 | [Longest Duplicate Substring](https://leetcode.com/problems/longest-duplicate-substring/) | Binary search length + rolling hash |

---

## Pattern 34: Line Sweep & Difference Array

**Idea:** turn every interval into two **events**: `+1` at the start, `-1` at the end. Sort the events and walk through them, keeping a running total. The running total is "how many are active right now".

Analogy: a turnstile counter at a stadium. +1 when someone enters, −1 when they leave. The highest reading is the peak crowd.

**Spot it when:** "maximum overlap", "rooms needed", "car pooling capacity", "skyline", "booked at the same time".

```python
def min_meeting_rooms(intervals):
    events = []
    for start, end in intervals:
        events.append((start, 1))
        events.append((end, -1))
    # Sort by time; at the same time, -1 (leave) comes before +1 (enter)
    events.sort()
    active = 0
    peak = 0
    for time, change in events:
        active = active + change
        peak = max(peak, active)
    return peak


print(min_meeting_rooms([[0, 30], [5, 10], [15, 20]]))  # 2
print(min_meeting_rooms([[1, 5], [5, 10]]))  # 1
```

### Questions

_No good Easy questions for this pattern. Start with Medium._

**Medium**

| # | Problem | Hint |
|---|---|---|
| 1094 | [Car Pooling](https://leetcode.com/problems/car-pooling/) ★ | Difference array on stops |
| 731 | [My Calendar II](https://leetcode.com/problems/my-calendar-ii/) | Sweep +1/-1 events |
| 253 | [Meeting Rooms II](https://leetcode.com/problems/meeting-rooms-ii/) ★ (Premium) | Sweep starts and ends |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 218 | [The Skyline Problem](https://leetcode.com/problems/the-skyline-problem/) ★ | Events + max-heap of heights |
| 850 | [Rectangle Area II](https://leetcode.com/problems/rectangle-area-ii/) | Sweep x; covered y length |

---

## Pattern 35: Design Questions

**Idea:** "implement a class" questions. The trick is always to **combine two simple structures** so every method is O(1) or O(log n).

| Question | Combine |
|---|---|
| LRU Cache | dict + doubly linked list (or `OrderedDict`) |
| Insert / Delete / GetRandom O(1) | list + dict of value → index |
| Min Stack | stack of (value, min so far) |
| Median of a stream | max-heap + min-heap |
| Time-based key-value | dict + sorted list + `bisect` |
| LFU Cache | dict of frequency → `OrderedDict` + min frequency |

```python
from collections import OrderedDict


class LRUCache:
    def __init__(self, capacity):
        self.capacity = capacity
        # Remembers insertion order; the front is the least recently used
        self.data = OrderedDict()

    def get(self, key):
        if key not in self.data:
            return -1
        # Just used: move it to the back (most recent)
        self.data.move_to_end(key)
        return self.data[key]

    def put(self, key, value):
        if key in self.data:
            self.data.move_to_end(key)
        self.data[key] = value
        if len(self.data) > self.capacity:
            # Remove the least recently used (the front)
            self.data.popitem(last=False)


cache = LRUCache(2)
cache.put(1, 1)
cache.put(2, 2)
print(cache.get(1))  # 1
cache.put(3, 3)
print(cache.get(2))  # -1
```

**The trap:** some interviewers say "no `OrderedDict`". Be ready to build the doubly linked list yourself: a dict maps key → node, and the list keeps the order.

### Questions

_No good Easy questions for this pattern. Start with Medium._

**Medium**

| # | Problem | Hint |
|---|---|---|
| 155 | [Min Stack](https://leetcode.com/problems/min-stack/) ★ | Pairs of (value, min) |
| 146 | [LRU Cache](https://leetcode.com/problems/lru-cache/) ★ | OrderedDict or dict + doubly linked list |
| 380 | [Insert Delete GetRandom O(1)](https://leetcode.com/problems/insert-delete-getrandom-o1/) ★ | List + index dict |
| 981 | [Time Based Key-Value Store](https://leetcode.com/problems/time-based-key-value-store/) ★ | dict of sorted lists + bisect |
| 362 | [Design Hit Counter](https://leetcode.com/problems/design-hit-counter/) (Premium) | Deque of timestamps |
| 1396 | [Design Underground System](https://leetcode.com/problems/design-underground-system/) | Two dicts: check-ins, (from, to) totals |
| 1146 | [Snapshot Array](https://leetcode.com/problems/snapshot-array/) | Per index: list of (snap_id, value) + bisect |
| 355 | [Design Twitter](https://leetcode.com/problems/design-twitter/) | Heap merge of followees' tweets |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 295 | [Find Median from Data Stream](https://leetcode.com/problems/find-median-from-data-stream/) ★ | Two heaps |
| 460 | [LFU Cache](https://leetcode.com/problems/lfu-cache/) | freq -> OrderedDict + min freq |
| 432 | [All O`one Data Structure](https://leetcode.com/problems/all-oone-data-structure/) | Doubly linked list of count buckets |
