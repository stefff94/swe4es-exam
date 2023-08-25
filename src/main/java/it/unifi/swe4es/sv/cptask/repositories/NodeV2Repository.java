package it.unifi.swe4es.sv.cptask.repositories;

import it.unifi.swe4es.sv.cptask.models.GraphV2;
import it.unifi.swe4es.sv.cptask.models.NodeType;
import it.unifi.swe4es.sv.cptask.models.NodeV2;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NodeV2Repository extends CrudRepository<NodeV2, Long> {

        NodeV2 findByLabelIgnoreCaseAndWeightAndTypeAndGraph(String label, Integer weight, NodeType type, GraphV2 graph);

        Optional<NodeV2> findNodeByLabelIgnoreCaseAndWeightAndTypeAndGraph(String label, Integer weight, NodeType type, GraphV2 graph);

}
