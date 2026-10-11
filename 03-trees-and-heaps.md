# Phase 3: Trees & Heaps

[← Phase 2](02-searching-and-linear.md) · [Roadmap](README.md) · [Next: Phase 4 →](04-recursion-and-graphs.md)

| # | Pattern | Key tool |
|---|---|---|
| 12 | [Tree DFS](#pattern-12-tree-dfs) | recursion: ask children, combine |
| 13 | [Tree BFS](#pattern-13-tree-bfs) | queue, level by level |
| 14 | [Binary Search Tree](#pattern-14-binary-search-tree) | left < node < right, inorder is sorted |
| 15 | [Heap / Top-K](#pattern-15-heap--top-k) | `heapq`, keep only k items |
| 16 | [Trie](#pattern-16-trie) | prefix tree of characters |

All tree code below uses this node:

```python
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right
```

---

## Pattern 12: Tree DFS

**Idea:** **DFS** (Depth-First Search) goes as deep as possible before coming back. For trees, write a recursive function that:
1. Handles the empty tree (`None`).
2. Asks the left child and the right child for their answers.
3. Combines them into the answer for this node.

Analogy: a manager asks each of their two team leads "how many people are under you?", adds them up, adds 1 for themself, and reports upward.

**Spot it when:** almost every tree question. Height, diameter, path sums, "is it balanced", LCA (lowest common ancestor), build a tree from traversals.

### Template A: return a value upward

```python
def max_depth(node):
    # An empty tree has depth 0
    if node is None:
        return 0
    # Ask both children
    left_depth = max_depth(node.left)
    right_depth = max_depth(node.right)
    # Combine: the deeper child + this node
    return 1 + max(left_depth, right_depth)
```

### Template B: return one thing, record another

Some answers (diameter, max path sum) can **bend** through a node, but a parent can only use a **straight** branch. So the function returns the straight branch and updates a separate "best" answer.

```python
def diameter(root):
    # A list so the inner function can change it
    best = [0]

    def height(node):
        if node is None:
            return 0
        left = height(node.left)
        right = height(node.right)
        # A path bending through this node uses both sides
        best[0] = max(best[0], left + right)
        # The parent can only continue one side
        return 1 + max(left, right)

    height(root)
    return best[0]


#        1
#       / \
#      2   3
#     / \
#    4   5
root = TreeNode(1, TreeNode(2, TreeNode(4), TreeNode(5)), TreeNode(3))
print(max_depth(root))  # 3
print(diameter(root))  # 3
```

| Traversal | Order | Used for |
|---|---|---|
| Preorder | node, left, right | copy / serialize a tree |
| Inorder | left, node, right | BST gives **sorted** order |
| Postorder | left, right, node | children's answers needed first (height, diameter) |

**The trap:** confusing "what I return to my parent" with "the final answer". If they differ, you need Template B.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 104 | [Maximum Depth of Binary Tree](https://leetcode.com/problems/maximum-depth-of-binary-tree/) ★ | 1 + max(left, right) |
| 226 | [Invert Binary Tree](https://leetcode.com/problems/invert-binary-tree/) ★ | Swap children, recurse |
| 543 | [Diameter of Binary Tree](https://leetcode.com/problems/diameter-of-binary-tree/) ★ | Return height, update best = l + r |
| 110 | [Balanced Binary Tree](https://leetcode.com/problems/balanced-binary-tree/) ★ | Return -1 if unbalanced |
| 100 | [Same Tree](https://leetcode.com/problems/same-tree/) ★ | Compare node by node |
| 572 | [Subtree of Another Tree](https://leetcode.com/problems/subtree-of-another-tree/) ★ | sameTree at every node |
| 112 | [Path Sum](https://leetcode.com/problems/path-sum/) | Subtract as you go down |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 236 | [Lowest Common Ancestor of a Binary Tree](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/) ★ | Found in both sides -> current node |
| 105 | [Construct Binary Tree from Preorder and Inorder Traversal](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/) ★ | preorder[0] is root; split inorder |
| 113 | [Path Sum II](https://leetcode.com/problems/path-sum-ii/) | Backtrack the path list |
| 437 | [Path Sum III](https://leetcode.com/problems/path-sum-iii/) ★ | Prefix sum on root-to-node path |
| 1448 | [Count Good Nodes in Binary Tree](https://leetcode.com/problems/count-good-nodes-in-binary-tree/) ★ | Pass max so far down |
| 114 | [Flatten Binary Tree to Linked List](https://leetcode.com/problems/flatten-binary-tree-to-linked-list/) | Reverse preorder with prev pointer |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 124 | [Binary Tree Maximum Path Sum](https://leetcode.com/problems/binary-tree-maximum-path-sum/) ★ | Return best single branch; update with both |
| 297 | [Serialize and Deserialize Binary Tree](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/) ★ | Preorder with '#' for None |
| 968 | [Binary Tree Cameras](https://leetcode.com/problems/binary-tree-cameras/) | Post-order states: covered, has camera, needs camera |

---

## Pattern 13: Tree BFS

**Idea:** **BFS** (Breadth-First Search) visits the tree **level by level**, using a queue. Take a snapshot of the queue size at the start of each level so you know where the level ends.

**Spot it when:** "level order", "each level", "right side view", "minimum depth", "zigzag", "width".

```python
from collections import deque


def level_order(root):
    if root is None:
        return []
    levels = []
    queue = deque([root])
    while len(queue) > 0:
        # Everything in the queue right now is one level
        level_size = len(queue)
        level = []
        for _ in range(level_size):
            node = queue.popleft()
            level.append(node.val)
            # Children belong to the next level
            if node.left is not None:
                queue.append(node.left)
            if node.right is not None:
                queue.append(node.right)
        levels.append(level)
    return levels


root = TreeNode(3, TreeNode(9), TreeNode(20, TreeNode(15), TreeNode(7)))
print(level_order(root))  # [[3], [9, 20], [15, 7]]
```

**The trap:** using `list.pop(0)` instead of `deque.popleft()`. `pop(0)` is O(n), which turns BFS into O(n²).

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 637 | [Average of Levels in Binary Tree](https://leetcode.com/problems/average-of-levels-in-binary-tree/) | Level loop, sum / count |
| 111 | [Minimum Depth of Binary Tree](https://leetcode.com/problems/minimum-depth-of-binary-tree/) | First leaf found by BFS |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 102 | [Binary Tree Level Order Traversal](https://leetcode.com/problems/binary-tree-level-order-traversal/) ★ | for _ in range(len(q)) |
| 103 | [Binary Tree Zigzag Level Order Traversal](https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/) ★ | Reverse every other level |
| 199 | [Binary Tree Right Side View](https://leetcode.com/problems/binary-tree-right-side-view/) ★ | Last node of each level |
| 116 | [Populating Next Right Pointers in Each Node](https://leetcode.com/problems/populating-next-right-pointers-in-each-node/) | Link nodes level by level |
| 662 | [Maximum Width of Binary Tree](https://leetcode.com/problems/maximum-width-of-binary-tree/) ★ | Index children 2i and 2i+1 |
| 863 | [All Nodes Distance K in Binary Tree](https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/) ★ | Add parent links, then BFS as a graph |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 987 | [Vertical Order Traversal of a Binary Tree](https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/) | Sort by (col, row, value) |

---

## Pattern 14: Binary Search Tree

**Idea:** in a **BST** (Binary Search Tree), everything in the left subtree is smaller than the node, and everything in the right subtree is bigger. So:
- You can search like binary search: go left or right, never both.
- **Inorder traversal visits the values in sorted order.**

**Spot it when:** the input is a BST. "Validate", "kth smallest", "closest value", "successor".

```python
def is_valid_bst(node, low=float("-inf"), high=float("inf")):
    # An empty tree is valid
    if node is None:
        return True
    # This node must be strictly between the bounds from its ancestors
    if not (low < node.val < high):
        return False
    # Left side: everything must be smaller than this node
    # Right side: everything must be bigger than this node
    return is_valid_bst(node.left, low, node.val) and is_valid_bst(node.right, node.val, high)


def kth_smallest(root, k):
    # Inorder walk without recursion, using a stack
    stack = []
    node = root
    while node is not None or len(stack) > 0:
        # Go as far left as possible
        while node is not None:
            stack.append(node)
            node = node.left
        # Smallest unvisited value
        node = stack.pop()
        k = k - 1
        if k == 0:
            return node.val
        # Then visit the right subtree
        node = node.right
    return None


#      5
#     / \
#    3   6
#   / \
#  2   4
bst = TreeNode(5, TreeNode(3, TreeNode(2), TreeNode(4)), TreeNode(6))
print(is_valid_bst(bst))  # True
print(kth_smallest(bst, 3))  # 4
```

**The trap:** checking only `node.left.val < node.val`. A value deep in the left subtree can still be bigger than the root. Pass **bounds** down.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 700 | [Search in a Binary Search Tree](https://leetcode.com/problems/search-in-a-binary-search-tree/) | Go left or right, never both |
| 530 | [Minimum Absolute Difference in BST](https://leetcode.com/problems/minimum-absolute-difference-in-bst/) | Inorder is sorted; compare neighbours |
| 108 | [Convert Sorted Array to Binary Search Tree](https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/) | Middle is the root |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 235 | [Lowest Common Ancestor of a Binary Search Tree](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/) ★ | Split point of p and q |
| 98 | [Validate Binary Search Tree](https://leetcode.com/problems/validate-binary-search-tree/) ★ | Pass (low, high) bounds down |
| 230 | [Kth Smallest Element in a BST](https://leetcode.com/problems/kth-smallest-element-in-a-bst/) ★ | Inorder, stop at k |
| 450 | [Delete Node in a BST](https://leetcode.com/problems/delete-node-in-a-bst/) | Two children: replace with inorder successor |
| 173 | [Binary Search Tree Iterator](https://leetcode.com/problems/binary-search-tree-iterator/) | Stack of left spine |
| 701 | [Insert into a Binary Search Tree](https://leetcode.com/problems/insert-into-a-binary-search-tree/) | Walk down to a None spot |
| 99 | [Recover Binary Search Tree](https://leetcode.com/problems/recover-binary-search-tree/) | Inorder; find the two out-of-order nodes |

---

## Pattern 15: Heap / Top-K

**Idea:** a **heap** always gives you the smallest item in O(log n), without sorting everything. For "top K", keep a heap of only K items: O(n log k) instead of O(n log n).

Analogy: a club with K chairs. A new person sits down only if they beat the weakest person sitting, who then leaves.

**Spot it when:** "k largest / smallest / most frequent / closest", "median of a stream", "merge K sorted lists", "always pick the cheapest next option".

| Want | Use |
|---|---|
| K **largest** | **min**-heap of size K (the smallest of the top K sits on top, ready to be kicked out) |
| K **smallest** | **max**-heap of size K (push negatives) |
| Running median | max-heap for the lower half + min-heap for the upper half |

```python
import heapq


def k_largest(nums, k):
    heap = []
    for x in nums:
        heapq.heappush(heap, x)
        # Too many: drop the smallest
        if len(heap) > k:
            heapq.heappop(heap)
    # heap[0] is the kth largest
    return heap[0]


def k_closest(points, k):
    heap = []
    for x, y in points:
        dist = x * x + y * y
        # Max-heap by distance: push the negative
        heapq.heappush(heap, (-dist, x, y))
        if len(heap) > k:
            # Removes the FARTHEST point
            heapq.heappop(heap)
    result = []
    for neg_dist, x, y in heap:
        result.append([x, y])
    return result


print(k_largest([3, 2, 1, 5, 6, 4], 2))  # 5
print(sorted(k_closest([[1, 3], [-2, 2], [5, 8], [0, 1]], 2)))  # [[-2, 2], [0, 1]]
```

**The trap:** pushing objects that can't be compared (like `ListNode`). If two priorities tie, Python compares the next item in the tuple and crashes. Add a counter: `(value, i, node)`.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 703 | [Kth Largest Element in a Stream](https://leetcode.com/problems/kth-largest-element-in-a-stream/) ★ | Min-heap of size k |
| 1046 | [Last Stone Weight](https://leetcode.com/problems/last-stone-weight/) ★ | Max-heap via negatives |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 215 | [Kth Largest Element in an Array](https://leetcode.com/problems/kth-largest-element-in-an-array/) ★ | Min-heap of size k (or quickselect) |
| 347 | [Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/) ★ | Counter + heap of size k |
| 973 | [K Closest Points to Origin](https://leetcode.com/problems/k-closest-points-to-origin/) ★ | Max-heap of size k on distance |
| 621 | [Task Scheduler](https://leetcode.com/problems/task-scheduler/) ★ | Most frequent task decides the idle slots |
| 355 | [Design Twitter](https://leetcode.com/problems/design-twitter/) | Merge k feeds with a heap |
| 767 | [Reorganize String](https://leetcode.com/problems/reorganize-string/) ★ | Always place the most frequent unlike the last |
| 378 | [Kth Smallest Element in a Sorted Matrix](https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/) | Heap of row heads (or binary search) |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 295 | [Find Median from Data Stream](https://leetcode.com/problems/find-median-from-data-stream/) ★ | Max-heap low half, min-heap high half |
| 23 | [Merge k Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/) ★ | Heap of (value, i, node) |
| 502 | [IPO](https://leetcode.com/problems/ipo/) | Heap of profits unlocked by capital |
| 480 | [Sliding Window Median](https://leetcode.com/problems/sliding-window-median/) | Two heaps + lazy delete |

---

## Pattern 16: Trie

**Idea:** a **trie** (say "try", from re*trie*val) is a tree where each edge is a letter. Words that share a beginning share a path. Checking a prefix costs O(length of word), no matter how many words are stored.

Analogy: a phone's autocomplete. Type "ca" and you are already standing on the node that leads to "cat", "car", "cake".

**Spot it when:** "prefix", "starts with", "autocomplete", "search many words in a grid", "max XOR of two numbers" (a trie of bits).

```python
class TrieNode:
    def __init__(self):
        # letter -> next TrieNode
        self.children = {}
        # True if a word ends exactly here
        self.is_word = False


class Trie:
    def __init__(self):
        self.root = TrieNode()

    def insert(self, word):
        node = self.root
        for ch in word:
            # Create the path if it doesn't exist yet
            if ch not in node.children:
                node.children[ch] = TrieNode()
            node = node.children[ch]
        node.is_word = True

    def _walk(self, text):
        # Follow the letters; None if the path breaks
        node = self.root
        for ch in text:
            if ch not in node.children:
                return None
            node = node.children[ch]
        return node

    def search(self, word):
        node = self._walk(word)
        return node is not None and node.is_word

    def starts_with(self, prefix):
        return self._walk(prefix) is not None


trie = Trie()
trie.insert("apple")
print(trie.search("app"))  # False
print(trie.starts_with("app"))  # True
```

**The trap:** `search("app")` must be `False` if only "apple" was inserted. That's what `is_word` is for.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 14 | [Longest Common Prefix](https://leetcode.com/problems/longest-common-prefix/) | Warm-up: compare char by char |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 208 | [Implement Trie (Prefix Tree)](https://leetcode.com/problems/implement-trie-prefix-tree/) ★ | Node: children dict + end flag |
| 211 | [Design Add and Search Words Data Structure](https://leetcode.com/problems/design-add-and-search-words-data-structure/) ★ | DFS on '.' |
| 1268 | [Search Suggestions System](https://leetcode.com/problems/search-suggestions-system/) | Trie or sort + bisect |
| 648 | [Replace Words](https://leetcode.com/problems/replace-words/) | Shortest root found in the trie |
| 421 | [Maximum XOR of Two Numbers in an Array](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/) | Bit trie; take the opposite bit |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 212 | [Word Search II](https://leetcode.com/problems/word-search-ii/) ★ | Trie of words + grid DFS; prune found words |
| 336 | [Palindrome Pairs](https://leetcode.com/problems/palindrome-pairs/) | Reverse words; check palindromic leftovers |
| 745 | [Prefix and Suffix Search](https://leetcode.com/problems/prefix-and-suffix-search/) | Insert 'suffix#word' keys |
