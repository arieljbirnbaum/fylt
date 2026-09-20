package io.ailurid.ancestrygraph.repository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

import io.ailurid.ancestrygraph.model.AncestryGraph;
import io.ailurid.ancestrygraph.model.AncestryGraphRecord;
import io.ailurid.ancestrygraph.service.CommonAncestorFinder;

/**
 * AncestryGraphRecordRepository
 * 
 * Stores ancestry graphs along with their precomputed data for user queries.
 */
@Repository
public class AncestryGraphRecordRepository {
    private ConcurrentMap<UUID, AncestryGraphRecord> graphs;

    public AncestryGraphRecordRepository() {
        this.graphs = new ConcurrentHashMap<>();
    }

    public UUID insert(AncestryGraph graph, CommonAncestorFinder commonAncestorFinder) {
        UUID id = UUID.randomUUID();
        AncestryGraphRecord record = new AncestryGraphRecord(graph, commonAncestorFinder);
        this.graphs.put(id, record);
        return id;
    }

    /**
     * Finds a graph record by its id
     * 
     * @param id UUID
     * @return Graph record with the given id if present; `empty` if not found
     */
    public Optional<AncestryGraphRecord> findById(UUID id) {
        return Optional.ofNullable(this.graphs.get(id));
    }
}
