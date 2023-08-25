package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.models.GraphV2;
import it.unifi.swe4es.sv.cptask.models.NodeV2;
import it.unifi.swe4es.sv.cptask.repositories.NodeV2Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NodeV2Service {

    private final NodeV2Repository nodeV2Repository;

    public NodeV2Service(NodeV2Repository nodeV2Repository) {
        this.nodeV2Repository = nodeV2Repository;
    }

    public NodeV2 insertNewNode(NodeDTO n, GraphV2 savedGraph) {

        Optional<NodeV2> savedNode = nodeV2Repository
                .findNodeByLabelIgnoreCaseAndWeightAndTypeAndGraph(n.getLabel(), n.getWeight(), n.getType(), savedGraph);

        if (savedNode.isPresent()) {
            return savedNode.get();
        } else {
            NodeV2 node = new NodeV2();
            node.setGraph(savedGraph);
            node.setLabel(n.getLabel());
            node.setType(n.getType());
            node.setWeight(n.getWeight());

            return nodeV2Repository.save(node);
        }
    }
}
