package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.models.Node;
import it.unifi.swe4es.sv.cptask.repositories.NodeRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NodeService {

    private final NodeRepository nodeRepository;

    @Autowired
    public NodeService(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public Node insertNewNode(Node node) {
        Optional<Node> savedNode = nodeRepository
                .findNodeByLabelIgnoreCaseAndWeightAndType(node.getLabel(), node.getWeight(), node.getType());

        if (savedNode.isPresent())
            return savedNode.get();
        else {
            node.setId(null);
            try {
                return nodeRepository.save(node);
            } catch(ConstraintViolationException | DataIntegrityViolationException e) {
                return nodeRepository
                        .findByLabelIgnoreCaseAndWeightAndType(node.getLabel(), node.getWeight(), node.getType());
            }
        }
    }
}
