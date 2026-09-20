package io.ailurid.ancestrygraph.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.ailurid.ancestrygraph.model.AncestryGraph;
import io.ailurid.ancestrygraph.model.Node;

public class CommonAncestorFinder {
    private Set<CommonAncestorFinder.NodePair> nodePairs;

    private static record NodePair(Node a, Node b) {
    };

    /**
     * Builder
     * 
     * Helper class for initialization; implements the following logic:
     * 
     * <pre>
     * - For each node:
     *   - it and itself share a common ancestor
     *   - it and its children share a common ancestor
     *   - for all pairs `a`, `b` of *distinct* children of the node, every
     *     descendant of `a` shares a common ancestor with every descendant of `b`.
     * </pre>
     */
    private static class Builder {
        private final AncestryGraph graph;
        private final Map<Node, Set<Node>> descendantSets;
        private final Set<NodePair> nodePairs;

        public Builder(AncestryGraph graph) {
            this.graph = graph;
            this.descendantSets = new HashMap<>();
            this.nodePairs = new HashSet<>();
            this.populate();
        }

        private void populate() {
            for (Node node : this.graph.getNodes()) {
                this.populateDescendantSets(node);
            }
            this.populateNodePairs();
        }

        /**
         * Pupulates the descendant set for the given node.
         * 
         * The descendant sets for each node are computed with a dynamic algorithm to
         * ensure each set is computed once; runtime for each node is linear in its
         * descendants, total for the graph is quadratic in the number of nodes.
         * 
         * @param node
         */
        private void populateDescendantSets(Node node) {
            if (this.descendantSets.get(node) != null) {
                return;
            }
            Set<Node> descendantSet = new HashSet<>();
            descendantSet.add(node);
            for (Node child : this.graph.getChildren(node)) {
                this.populateDescendantSets(child);
                descendantSet.addAll(descendantSets.get(child));
            }
            descendantSets.put(node, descendantSet);
        }

        /**
         * Assuming descendant sets are populated, populate the pairs of nodes with
         * common ancestors.
         * 
         * Given the input constraints, two nodes will have *at most one* least common
         * ancestor; if two existed that would imply an *undirected* cycle in the graph.
         * Also, since the graph is incest-free, the descendant sets for any two sibling
         * nodes must be *disjoint*.
         * Taken together, this implies that any pair of nodes is considered *at most
         * once* by the algorithm above (specifically when processing its least common
         * ancestor). Hence the runtime is quadratic in the number of nodes.
         */
        private void populateNodePairs() {
            for (Node node : this.graph.getNodes()) {
                nodePairs.add(new NodePair(node, node));
                Node[] children = this.graph.getChildren(node).toArray(new Node[0]);
                for (int i = 0; i < children.length; i++) {
                    for (Node di : descendantSets.get(children[i])) {
                        nodePairs.add(new NodePair(node, di));
                        nodePairs.add(new NodePair(di, node));
                        for (int j = i + 1; j < children.length; j++) {
                            for (Node dj : descendantSets.get(children[j])) {
                                nodePairs.add(new NodePair(di, dj));
                                nodePairs.add(new NodePair(dj, di));
                            }
                        }
                    }
                }
            }
        }

        /**
         * @return The pairs of nodes with common ancestors.
         */
        public Set<NodePair> getNodePairs() {
            return this.nodePairs;
        }
    }

    public CommonAncestorFinder(Set<CommonAncestorFinder.NodePair> nodePairs) {
        this.nodePairs = nodePairs;
    }

    /**
     * Calculates the set of pairs of nodes that share a common ancestor.
     * Runtime is quadratic in the number of nodes.
     * 
     * @param graph An ancestry graph
     * @return The set of pairs of nodes that share a common ancestor
     */
    public static CommonAncestorFinder fromAncestryGraph(AncestryGraph graph) {
        Builder builder = new Builder(graph);
        return new CommonAncestorFinder(builder.getNodePairs());
    }

    /**
     * True if the given nodes share a common ancestor.
     * Runs in constant time.
     * 
     * @param a First node
     * @param b Second node
     * @return `true` iff a common ancestor exists
     */
    public boolean shareCommonAncestor(Node a, Node b) {
        return this.nodePairs.contains(new NodePair(a, b));
    }
}
