package ancestrygraph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AncestryGraph {
    private Set<Node> nodes;
    private Map<Node, Set<Node>> parentSets;
    private Map<Node, Set<Node>> childSets;

    /**
     * Constructs an ancestry graph based on a list of edges.
     * Linear in the number of nodes and edges.
     * 
     * @param edges List of [parent,child] edges, meeting the requirements described
     *              in the problem spec. **No validation is performed.**
     */
    public AncestryGraph(int[][] edges) {
        this.nodes = new HashSet<>();
        this.parentSets = new HashMap<>();
        this.childSets = new HashMap<>();
        for (int[] edge : edges) {
            Node parent = new Node(edge[0]);
            this.nodes.add(parent);
            Node child = new Node(edge[1]);
            this.nodes.add(child);
            this.parentSets.computeIfAbsent(child, k -> new HashSet<>()).add(parent);
            this.parentSets.computeIfAbsent(parent, k -> new HashSet<>());
            this.childSets.computeIfAbsent(parent, k -> new HashSet<>()).add(child);
            this.childSets.computeIfAbsent(child, k -> new HashSet<>());
        }
    }

    public Set<Node> getNodes() {
        return Collections.unmodifiableSet(this.nodes);
    }

    public Set<Node> getParents(Node node) {
        return Collections.unmodifiableSet(this.parentSets.get(node));
    }

    public Set<Node> getChildren(Node node) {
        return Collections.unmodifiableSet(this.childSets.get(node));
    }

    public static final int MAX_PARENT_COUNT = 2;
}
