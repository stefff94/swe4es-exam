package it.unifi.swe4es.sv.cptask.repositories;

import it.unifi.swe4es.sv.cptask.models.Graph;
import it.unifi.swe4es.sv.cptask.models.Node;
import it.unifi.swe4es.sv.cptask.models.NodeType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NodeRepository extends CrudRepository<Node, Long> {

        Node findByLabelIgnoreCaseAndWeightAndTypeAndGraph(String label, Integer weight, NodeType type, Graph graph);

        Optional<Node> findNodeByLabelIgnoreCaseAndWeightAndTypeAndGraph(String label, Integer weight, NodeType type, Graph graph);

}
