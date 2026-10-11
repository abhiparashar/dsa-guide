# Phase 4: Backtracking & Graphs

[← Phase 3](03-trees-and-heaps.md) · [Roadmap](README.md) · [Next: Phase 5 →](05-dp-and-greedy.md)

| # | Pattern | Key tool |
|---|---|---|
| 17 | [Backtracking](#pattern-17-backtracking) | choose → explore → un-choose |
| 18 | [Graph BFS / DFS](#pattern-18-graph-bfs--dfs) | adjacency list + visited set |
| 19 | [Union-Find](#pattern-19-union-find-disjoint-set) | "are these connected?" fast |
| 20 | [Topological Sort](#pattern-20-topological-sort) | order with prerequisites |
| 21 | [Shortest Path & MST](#pattern-21-shortest-path--minimum-spanning-tree) | Dijkstra, Bellman-Ford, Prim |

---

## Pattern 17: Backtracking

**Idea:** build an answer one choice at a time. After exploring a choice, **undo it** and try the next one. It's a DFS over a tree of decisions.

Analogy: walking a maze. At each fork you pick a path. At a dead end you walk back to the last fork and try the next path.

**Spot it when:** "all subsets", "all permutations", "all combinations", "all ways to…", N-Queens, Sudoku, word search. Input sizes are small (n ≤ 20).

**Complexity:** usually O(2ⁿ) for subsets, O(n!) for permutations. That's expected.

### Template: choose → explore → un-choose

```python
def subsets(nums):
    result = []
    path = []

    def backtrack(start):
        # Every path we reach is a valid subset: save a COPY
        result.append(path[:])
        for i in range(start, len(nums)):
            # Choose
            path.append(nums[i])
            # Explore: only look at later numbers (no reuse, no reorder)
            backtrack(i + 1)
            # Un-choose
            path.pop()

    backtrack(0)
    return result


def permutations(nums):
    result = []
    path = []
    used = [False] * len(nums)

    def backtrack():
        # A full-length path is one permutation
        if len(path) == len(nums):
            result.append(path[:])
            return
        for i in range(len(nums)):
            if used[i]:
                continue
            used[i] = True
            path.append(nums[i])
            backtrack()
            path.pop()
            used[i] = False

    backtrack()
    return result


print(subsets([1, 2, 3]))
# [[], [1], [1, 2], [1, 2, 3], [1, 3], [2], [2, 3], [3]]
print(permutations([1, 2, 3]))
# [[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]
```

| Question type | Loop starts at | Recurse with |
|---|---|---|
| Subsets / combinations (each used once) | `start` | `i + 1` |
| Combination Sum (reuse allowed) | `start` | `i` |
| Permutations (order matters) | `0` + `used[]` | — |
| Input has duplicates | sort first, then `if i > start and nums[i] == nums[i - 1]: continue` | |

**The trap:** `result.append(path)` saves a **reference**. Later `pop()` calls empty it. Always save a copy: `path[:]`.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 257 | [Binary Tree Paths](https://leetcode.com/problems/binary-tree-paths/) | Path list; record at leaves |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 78 | [Subsets](https://leetcode.com/problems/subsets/) ★ | Take it or skip it |
| 90 | [Subsets II](https://leetcode.com/problems/subsets-ii/) ★ | Sort; skip same value at the same level |
| 46 | [Permutations](https://leetcode.com/problems/permutations/) ★ | used[] array |
| 47 | [Permutations II](https://leetcode.com/problems/permutations-ii/) | Sort; skip if same as previous unused |
| 77 | [Combinations](https://leetcode.com/problems/combinations/) | Start index moves forward |
| 39 | [Combination Sum](https://leetcode.com/problems/combination-sum/) ★ | Reuse allowed: recurse with i, not i+1 |
| 40 | [Combination Sum II](https://leetcode.com/problems/combination-sum-ii/) ★ | Sort; skip duplicates; recurse with i+1 |
| 17 | [Letter Combinations of a Phone Number](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) ★ | One digit per level |
| 22 | [Generate Parentheses](https://leetcode.com/problems/generate-parentheses/) ★ | Add '(' if open < n; ')' if close < open |
| 79 | [Word Search](https://leetcode.com/problems/word-search/) ★ | Mark cell visited, undo after |
| 131 | [Palindrome Partitioning](https://leetcode.com/problems/palindrome-partitioning/) ★ | Cut only at palindromic prefixes |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 51 | [N-Queens](https://leetcode.com/problems/n-queens/) ★ | Sets for cols, r-c, r+c |
| 37 | [Sudoku Solver](https://leetcode.com/problems/sudoku-solver/) | Try 1-9 in empty cells with row/col/box sets |
| 282 | [Expression Add Operators](https://leetcode.com/problems/expression-add-operators/) | Track last operand for '*' |

---

## Pattern 18: Graph BFS / DFS

**Idea:** a **graph** is dots (nodes) joined by lines (edges). Store it as an **adjacency list**: `node -> list of neighbours`. Then:
- **DFS** explores one path deeply. Good for "is it connected?", "count groups".
- **BFS** explores in rings by distance. **BFS gives the shortest path when every edge costs the same.**

A grid is also a graph: each cell is a node, its 4 neighbours are edges.

**Spot it when:** islands, regions, "connected", "can you reach", "minimum steps / moves", word ladders, "spreads every minute" (multi-source BFS).

### Template A: grid DFS (count islands)

```python
def num_islands(grid):
    rows = len(grid)
    cols = len(grid[0])
    count = 0

    def sink(r, c):
        # Stop outside the grid or on water
        if r < 0 or r >= rows or c < 0 or c >= cols:
            return
        if grid[r][c] != "1":
            return
        # Mark as visited by turning land into water
        grid[r][c] = "0"
        # Visit the 4 neighbours
        sink(r + 1, c)
        sink(r - 1, c)
        sink(r, c + 1)
        sink(r, c - 1)

    for r in range(rows):
        for c in range(cols):
            # New land we haven't visited = a new island
            if grid[r][c] == "1":
                count = count + 1
                sink(r, c)
    return count


grid = [
    ["1", "1", "0", "0"],
    ["1", "0", "0", "1"],
    ["0", "0", "1", "1"],
]
print(num_islands(grid))  # 2
```

### Template B: BFS shortest path on a graph

```python
from collections import deque, defaultdict


def shortest_path(n, edges, start, goal):
    # Build the adjacency list (undirected: add both ways)
    graph = defaultdict(list)
    for a, b in edges:
        graph[a].append(b)
        graph[b].append(a)
    # distance from start to each visited node
    dist = {start: 0}
    queue = deque([start])
    while len(queue) > 0:
        node = queue.popleft()
        if node == goal:
            return dist[node]
        for nxt in graph[node]:
            # Mark visited when ADDING to the queue, not when removing
            if nxt not in dist:
                dist[nxt] = dist[node] + 1
                queue.append(nxt)
    # goal can't be reached
    return -1


print(shortest_path(5, [[0, 1], [1, 2], [0, 3], [3, 4], [4, 2]], 0, 2))  # 2
```

**The trap:** marking a node visited when you **pop** it instead of when you **push** it. The same node gets added many times and BFS becomes very slow.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 733 | [Flood Fill](https://leetcode.com/problems/flood-fill/) | DFS from one cell |
| 1971 | [Find if Path Exists in Graph](https://leetcode.com/problems/find-if-path-exists-in-graph/) | Adjacency list + BFS |
| 463 | [Island Perimeter](https://leetcode.com/problems/island-perimeter/) | Each land cell adds 4 minus shared sides |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 200 | [Number of Islands](https://leetcode.com/problems/number-of-islands/) ★ | DFS/BFS from every unvisited '1' |
| 695 | [Max Area of Island](https://leetcode.com/problems/max-area-of-island/) ★ | DFS returns size |
| 133 | [Clone Graph](https://leetcode.com/problems/clone-graph/) ★ | dict old -> new + DFS |
| 994 | [Rotting Oranges](https://leetcode.com/problems/rotting-oranges/) ★ | Multi-source BFS from all rotten |
| 417 | [Pacific Atlantic Water Flow](https://leetcode.com/problems/pacific-atlantic-water-flow/) ★ | Reverse: flow uphill from each ocean |
| 130 | [Surrounded Regions](https://leetcode.com/problems/surrounded-regions/) ★ | Save 'O's touching the border first |
| 1091 | [Shortest Path in Binary Matrix](https://leetcode.com/problems/shortest-path-in-binary-matrix/) | BFS with 8 directions |
| 286 | [Walls and Gates](https://leetcode.com/problems/walls-and-gates/) ★ (Premium) | Multi-source BFS from gates |
| 752 | [Open the Lock](https://leetcode.com/problems/open-the-lock/) | BFS over 4-digit states |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 127 | [Word Ladder](https://leetcode.com/problems/word-ladder/) ★ | BFS; wildcard patterns 'h*t' |
| 1293 | [Shortest Path in a Grid with Obstacles Elimination](https://leetcode.com/problems/shortest-path-in-a-grid-with-obstacles-elimination/) | BFS state = (r, c, removals left) |
| 815 | [Bus Routes](https://leetcode.com/problems/bus-routes/) | BFS over routes, not stops |

---

## Pattern 19: Union-Find (Disjoint Set)

**Idea:** keep groups of connected items. Each group has a leader (the **root**). Two operations:
- `find(x)`: who is x's leader?
- `union(a, b)`: merge a's group and b's group.

With two small tricks (**path compression** and **union by size**) both are almost O(1).

Analogy: companies merging. To check if two employees work for the same company, follow each one's "parent company" chain up to the top.

**Spot it when:** "number of connected groups", "are these connected", "redundant edge / cycle in an undirected graph", "merge accounts", edges arriving one at a time, Kruskal's MST.

```python
class UnionFind:
    def __init__(self, n):
        # Everyone starts as their own leader
        self.parent = list(range(n))
        # Size of each leader's group
        self.size = [1] * n
        # Number of separate groups
        self.groups = n

    def find(self, x):
        # Walk up to the leader
        root = x
        while self.parent[root] != root:
            root = self.parent[root]
        # Path compression: point everyone on the path straight at the leader
        while self.parent[x] != root:
            next_x = self.parent[x]
            self.parent[x] = root
            x = next_x
        return root

    def union(self, a, b):
        root_a = self.find(a)
        root_b = self.find(b)
        # Already in the same group: nothing to merge (this edge makes a cycle)
        if root_a == root_b:
            return False
        # Union by size: hang the smaller group under the bigger one
        if self.size[root_a] < self.size[root_b]:
            root_a, root_b = root_b, root_a
        self.parent[root_b] = root_a
        self.size[root_a] = self.size[root_a] + self.size[root_b]
        self.groups = self.groups - 1
        return True


uf = UnionFind(5)
uf.union(0, 1)
uf.union(3, 4)
print(uf.groups)  # 3
print(uf.find(0) == uf.find(1))  # True
print(uf.union(1, 0))  # False
```

**The trap:** comparing `parent[a] == parent[b]` instead of `find(a) == find(b)`. Parents are not always the leaders.

### Questions

_No good Easy questions for this pattern. Start with Medium._

**Medium**

| # | Problem | Hint |
|---|---|---|
| 547 | [Number of Provinces](https://leetcode.com/problems/number-of-provinces/) ★ | Count roots |
| 684 | [Redundant Connection](https://leetcode.com/problems/redundant-connection/) ★ | First edge joining two already-connected nodes |
| 323 | [Number of Connected Components in an Undirected Graph](https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/) ★ (Premium) | n - successful unions |
| 261 | [Graph Valid Tree](https://leetcode.com/problems/graph-valid-tree/) ★ (Premium) | n-1 edges and no cycle |
| 721 | [Accounts Merge](https://leetcode.com/problems/accounts-merge/) ★ | Union emails; group by root |
| 990 | [Satisfiability of Equality Equations](https://leetcode.com/problems/satisfiability-of-equality-equations/) | Union '==' first, then check '!=' |
| 947 | [Most Stones Removed with Same Row or Column](https://leetcode.com/problems/most-stones-removed-with-same-row-or-column/) | Answer = stones - components |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 305 | [Number of Islands II](https://leetcode.com/problems/number-of-islands-ii/) (Premium) | Add land one by one, union neighbours |
| 685 | [Redundant Connection II](https://leetcode.com/problems/redundant-connection-ii/) | Handle node with two parents + cycle |
| 1579 | [Remove Max Number of Edges to Keep Graph Fully Traversable](https://leetcode.com/problems/remove-max-number-of-edges-to-keep-graph-fully-traversable/) | Two DSUs; shared edges first |

---

## Pattern 20: Topological Sort

**Idea:** order tasks so every task comes after its prerequisites. **Kahn's algorithm:** count how many prerequisites each task still needs (its **indegree**). Start with tasks that need nothing. When you finish a task, reduce the count of the tasks that depend on it.

If you can't finish all tasks, there is a **cycle** (A needs B, B needs A).

Analogy: getting dressed. Socks before shoes, shirt before jacket. Start with anything that has no "before".

**Spot it when:** "prerequisites", "dependencies", "order of courses / builds", "is there a cycle in a **directed** graph", alien dictionary.

```python
from collections import deque, defaultdict


def course_order(num_courses, prerequisites):
    graph = defaultdict(list)
    indegree = [0] * num_courses
    for course, needed in prerequisites:
        # Edge: needed -> course
        graph[needed].append(course)
        indegree[course] = indegree[course] + 1
    # Start with courses that need nothing
    queue = deque()
    for c in range(num_courses):
        if indegree[c] == 0:
            queue.append(c)
    order = []
    while len(queue) > 0:
        c = queue.popleft()
        order.append(c)
        for nxt in graph[c]:
            # One fewer prerequisite left for nxt
            indegree[nxt] = indegree[nxt] - 1
            if indegree[nxt] == 0:
                queue.append(nxt)
    # Not everything was taken: there is a cycle
    if len(order) != num_courses:
        return []
    return order


print(course_order(4, [[1, 0], [2, 0], [3, 1], [3, 2]]))  # [0, 1, 2, 3]
print(course_order(2, [[0, 1], [1, 0]]))  # []
```

**The trap:** getting the edge direction backwards. `[course, needed]` means `needed → course`.

### Questions

**Easy**

| # | Problem | Hint |
|---|---|---|
| 997 | [Find the Town Judge](https://leetcode.com/problems/find-the-town-judge/) | indegree - outdegree == n - 1 |

**Medium**

| # | Problem | Hint |
|---|---|---|
| 207 | [Course Schedule](https://leetcode.com/problems/course-schedule/) ★ | Kahn's: can we take all n? |
| 210 | [Course Schedule II](https://leetcode.com/problems/course-schedule-ii/) ★ | Kahn's order |
| 310 | [Minimum Height Trees](https://leetcode.com/problems/minimum-height-trees/) | Peel leaves layer by layer |
| 802 | [Find Eventual Safe States](https://leetcode.com/problems/find-eventual-safe-states/) | Reverse graph + Kahn's |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 269 | [Alien Dictionary](https://leetcode.com/problems/alien-dictionary/) ★ (Premium) | Edge from first differing chars |
| 329 | [Longest Increasing Path in a Matrix](https://leetcode.com/problems/longest-increasing-path-in-a-matrix/) ★ | DFS + memo (DAG) |
| 2050 | [Parallel Courses III](https://leetcode.com/problems/parallel-courses-iii/) | Kahn's + longest finish time |
| 2392 | [Build a Matrix With Conditions](https://leetcode.com/problems/build-a-matrix-with-conditions/) | Two topo sorts: rows and cols |

---

## Pattern 21: Shortest Path & Minimum Spanning Tree

**Idea:** when edges have **different costs**, BFS is not enough.

| Algorithm | Use when | Time |
|---|---|---|
| BFS | all edges cost the same | O(V + E) |
| 0-1 BFS (deque) | edges cost 0 or 1 | O(V + E) |
| **Dijkstra** | costs ≥ 0 | O(E log V) |
| Bellman-Ford | negative costs, or "at most K edges" | O(V · E) |
| Floyd-Warshall | all pairs, n ≤ 400 | O(V³) |
| Prim / Kruskal (MST) | connect everything with minimum total cost | O(E log V) |

(V = number of nodes, E = number of edges.)

**Dijkstra idea:** always expand the closest unfinished node, using a min-heap. Like water spreading from a tap: it reaches near places first.

```python
import heapq
from collections import defaultdict


def dijkstra(n, edges, source):
    graph = defaultdict(list)
    for a, b, cost in edges:
        # Directed edge a -> b
        graph[a].append((b, cost))
    dist = [float("inf")] * n
    dist[source] = 0
    # Heap of (distance so far, node)
    heap = [(0, source)]
    while len(heap) > 0:
        d, node = heapq.heappop(heap)
        # Old, outdated entry: we already found a shorter way
        if d > dist[node]:
            continue
        for nxt, cost in graph[node]:
            new_d = d + cost
            # Found a shorter way to nxt
            if new_d < dist[nxt]:
                dist[nxt] = new_d
                heapq.heappush(heap, (new_d, nxt))
    return dist


edges = [[0, 1, 4], [0, 2, 1], [2, 1, 2], [1, 3, 1], [2, 3, 5]]
print(dijkstra(4, edges, 0))  # [0, 3, 1, 4]
```

**The trap:** Dijkstra gives wrong answers with **negative** edge costs. Use Bellman-Ford there.

### Questions

_No good Easy questions for this pattern. Start with Medium._

**Medium**

| # | Problem | Hint |
|---|---|---|
| 743 | [Network Delay Time](https://leetcode.com/problems/network-delay-time/) ★ | Plain Dijkstra; answer = max distance |
| 1631 | [Path With Minimum Effort](https://leetcode.com/problems/path-with-minimum-effort/) ★ | Dijkstra where cost = max step |
| 787 | [Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/) ★ | Bellman-Ford k+1 rounds |
| 1514 | [Path with Maximum Probability](https://leetcode.com/problems/path-with-maximum-probability/) | Dijkstra with max-heap |
| 1584 | [Min Cost to Connect All Points](https://leetcode.com/problems/min-cost-to-connect-all-points/) ★ | MST: Prim's with a heap |
| 1334 | [Find the City With the Smallest Number of Neighbors at a Threshold Distance](https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/) | Floyd-Warshall |

**Hard**

| # | Problem | Hint |
|---|---|---|
| 778 | [Swim in Rising Water](https://leetcode.com/problems/swim-in-rising-water/) ★ | Dijkstra on max height |
| 2290 | [Minimum Obstacle Removal to Reach Corner](https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/) | 0-1 BFS with deque |
