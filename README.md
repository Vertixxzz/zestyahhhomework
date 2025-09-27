Report — Divide n Conquer Algorithms (Javahateit)

Scope: MergeSort, QuickSort, Deterministic Select (Median‑of‑Medians), Closest Pair of Points (2D).
Implementation is iterative where possible, avoids unnecessary allocations, and keeps recursion depth under control.

Architecture notes (how depth/allocations are controlled)
MergeSort: top‑down split into halves; a single reusable int[] buffer of size n is allocated once and reused for all merges. 
Merge copies the active slice to the buffer with System.arraycopy and merges back into the original array.
Recursion depth is about ceil(log2 n). Stable due to the <= merge condition. No small‑n cutoffs.

QuickSort: randomized pivot with 3‑way partition (<, =, >).
After partitioning, the algorithm recurses only into the smaller side and processes the larger side via a loop, which bounds the call stack to O(log n) with high probability.
In‑place partitioning keeps extra space at O(1). 
Randomization defends against presorted/adversarial inputs; the 3‑way scheme handles duplicates efficiently.
Select (Median‑of‑Medians): groups of five, sort each group in place, move medians to the front, recursively select the median of those medians as a pivot.
Partition is 3‑way and only the side containing k is kept; the outer selection is a loop (tail recursion eliminated).
Extra space is O(1). Depth is O(log n) in the worst case coming from the pivot‑finding recursion.

Closest Pair (2D): points are cloned and presorted by x and by y once. 
The divide step splits by the median x; the conquer step returns the best of the two halves; the merge builds a y‑ordered strip within delta of the split line and checks each point against up to seven successors.
Uses temporary arrays to split py into left/right (and a HashSet for membership), so extra space is O(n). 
Recursion depth is about log2 n.

Recurrence analysis (method and o‑result)
MergeSort: T(n) = 2T(n/2) + o(n). Master Theorem Case 2 with a = 2, b = 2, f(n) = o(n) = o(n^{log_b a}). Result: o(n log n).
The linear merge term dominates the non‑leaf work; the balanced recursion gives the log n factor.

QuickSort: expected recurrence with random pivot and 3‑way partition is E[T(n)] = E[T(U)] + E[T(n − U − M)] + o(n), where U is the number less than the pivot and M the size of the equals block.
With uniform pivot ranks, the solution is o(n log n) in expectation. 
Worst case (consistently extreme pivots) gives T(n) = T(n−1) + o(n) = o(n^2).
Tail‑recursing on the smaller side does not change time complexity but reduces stack to O(log n) with high probability.

Select (Median‑of‑Medians): at least 3 elements in each 5‑group are >= the group median and >= half of those medians are ≥ the pivot, so at least 3⌈n/5⌉/2 elements are ≥ pivot (and symmetrically ≤ pivot).
A standard bound is T(n) ≤ T(⌈n/5⌉) + T(7n/10 + O(1)) + o(n).
By substitution or Akra-Bazzi intuition (total subproblem weight strictly less than n, linear toll), T(n) = o(n).

Closest Pair (2D): after an initial o(n log n) presort, the divide‑and‑conquer recurrence is T(n) = 2T(n/2) + o(n), because the strip scan is linear when the points are y‑sorted and at most seven successors per point are examined. 
Master Theorem Case 2 gives o(n log n).

Plots (what to measure and what to expect)
Time vs n: measure total time over multiple trials per n to reduce noise.
MergeSort and ClosestPair should grow close to n log n.

QuickSort should also appear as n log n on average;
its curve may sit below MergeSort for random int arrays due to in‑place partitioning and lower write traffic, but MergeSort can win on very large arrays because of sequential memory access in merge and predictable branches.
Select should grow roughly linearly; for small n it may look suboptimal compared to QuickSort due to larger constants in pivot selection.

Depth vs n: MergeSort and ClosestPair should grow like about log2 n.
QuickSorts observed maximum depth should be around 2*floor(log2 n) + O(1) under randomization and 3‑way partition, but can spike toward n in contrived worst cases.
Select’s depth should grow slowly (= log n) from the median‑of‑medians subcalls; the outer loop keeps it bounded.
Constant‑factor effects: cache locality favors MergeSort’s linear merges and can favor Select’s linear scans; branch mispredictions and irregular accesses can slow QuickSort on certain data. 
GC overhead should be minimal here: MergeSort allocates one O(n) buffer once, QuickSort and Select are in‑place; ClosestPair keeps O(n) arrays for presort and temporary splitting.
For large inputs, ensure the JVM is warmed up and consider using -Xms/-Xmx to reduce GC variance.

Summary (theory vs measurements)
In typical measurements, MergeSort and ClosestPair track o(n log n) closely across sizes; 
QuickSort matches o(n log n) on average and is usually the fastest sorter on random primitive arrays thanks to in‑place operation and good constant factors, though MergeSort may catch up on very large n due to better sequential access. 
Select (MoM) achieves linear growth but is often slower than QuickSort‑based selection for small to medium n because of larger constants;
its advantage is worst‑case guarantees and robustness to adversarial inputs.
Any mismatch usually comes from pivot quality, data distributions with many duplicates (which 3‑way partition handles well), memory hierarchy effects (cache/TLB), and JVM warm‑up/GC behavior.
With sufficient trials and careful benchmarking, the empirical curves align with the o‑results above.
