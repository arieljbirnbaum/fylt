package io.ailurid.ancestrygraph.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.ailurid.ancestrygraph.model.AncestryGraph;
import io.ailurid.ancestrygraph.model.Node;

/**
 * ParentCounter
 * 
 * NOTE: This implementation is only for the task. In production, burn this with
 * fire and use `Collectors.groupingBy()` or `SELECT GROUP BY` instead :)
 */
public class ParentCounter {
    /**
     * Given an ancestry graph, returns a map from all actual parent counts to the
     * (possibly empty) set of node IDs with exactly that parent count.
     * Linear in the number of nodes.
     * 
     * @param graph Ancestry graph
     * @return
     */
    public static Map<Integer, Set<Node>> groupByParentCount(AncestryGraph graph) {
        Map<Node, Integer> parentCounts = new HashMap<>();
        for (var node : graph.getNodes()) {
            parentCounts.put(node, graph.getParents(node).size());
        }

        // Groups per count = preimage fibers.
        // The type of `preimage` prevents mixing up keys and values.
        Map<Integer, Set<Node>> groupedByParentCount = preimage(parentCounts);

        // For uniformity, initialize empty sets for unrepresented parent counts.
        for (int count = 0; count <= AncestryGraph.MAX_PARENT_COUNT; count++) {
            groupedByParentCount.computeIfAbsent(count, c -> new HashSet<>());
        }

        return groupedByParentCount;
    }

    /**
     * Given a map from keys to values, returns the inverse image map from values to
     * their corresponding sets of keys.
     * Linear in the number of keys.
     * 
     * @param <K> Type of keys
     * @param <V> Type of values
     * @param map Map from keys to values
     * @return Inverse image map from values to sets of keys
     */
    private static <K, V> Map<V, Set<K>> preimage(Map<K, V> map) {
        Map<V, Set<K>> fibers = new HashMap<>();

        // Initialize fiber sets to avoid having to check for each insertion.
        for (V count : map.values()) {
            fibers.put(count, new HashSet<>());
        }

        // Add each domain element to its corresponding fiber.
        for (Map.Entry<K, V> entry : map.entrySet()) {
            fibers.get(entry.getValue()).add(entry.getKey());
        }

        return fibers;
    }
}
