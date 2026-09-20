package io.ailurid.ancestrygraph.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.ailurid.ancestrygraph.model.AncestryGraph;
import io.ailurid.ancestrygraph.model.AncestryGraphRecord;
import io.ailurid.ancestrygraph.repository.AncestryGraphRecordRepository;

@Service
public class AncestryGraphService {
    private final AncestryGraphRecordRepository repository;

    public AncestryGraphService(AncestryGraphRecordRepository repository) {
        this.repository = repository;
    }

    public UUID create(AncestryGraph graph) {
        CommonAncestorFinder commonAncestorFinder = CommonAncestorFinder.fromAncestryGraph(graph);
        return this.repository.insert(graph, commonAncestorFinder);
    }

    public Optional<AncestryGraphRecord> get(UUID id) {
        return this.repository.findById(id);
    }
}
