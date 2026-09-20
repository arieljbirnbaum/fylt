package io.ailurid.ancestrygraph.model;

import io.ailurid.ancestrygraph.service.CommonAncestorFinder;

public record AncestryGraphRecord(
        AncestryGraph graph,
        CommonAncestorFinder commonAncestorFinder) {
}