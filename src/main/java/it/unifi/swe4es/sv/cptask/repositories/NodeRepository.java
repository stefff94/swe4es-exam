package it.unifi.swe4es.sv.cptask.repositories;

import it.unifi.swe4es.sv.cptask.models.Node;
import it.unifi.swe4es.sv.cptask.models.NodeType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@Deprecated
public interface NodeRepository extends CrudRepository<Node, Long> {

    Node findByLabelIgnoreCaseAndWeightAndType(String label, Integer weight, NodeType type);
    Optional<Node> findNodeByLabelIgnoreCaseAndWeightAndType(String label, Integer weight, NodeType type);
}
