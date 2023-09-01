package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.models.Graph;
import it.unifi.swe4es.sv.cptask.models.Node;
import it.unifi.swe4es.sv.cptask.repositories.NodeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NodeService {

    private final NodeRepository nodeRepository;

    public NodeService(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public Node insertNewNode(NodeDTO n, Graph savedGraph) {

        Optional<Node> savedNode = nodeRepository
                .findNodeByLabelIgnoreCaseAndWeightAndTypeAndGraph(n.getLabel(), n.getWeight(), n.getType(), savedGraph);

        if (savedNode.isPresent()) {
            return savedNode.get();
        } else {
            Node node = new Node();
            node.setGraph(savedGraph);
            node.setLabel(n.getLabel());
            node.setType(n.getType());
            node.setWeight(n.getWeight());

            return nodeRepository.save(node);
        }
    }
}
