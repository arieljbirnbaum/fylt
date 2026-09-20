package io.ailurid.ancestrygraph.rest;

import java.util.UUID;

/**
 * GraphNotFoundException
 */
public abstract sealed class AncestryGraphException extends RuntimeException {
    protected AncestryGraphException(String message) {
        super(message);
    }

    public static final class NotFound extends AncestryGraphException {
        public NotFound(UUID id) {
            super("No graph found with id " + id);
        }
    }
}
