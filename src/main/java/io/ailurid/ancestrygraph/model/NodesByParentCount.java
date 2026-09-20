package io.ailurid.ancestrygraph.model;

import java.util.Map;
import java.util.Set;

public class NodesByParentCount {
        public final Map<Integer, Set<Node>> nodesByParentCount;

        public NodesByParentCount(
                        Map<Integer, Set<Node>> nodesByParentCount) {
                this.nodesByParentCount = nodesByParentCount;
        }
}
