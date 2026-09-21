# Java Spring Project

This test covers Spring basics, basic version control with git[^1] as well as some
algorithm solving skills.

## General

* If, at any given time, you have a question, write us an email[^2] or visit the helpdesk[^3].
* We expect you to use at least Java SE 25.
* Do not use LLM-based code generation (ChatGPT, Claude, Copilot Chat, Cursor, similar tools). Plain IDE autocomplete is fine. We do test your submission for AI-assisted code.
* All task work starts from the `master` branch. Create a branch for each task, e.g., `task/01-algorithm`. You are allowed to merge between task/feature branches (useful when a later task builds on an earlier one).
* When you have finished a task, or just want some intermediate feedback, open a merge request from your task branch back to `master`. Mark it as a *Draft* (prefix the title with `Draft:`) while still in progress, and assign it to your supervisor or to the GitLab user `@expertsieve`. Remove the `Draft:` prefix once you consider the task done.
* If you cannot finish a task or hit an issue you cannot resolve, explain it in the merge request description and/or in this `README`.
* If you really can't make it using git, please contact our support[^2].
* Please use a build system, e.g., Maven or Gradle, AND leave some documentation about how to build and run your solution. `build.sh` and `start.sh` scripts are welcome.
* Feel free to use additional libraries as long as they do not solve the core problem for you. This test is to assess your algorithm solving skills.

### Build Instructions

The project uses Maven; wrapper script `mvnw` (and `mvnw.cmd` for Windows shell) provided.
Run unit tests with `mvnw test`.

# Tasks

## Task 1 - The Algorithm

Suppose we have some input data describing relationships between nodes over
multiple generations. The input data is formatted as a list of
(parent, child) pairs, where each individual is assigned a unique integer
identifier.

For example, in this diagram, 3 is a child of 10 and 2, and 5 is a child of 4:

```
10  2   4
 \ /   / \
  3   5   8
   \ / \   \
    6   17   9
```

Sample graph as input data

```java
// Java sample code, no need to use it as-is
int[][] parentChildPairs = new int[][] {
    {10, 3}, {2, 3}, {3, 6}, {5, 6}, {5, 17},
    {4, 5}, {4, 8}, {8, 9}
};
```

Write a function that takes this data as input and returns two "collections":

* one containing all individuals with zero known parents, and
* one containing all individuals with exactly one known parent.

Sample output for the sample graph

```
Zero parents: 10, 2, 4
One parent: 5, 17, 8, 9
```

Test your solution.

### Clarifications

* Please do not implement your solution in the `main` function.
* Output order is irrelevant.
* The IDs are not guaranteed to be contiguous.
* The input is not necessarily a connected graph. There may be >3 generations.
* No node in the input set will have more than two parents, nor will there be duplicate entries.
* No node in the input is their own parent.
* The size of the graph is not limited. If a limit is needed, it shall be 1 million pairs.
* There are no cycles in the input.
* No node may appear twice via different ancestry paths from the same descendant. That is, individual A may not be descended from individual B through both of the separate individuals C and D.
* Solution methods returning a String will be always wrong.
* Think about complexities.

### Implementation Notes

* Using `HashSet` and `HashMap` for flexibility and ease of implementation. Avoiding `Collectors` and similar for compliance.
* Not explicitly stated, but the given input spec does not allow mentioning isolated nodes (ie. nodes that have no parent AND no child). If allowed, such nodes *would* be part of the zero-parent set.
* For this task alone, it suffices to only count the incidences for each node (it is guaranteed that there are no duplicate entries). We compute a richer representation for compatibility with the following tasks.
* End-to-end runtime is linear in the number of (nodes and) edges.

## Task 2 - Complex relationships

Based on Task 1, write a function that, for two given individuals in our dataset, returns `true` if and only if they share at least one known ancestor.


Example based on the sample graph of Task 1, two nodes as input:
```
[3, 8] => false
[5, 8] => true
[6, 8] => true
[6, 5] => true  # as they are in direct line aka one is ancestor of the other one
```

Test your solution.

### Implementation Notes

* Same structures as Task 1.
* Assuming that the same input graph will be queried repeatedly with multiple node pairs; opted for an upfront computation of all pairs sharing a common ancestor.
* Initialization is quadratic in the number of nodes. Subsequent queries run in constant time.

## Task 3 - REST with Spring

Please implement a **Spring Boot** and **Spring Framework based** REST service that provides Task 1 and Task 2 via an API. Thus, your solution shall accept an input graph and provide the result for Task 1 and Task 2 via API. For Task 2 it shall accept user input (for the node pair).
Test your solution.

### Clarifications

* The final version of your service must accept a graph as input and store it
(in memory is ok).
* The calculation results shall be available on request. Thus, on a second request.
* API documentation is mandatory (and shall be available without building the application)
* Think about the state and concurrent access

## Resources

[^1]: https://git-scm.com
[^2]: support@fyltura.de
[^3]: https://expertsieve.freshdesk.com/
