package io.ailurid.ancestrygraph.rest;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.ailurid.ancestrygraph.model.AncestryGraph;
import io.ailurid.ancestrygraph.model.BooleanQueryResult;
import io.ailurid.ancestrygraph.model.Node;
import io.ailurid.ancestrygraph.model.NodesByParentCount;
import io.ailurid.ancestrygraph.model.RawEdgeList;
import io.ailurid.ancestrygraph.service.AncestryGraphService;
import io.ailurid.ancestrygraph.service.ParentCounter;
import jakarta.validation.constraints.AssertTrue;

@RestController
@RequestMapping("/api/ancestry-graphs")
@Validated
public class AncestryGraphController {
    private final AncestryGraphService service;

    public AncestryGraphController(AncestryGraphService service) {
        this.service = service;
    }

    @PostMapping
    public AncestryGraphModel create(@RequestBody RawEdgeList request) {
        var graph = new AncestryGraph(request);
        var id = service.create(graph);
        var model = new AncestryGraphModel(id, graph);
        return model;
    }

    @GetMapping("/{id}")
    public AncestryGraphModel get(@PathVariable UUID id) {
        var record = service.get(id).orElseThrow(() -> new AncestryGraphException.NotFound(id));
        return new AncestryGraphModel(id, record.graph());
    }

    @GetMapping("/{id}/nodes")
    public NodesByParentCount findNodes(
            @PathVariable UUID id,
            @RequestParam @AssertTrue boolean groupByParentCount) {

        var record = service.get(id).orElseThrow(() -> new AncestryGraphException.NotFound(id));
        return new NodesByParentCount(ParentCounter.groupByParentCount(record.graph()));

    }

    @GetMapping("/{id}/nodes/share-common-ancestor")
    public BooleanQueryResult shareCommonAncestor(
            @PathVariable UUID id,
            @RequestParam Integer nodeA,
            @RequestParam Integer nodeB) {
        var record = service.get(id).orElseThrow(() -> new AncestryGraphException.NotFound(id));
        return new BooleanQueryResult(
                record.commonAncestorFinder().shareCommonAncestor(new Node(nodeA), new Node(nodeB)));
    }

    @ExceptionHandler(AncestryGraphException.NotFound.class)
    public ResponseEntity<AncestryGraphException> handleNotFound(AncestryGraphException.NotFound notFound) {
        return ResponseEntity.notFound().build();
    }
}
