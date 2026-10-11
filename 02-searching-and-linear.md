# Phase 2: Searching & Linear Structures

[← Phase 1](01-arrays-and-strings.md) · [Roadmap](README.md) · [Next: Phase 3 →](03-trees-and-heaps.md)

| # | Pattern | Key tool |
|---|---|---|
| 7 | [Binary Search](#pattern-7-binary-search) | halve the search space |
| 8 | [Stack](#pattern-8-stack) | last in, first out |
| 9 | [Monotonic Stack](#pattern-9-monotonic-stack) | "next greater / smaller" |
| 10 | [Linked List](#pattern-10-linked-list) | dummy node, fast & slow pointers |
| 11 | [Intervals](#pattern-11-intervals) | sort by start (or end) |

---

## Pattern 7: Binary Search

**Idea:** if you can split the options into a "no" part and a "yes" part (`F F F F T T T`), you can find the first `T` by checking the middle and throwing half away each time. O(log n).

Analogy: guessing a number from 1 to 100 when someone says "higher" or "lower". You need at most 7 guesses.

**Spot it when:**
- The array is **sorted** (or rotated sorted).
- "Find the minimum X such that…" / "maximum X such that…". This is **binary search on the answer**: the answer itself is the thing you search.
- n up to 10⁹, or the expected answer is O(log n).

**Complexity:** O(log n) checks.

### Template A: find the first "yes" (use this one for everything)

```python
def first_true(lo, hi, is_ok):
    # Search the range [lo, hi]. is_ok must look like F F F T T T.
    while lo < hi:
        # Middle (rounds down)
        mid = (lo + hi) // 2
        if is_ok(mid):
            # mid works, but maybe something smaller works too
            hi = mid
        else:
            # mid fails, so the answer is to the right
            lo = mid + 1
    # lo == hi: the first value where is_ok is True
    return lo


def lower_bound(nums, target):
    # First index whose value is >= target (len(nums) if none)
    def is_ok(i):
        return nums[i] >= target
    if len(nums) == 0 or nums[-1] < target:
        return len(nums)
    return first_true(0, len(nums) - 1, is_ok)


print(lower_bound([1, 3, 5, 7], 5))  # 2
print(lower_bound([1, 3, 5, 7], 4))  # 2
```

### Template B: binary search on the answer

"Koko eats bananas: what is the **slowest** speed that finishes in `h` hours?" Speeds that are too slow fail, fast enough speeds work: `F F F T T T`.

```python
def min_eating_speed(piles, h):
    def can_finish(speed):
        hours = 0
        for pile in piles:
            # Ceiling division: (pile + speed - 1) // speed
            hours = hours + (pile + speed - 1) // speed
        return hours <= h
    # Slowest possible speed is 1, fastest needed is the biggest pile
    return first_true(1, max(piles), can_finish)


print(min_eating_speed([3, 6, 7, 11], 8))  # 4
```

**The trap:** infinite loops from mixing styles. With `while lo < hi`, always use `hi = mid` and `lo = mid + 1`. Never `lo = mid`.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 704 | [Binary Search](https://leetcode.com/problems/binary-search/) ★ | Classic lo <= hi |
| 35 | [Search Insert Position](https://leetcode.com/problems/search-insert-position/) | First index with value >= target |
| 278 | [First Bad Version](https://leetcode.com/problems/first-bad-version/) | First True in F F F T T |
| 69 | [Sqrt(x)](https://leetcode.com/problems/sqrtx/) | Last x with x*x <= n |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 74 | [Search a 2D Matrix](https://leetcode.com/problems/search-a-2d-matrix/) ★ | Treat as one sorted array: r = i // cols |
| 33 | [Search in Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/) ★ | One half is always sorted |
| 153 | [Find Minimum in Rotated Sorted Array](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/) ★ | Compare mid with right end |
| 34 | [Find First and Last Position of Element in Sorted Array](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) ★ | Two searches: first >= t, first > t |
| 162 | [Find Peak Element](https://leetcode.com/problems/find-peak-element/) | Walk uphill: if nums[m] < nums[m+1] go right |
| 981 | [Time Based Key-Value Store](https://leetcode.com/problems/time-based-key-value-store/) ★ | bisect on timestamps |
| 875 | [Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/) ★ | Search on speed; check hours |
| 1011 | [Capacity To Ship Packages Within D Days](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/) ★ | Search on capacity |
| 1482 | [Minimum Number of Days to Make m Bouquets](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/) | Search on days |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 4 | [Median of Two Sorted Arrays](https://leetcode.com/problems/median-of-two-sorted-arrays/) ★ | Binary search the partition of the smaller array |
| 410 | [Split Array Largest Sum](https://leetcode.com/problems/split-array-largest-sum/) ★ | Search on max sum; greedy split count |
| 668 | [Kth Smallest Number in Multiplication Table](https://leetcode.com/problems/kth-smallest-number-in-multiplication-table/) | Search on value; count <= mid per row |

---

## Pattern 8: Stack

**Idea:** a stack is a pile of plates. You only add to the top and take from the top (**LIFO**: last in, first out). In Python, a `list` with `append()` and `pop()` is a stack.

**Spot it when:**
- Matching pairs: brackets, tags.
- "Undo", "go back", nested things like `3[a2[c]]`.
- Evaluating expressions.

### Template: matching brackets

```python
def is_valid(s):
    # Which opener each closer needs
    pairs = {")": "(", "]": "[", "}": "{"}
    stack = []
    for ch in s:
        if ch in pairs:
            # A closer: the top of the stack must be its opener
            if len(stack) == 0 or stack[-1] != pairs[ch]:
                return False
            stack.pop()
        else:
            # An opener: wait for its closer
            stack.append(ch)
    # Every opener must be closed
    return len(stack) == 0


print(is_valid("({[]})"))  # True
print(is_valid("(]"))  # False
```

**The trap:** calling `stack[-1]` or `stack.pop()` on an empty stack crashes. Always check `len(stack) > 0` first.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 20 | [Valid Parentheses](https://leetcode.com/problems/valid-parentheses/) ★ | Push opens, pop on close |
| 232 | [Implement Queue using Stacks](https://leetcode.com/problems/implement-queue-using-stacks/) | In-stack and out-stack |
| 1047 | [Remove All Adjacent Duplicates In String](https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/) | Pop if top equals current |
| 844 | [Backspace String Compare](https://leetcode.com/problems/backspace-string-compare/) | Stack, or scan from the end |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 155 | [Min Stack](https://leetcode.com/problems/min-stack/) ★ | Store (value, min so far) |
| 150 | [Evaluate Reverse Polish Notation](https://leetcode.com/problems/evaluate-reverse-polish-notation/) ★ | Pop two, push result; int(a / b) for division |
| 394 | [Decode String](https://leetcode.com/problems/decode-string/) ★ | Push (string, count) on '[' |
| 71 | [Simplify Path](https://leetcode.com/problems/simplify-path/) | Split by '/', handle '..' |
| 735 | [Asteroid Collision](https://leetcode.com/problems/asteroid-collision/) ★ | Collide only when top > 0 and new < 0 |
| 227 | [Basic Calculator II](https://leetcode.com/problems/basic-calculator-ii/) ★ | Apply * and / at once; push +/- numbers |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 224 | [Basic Calculator](https://leetcode.com/problems/basic-calculator/) ★ | Push (result, sign) on '(' |
| 32 | [Longest Valid Parentheses](https://leetcode.com/problems/longest-valid-parentheses/) | Stack of indices; base index -1 |

---

## Pattern 9: Monotonic Stack

**Idea:** keep a stack whose values are always in order (for example, always decreasing). When a new item breaks the order, pop the smaller items: **the new item is their "next greater element"**. Each item is pushed and popped once, so it's O(n).

Analogy: people standing in a line, looking right. Each person can see the first taller person ahead of them. When a tall person arrives, everyone shorter who was "waiting" has found their answer.

**Spot it when:** "next greater", "next smaller", "previous smaller", "how many days until warmer", "largest rectangle", "remove digits to make the smallest number".

### Template: next greater element

```python
def next_greater(nums):
    # Default answer: -1 (no greater element to the right)
    answer = [-1] * len(nums)
    # Stack of INDEXES still waiting for a greater element
    stack = []
    for i in range(len(nums)):
        # The current number is the answer for every smaller waiting number
        while len(stack) > 0 and nums[stack[-1]] < nums[i]:
            waiting_index = stack.pop()
            answer[waiting_index] = nums[i]
        # Now the current index waits too
        stack.append(i)
    return answer


print(next_greater([2, 1, 2, 4, 3]))  # [4, 2, 4, -1, -1]
```

| Want | Stack keeps | Pop while |
|---|---|---|
| next **greater** | decreasing values | `nums[top] < nums[i]` |
| next **smaller** | increasing values | `nums[top] > nums[i]` |

**The trap:** store **indexes**, not values. Many questions need the distance (`i - index`) or need to write into an answer array.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 496 | [Next Greater Element I](https://leetcode.com/problems/next-greater-element-i/) | Decreasing stack + dict |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 739 | [Daily Temperatures](https://leetcode.com/problems/daily-temperatures/) ★ | Stack of indices waiting for a warmer day |
| 503 | [Next Greater Element II](https://leetcode.com/problems/next-greater-element-ii/) | Loop twice (i % n) |
| 901 | [Online Stock Span](https://leetcode.com/problems/online-stock-span/) | Stack of (price, span) |
| 853 | [Car Fleet](https://leetcode.com/problems/car-fleet/) ★ | Sort by position; stack of arrival times |
| 907 | [Sum of Subarray Minimums](https://leetcode.com/problems/sum-of-subarray-minimums/) ★ | Count how many subarrays each x is the min of |
| 402 | [Remove K Digits](https://leetcode.com/problems/remove-k-digits/) ★ | Increasing stack; pop bigger digits |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 84 | [Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/) ★ | On pop: width = i - new top - 1 |
| 85 | [Maximal Rectangle](https://leetcode.com/problems/maximal-rectangle/) ★ | Histogram per row |

---

## Pattern 10: Linked List

**Idea:** a linked list is a chain of nodes. Each node knows its value and the next node. You can't jump to the middle; you must walk. Three tricks solve almost everything:
1. **Dummy node** before the head, so you never special-case "the head changed".
2. **Fast & slow pointers**: fast moves 2 steps, slow moves 1. Finds the middle and detects cycles.
3. **Reverse in place** with `prev`, `cur`, `nxt`.

**Spot it when:** the input is a `ListNode`. Also "find the duplicate in 1..n with O(1) space" (it's secretly a cycle).

```python
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


def reverse(head):
    prev = None
    cur = head
    while cur is not None:
        # Save the rest of the list before we break the link
        nxt = cur.next
        # Point this node backwards
        cur.next = prev
        # Step forward
        prev = cur
        cur = nxt
    # prev is the new head
    return prev


def middle(head):
    slow = head
    fast = head
    # Fast moves twice as fast; when it ends, slow is in the middle
    while fast is not None and fast.next is not None:
        slow = slow.next
        fast = fast.next.next
    return slow


def merge_sorted(a, b):
    # Dummy node: we build the answer after it
    dummy = ListNode()
    tail = dummy
    while a is not None and b is not None:
        if a.val <= b.val:
            tail.next = a
            a = a.next
        else:
            tail.next = b
            b = b.next
        tail = tail.next
    # Attach whatever is left
    if a is not None:
        tail.next = a
    else:
        tail.next = b
    return dummy.next


def build(values):
    dummy = ListNode()
    tail = dummy
    for v in values:
        tail.next = ListNode(v)
        tail = tail.next
    return dummy.next


def to_list(head):
    out = []
    while head is not None:
        out.append(head.val)
        head = head.next
    return out


print(to_list(reverse(build([1, 2, 3, 4]))))  # [4, 3, 2, 1]
print(middle(build([1, 2, 3, 4, 5])).val)  # 3
print(to_list(merge_sorted(build([1, 4]), build([2, 3, 5]))))  # [1, 2, 3, 4, 5]
```

**The trap:** losing the rest of the list. Always save `nxt = cur.next` **before** changing `cur.next`.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 206 | [Reverse Linked List](https://leetcode.com/problems/reverse-linked-list/) ★ | prev, cur, nxt |
| 21 | [Merge Two Sorted Lists](https://leetcode.com/problems/merge-two-sorted-lists/) ★ | Dummy head |
| 141 | [Linked List Cycle](https://leetcode.com/problems/linked-list-cycle/) ★ | Fast and slow meet |
| 876 | [Middle of the Linked List](https://leetcode.com/problems/middle-of-the-linked-list/) | Slow is the middle when fast ends |
| 234 | [Palindrome Linked List](https://leetcode.com/problems/palindrome-linked-list/) ★ | Find middle, reverse second half, compare |
| 160 | [Intersection of Two Linked Lists](https://leetcode.com/problems/intersection-of-two-linked-lists/) | Switch heads at the end |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 19 | [Remove Nth Node From End of List](https://leetcode.com/problems/remove-nth-node-from-end-of-list/) ★ | Fast goes n steps ahead |
| 143 | [Reorder List](https://leetcode.com/problems/reorder-list/) ★ | Middle + reverse + merge |
| 142 | [Linked List Cycle II](https://leetcode.com/problems/linked-list-cycle-ii/) ★ | After meeting, restart one from head |
| 2 | [Add Two Numbers](https://leetcode.com/problems/add-two-numbers/) ★ | Carry, dummy head |
| 138 | [Copy List with Random Pointer](https://leetcode.com/problems/copy-list-with-random-pointer/) ★ | dict old -> new |
| 146 | [LRU Cache](https://leetcode.com/problems/lru-cache/) ★ | dict + doubly linked list (or OrderedDict) |
| 92 | [Reverse Linked List II](https://leetcode.com/problems/reverse-linked-list-ii/) | Reverse a slice; keep the node before it |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 25 | [Reverse Nodes in k-Group](https://leetcode.com/problems/reverse-nodes-in-k-group/) ★ | Check k nodes exist, reverse, reconnect |
| 23 | [Merge k Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/) ★ | Min-heap of list heads |
| 460 | [LFU Cache](https://leetcode.com/problems/lfu-cache/) | freq -> OrderedDict; track min freq |

---

## Pattern 11: Intervals

**Idea:** an interval is `[start, end]`. Sort them first, then walk left to right comparing each one with the last one you kept. Two intervals overlap when `next.start <= last.end`.

**Spot it when:** meetings, bookings, ranges, "merge", "overlap", "minimum rooms", "remove fewest to stop overlap".

| Goal | Sort by |
|---|---|
| Merge / insert | start |
| Keep the most non-overlapping (remove the fewest) | **end** |
| Count rooms needed at the same time | start, plus a min-heap of end times |

```python
def merge_intervals(intervals):
    # Sort by start time
    intervals.sort(key=lambda interval: interval[0])
    merged = []
    for start, end in intervals:
        # Overlaps the last kept interval?
        if len(merged) > 0 and start <= merged[-1][1]:
            # Stretch the last interval's end
            merged[-1][1] = max(merged[-1][1], end)
        else:
            # No overlap: keep it as a new interval
            merged.append([start, end])
    return merged


print(merge_intervals([[1, 3], [8, 10], [2, 6], [15, 18]]))
# [[1, 6], [8, 10], [15, 18]]
```

**The trap:** using `end = end_of_new` instead of `max(...)`. `[1, 10]` and `[2, 3]` merge to `[1, 10]`, not `[1, 3]`.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 228 | [Summary Ranges](https://leetcode.com/problems/summary-ranges/) | Extend while next == prev + 1 |
| 252 | [Meeting Rooms](https://leetcode.com/problems/meeting-rooms/) ★ (Premium) | Sort by start; any overlap? |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 56 | [Merge Intervals](https://leetcode.com/problems/merge-intervals/) ★ | Sort by start; merge if start <= last end |
| 57 | [Insert Interval](https://leetcode.com/problems/insert-interval/) ★ | Before, overlap, after |
| 435 | [Non-overlapping Intervals](https://leetcode.com/problems/non-overlapping-intervals/) ★ | Sort by end; keep earliest ending |
| 253 | [Meeting Rooms II](https://leetcode.com/problems/meeting-rooms-ii/) ★ (Premium) | Min-heap of end times |
| 452 | [Minimum Number of Arrows to Burst Balloons](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/) | Sort by end; greedy |
| 986 | [Interval List Intersections](https://leetcode.com/problems/interval-list-intersections/) | Two pointers; drop the one ending first |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 759 | [Employee Free Time](https://leetcode.com/problems/employee-free-time/) (Premium) | Merge all, gaps are free |
| 352 | [Data Stream as Disjoint Intervals](https://leetcode.com/problems/data-stream-as-disjoint-intervals/) | Sorted structure, merge neighbours |
| 1851 | [Minimum Interval to Include Each Query](https://leetcode.com/problems/minimum-interval-to-include-each-query/) | Sort queries + heap of (size, end) |
