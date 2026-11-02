// Pattern 7: Fast & Slow Pointers (Floyd's tortoise and hare)
// slow moves 1 step, fast moves 2 steps.
// If there is a loop, fast catches slow. If not, fast reaches the end.
// When fast reaches the end, slow is in the middle.
// Run: java P7FastSlow.java

public class P7FastSlow {

    // a linked list node: a value plus a link to the next node
    static class ListNode {
        // the number stored in this node
        int val;
        // the next node in the list (null at the end)
        ListNode next;

        // constructor: make a node holding val
        ListNode(int val) {
            this.val = val;
        }
    }

    // helper: build a linked list from numbers, e.g. 1 -> 2 -> 3
    static ListNode build(int[] values) {
        // dummy node in front makes the loop simpler
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        for (int value : values) {
            tail.next = new ListNode(value);
            tail = tail.next;
        }
        return dummy.next;
    }

    // Template A: does the list have a cycle? (LeetCode 141). Time O(n), space O(1).
    static boolean hasCycle(ListNode head) {
        // both start at the head
        ListNode slow = head;
        ListNode fast = head;
        // fast needs two more nodes to take a double step
        while (fast != null && fast.next != null) {
            // one step
            slow = slow.next;
            // two steps
            fast = fast.next.next;
            // they met: there must be a loop
            if (slow == fast) {
                return true;
            }
        }
        // fast fell off the end: no loop
        return false;
    }

    // Template B: where does the cycle START? (LeetCode 142). Time O(n), space O(1).
    // Math fact: after they meet, a pointer from head and a pointer from the meeting
    // point, both moving 1 step, meet exactly at the cycle start.
    static ListNode cycleStart(ListNode head) {
        // both start at the head
        ListNode slow = head;
        ListNode fast = head;
        // phase 1: find a meeting point inside the loop
        while (fast != null && fast.next != null) {
            // slow: one step
            slow = slow.next;
            // fast: two steps
            fast = fast.next.next;
            // they met inside the loop
            if (slow == fast) {
                // phase 2: one pointer back to head, both move 1 step
                ListNode finder = head;
                while (finder != slow) {
                    // both move one step at the same speed
                    finder = finder.next;
                    slow = slow.next;
                }
                // where they meet is the start of the loop
                return finder;
            }
        }
        // no loop
        return null;
    }

    // Template C: middle node (LeetCode 876). For an even length, returns the SECOND middle.
    // Time O(n), space O(1).
    static ListNode middle(ListNode head) {
        // both start at the head
        ListNode slow = head;
        ListNode fast = head;
        // when fast reaches the end, slow has walked half the distance
        while (fast != null && fast.next != null) {
            // slow: one step
            slow = slow.next;
            // fast: two steps
            fast = fast.next.next;
        }
        // slow is in the middle
        return slow;
    }

    // Template D: remove the n-th node from the END (LeetCode 19). Same speed, fixed GAP.
    // Time O(length), space O(1).
    static ListNode removeNthFromEnd(ListNode head, int n) {
        // dummy in front handles "remove the head" with no special case
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        // both start at the dummy
        ListNode ahead = dummy;
        ListNode behind = dummy;
        // move ahead n + 1 steps, so the gap between them is n + 1
        for (int step = 0; step <= n; step++) {
            // one step forward
            ahead = ahead.next;
        }
        // move both until ahead falls off the end
        while (ahead != null) {
            // both move one step, the gap stays the same
            ahead = ahead.next;
            behind = behind.next;
        }
        // behind is now just before the node to remove: skip over it
        behind.next = behind.next.next;
        // dummy.next is the (maybe new) head
        return dummy.next;
    }

    // Template E: find the duplicate in an ARRAY of n+1 numbers in range 1..n (LeetCode 287).
    // Treat value as "next pointer": index -> nums[index]. The duplicate is the cycle start.
    // Time O(n), space O(1), array not changed.
    static int findDuplicate(int[] nums) {
        // phase 1: index 0 is the "head"; slow takes 1 hop, fast takes 2 hops
        int slow = nums[0];
        int fast = nums[nums[0]];
        // keep hopping until they land on the same index
        while (slow != fast) {
            // slow: one hop
            slow = nums[slow];
            // fast: two hops
            fast = nums[nums[fast]];
        }
        // phase 2: one pointer back to the start, both hop once per step
        int finder = 0;
        // they meet at the cycle start = the duplicate value
        while (finder != slow) {
            finder = nums[finder];
            slow = nums[slow];
        }
        // the cycle start is the duplicate number
        return finder;
    }

    // helper: list to text, e.g. "1 -> 2 -> 3"
    static String show(ListNode head) {
        StringBuilder text = new StringBuilder();
        ListNode node = head;
        while (node != null) {
            text.append(node.val);
            if (node.next != null) {
                text.append(" -> ");
            }
            node = node.next;
        }
        return text.toString();
    }

    public static void main(String[] args) {
        // build 3 -> 2 -> 0 -> -4, then link -4 back to 2 to make a loop
        ListNode looped = build(new int[] {3, 2, 0, -4});
        looped.next.next.next.next = looped.next;

        // Template A
        System.out.println(hasCycle(looped));
        // prints: true
        System.out.println(hasCycle(build(new int[] {1, 2, 3})));
        // prints: false

        // Template B
        System.out.println(cycleStart(looped).val);
        // prints: 2

        // Template C
        System.out.println(middle(build(new int[] {1, 2, 3, 4, 5})).val);
        // prints: 3
        System.out.println(middle(build(new int[] {1, 2, 3, 4, 5, 6})).val);
        // prints: 4

        // Template D
        System.out.println(show(removeNthFromEnd(build(new int[] {1, 2, 3, 4, 5}), 2)));
        // prints: 1 -> 2 -> 3 -> 5

        // Template E
        System.out.println(findDuplicate(new int[] {1, 3, 4, 2, 2}));
        // prints: 2
    }
}
