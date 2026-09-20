package io.ailurid.ancestrygraph.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.UUID;

import org.springframework.hateoas.EntityModel;

import io.ailurid.ancestrygraph.model.AncestryGraph;

public class AncestryGraphModel extends EntityModel<AncestryGraph> {
    public final UUID id;

    public AncestryGraphModel(UUID id, AncestryGraph graph) {
        super(graph);
        this.id = id;

        var controller = methodOn(AncestryGraphController.class);
        this.add(linkTo(controller.get(id)).withSelfRel());
        this.add(linkTo(controller.findNodes(id, true)).withRel("nodesByParentCount"));
        this.add(linkTo(controller.shareCommonAncestor(id, null, null)).withRel("shareCommonAncestor"));
    }
}
